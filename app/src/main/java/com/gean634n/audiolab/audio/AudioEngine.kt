package com.gean634n.audiolab.audio

import android.content.Context
import org.puredata.android.io.PdAudio
import org.puredata.core.PdBase
import java.io.File
import kotlin.math.pow
import android.media.AudioManager
import com.gean634n.audiolab.ui.waveform.WaveformType
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.text.toInt

class AudioEngine (
    appContext: Context
) {
    private val context = appContext.applicationContext

    @Volatile
    private var transport: AudioTransport = LibPdTransport()

    @Volatile
    private var settings = AudioSettings()

    private val transportSelectionVersion = AtomicLong(0)

    private val _state = MutableStateFlow(SynthState())
    val state: StateFlow<SynthState> = _state.asStateFlow()

    private val _executionState = MutableStateFlow(AudioExecutionState.DEVICE)
    val executionState: StateFlow<AudioExecutionState> = _executionState.asStateFlow()

    // TODO: Separar a inicialização do engine da seleção do transporte.
    //  Atualmente start() ainda seleciona o transporte antes de o DataStore
    //  terminar de carregar as configurações persistidas.
    fun start() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val sampleRate = audioManager.getProperty(
            AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
        )?.toInt() ?: 44100

        PdAudio.initAudio(
            sampleRate,
            0,
            2,
            1,
            true
        )

        val patchFile = copyPatchToInternalStorage()

        PdBase.openPatch(patchFile)

        PdAudio.startAudio(context)
        selectTransport(settings)
    }

    private fun replaceTransport(newTransport: AudioTransport) {
        val oldTransport = transport
        transport = newTransport
        oldTransport.close()
    }

    @Synchronized
    private fun selectTransport(settings: AudioSettings) {
        val selectionVersion = transportSelectionVersion.incrementAndGet()

        if (settings.executionMode == AudioExecutionMode.DEVICE) {

            replaceTransport(
                AudioTransportFactory.create(
                    settings = settings,
                    computerAvailable = false
                )
            )

            _executionState.value = AudioExecutionState.DEVICE

            Log.d("AudioDebug", "Transport: LOCAL")
            return
        }

        _executionState.value = AudioExecutionState.COMPUTER_CONNECTING

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val handshake = UdpHandshake(
                host = settings.computerHost,
                sendPort = settings.computerPort.toInt(),
                replyPort = settings.replyPort.toInt(),
                timeoutMillis = 500
            )

            val computerAvailable = handshake.check()

            // Keep version validation and installation atomic with stop().
            synchronized(this@AudioEngine) {
                if (selectionVersion != transportSelectionVersion.get()) {
                    executor.shutdown()
                    return@execute
                }

                replaceTransport(
                    AudioTransportFactory.create(
                        settings = settings,
                        computerAvailable = computerAvailable
                    )
                )

                _executionState.value =
                    if (computerAvailable) {
                        AudioExecutionState.COMPUTER
                    } else {
                        AudioExecutionState.COMPUTER_UNAVAILABLE
                    }

                Log.d(
                    "AudioDebug",
                    if (computerAvailable) {
                        "Transport: UDP"
                    } else {
                        "Transport: LOCAL"
                    }
                )
            }

            executor.shutdown()
        }
    }

    fun applySettings(newSettings: AudioSettings) {
        if (newSettings == settings) {
            return
        }

        settings = newSettings
        selectTransport(newSettings)
    }

    fun retryComputerConnection() {
        if (settings.executionMode != AudioExecutionMode.COMPUTER) {
            return
        }

        selectTransport(settings)
    }

    // TODO: Define background audio policy.
    //  Volume and TouchPad currently keep producing audio while the app is
    //  in the background. Decide whether AudioEngine should stop or mute
    //  globally when the app leaves the foreground.
    @Synchronized
    fun stop() {
        transportSelectionVersion.incrementAndGet()
        playAbort()
        val old = transport
        transport = NoopTransport
        old.close()
        PdAudio.release()
    }

    fun mute() {
        transport.sendFloat("/audio/level", 0f)
    }

    private fun copyPatchToInternalStorage(): File {
        val patchFile = File(context.filesDir, "patch.pd")

        context.assets.open("patch.pd").use { input ->
            patchFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return patchFile
    }

    fun setLevelDb(db: Float) {
        _state.update { it.copy(levelDb = db) }

        val amplitude = 10f.pow(db / 20f)
        transport.sendFloat("/audio/level", amplitude)
    }

    fun setFrequencyHz(hz: Float) {
        _state.update { it.copy(frequencyHz = hz) }
        transport.sendFloat("/audio/frequency", hz)
    }

    fun setWaveform(type: WaveformType) {
        _state.update { it.copy(waveform = type) }

        val value = when (type) {
            WaveformType.SINE -> "sine"
            WaveformType.SAWTOOTH -> "sawtooth"
            WaveformType.SQUARE -> "square"
            WaveformType.TRIANGLE -> "triangle"
        }

        transport.sendString("/audio/waveform", value)
    }

    fun startDrawingStroke(
        id: Int,
        tool: String,
        lineStyle: String,
        red: Float,
        green: Float,
        blue: Float,
    ) {
        transport.sendMessage(
            "/draw/start",
            id,
            tool,
            lineStyle,
            red,
            green,
            blue,
        )
    }

    fun sendDrawingPoint(
        id: Int,
        x: Float,
        y: Float,
        elapsedMillis: Long,
    ) {
        transport.sendMessage(
            "/draw/point",
            id,
            x,
            y,
            elapsedMillis.toFloat(),
        )
    }

    fun endDrawingStroke(id: Int) {
        transport.sendMessage(
            "/draw/end",
            id,
        )
    }

    fun playStart(durationMillis: Int, mode: String) {
        transport.sendMessage("/play/start", durationMillis, mode)
    }

    fun playStroke(
        voice: Int,
        id: Int,
        tool: String,
        lineStyle: String,
        red: Float,
        green: Float,
        blue: Float
    ) {
        transport.sendMessage(
            "/play/v$voice/stroke",
            id,
            tool,
            lineStyle,
            red,
            green,
            blue,
        )
    }

    fun playPoint(
        voice: Int,
        id: Int,
        x: Float,
        y: Float,
        timeMillis: Float
    ) {
        transport.sendMessage(
            "/play/v$voice/point",
            id,
            x,
            y,
            timeMillis,
        )
    }

    fun playEnd(voice: Int, id: Int) {
        transport.sendMessage("/play/v$voice/end", id)
    }

    fun playPause() {
        transport.sendMessage("/play/pause")
    }

    fun playResume() {
        transport.sendMessage("/play/resume")
    }

    fun playFinish() {
        transport.sendMessage("/play/finish")
    }

    fun playAbort() {
        transport.sendMessage("/play/abort")
    }

}
