package com.gean634n.audiolab.audio

import android.content.Context
import org.puredata.android.io.PdAudio
import org.puredata.core.PdBase
import java.io.File
import kotlin.math.pow
import android.media.AudioManager
import com.gean634n.audiolab.ui.waveform.WaveformType

class AudioEngine (
    private val context: Context,
    private val transport: AudioTransport = AudioTransportFactory.create()
) {
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
    }

    fun stop() {
        PdAudio.release()
    }

    fun mute() {
        transport.sendFloat("level", 0f)
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
        val amplitude = 10f.pow(db / 20f)
        transport.sendFloat("level", amplitude)
    }

    fun setFrequencyHz(hz: Float) {
        transport.sendFloat("frequency", hz)
    }

    fun setWaveform(type: WaveformType) {
        val value = when (type) {
            WaveformType.SINE -> 0f
            WaveformType.SAWTOOTH -> 1f
            WaveformType.SQUARE -> 2f
            WaveformType.TRIANGLE -> 3f
        }

        transport.sendFloat("waveform", value)
    }
}

