package com.gean634n.audiolab.audio

import android.content.Context
import org.puredata.android.io.PdAudio
import org.puredata.core.PdBase
import java.io.File

class AudioEngine (
    private val context: Context
) {

    fun start() {
        PdAudio.initAudio(
            44100,
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
}

