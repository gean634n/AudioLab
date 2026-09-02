package com.gean634n.audiolab.audio

import org.puredata.android.io.PdAudio

class AudioEngine {

    fun start() {
        PdAudio.initAudio(
            0,
            2,
            44100,
            10,
            true
        )
    }

    fun stop() {
        PdAudio.release()
    }
}