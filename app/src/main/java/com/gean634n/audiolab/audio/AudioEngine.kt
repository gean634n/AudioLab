package com.gean634n.audiolab.audio

import android.content.Context
import org.puredata.android.io.PdAudio
import org.puredata.core.PdBase
import java.io.File
import kotlin.math.pow
import android.media.AudioManager

class AudioEngine (
    private val context: Context
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

    private fun copyPatchToInternalStorage(): File {
        val patchFile = File(context.filesDir, "test.pd")

        context.assets.open("test.pd").use { input ->
            patchFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return patchFile
    }

    fun setLevelDb(db: Float) {
        val amplitude = 10f.pow(db / 20f)
        PdBase.sendFloat("nivel", amplitude)
    }
}

