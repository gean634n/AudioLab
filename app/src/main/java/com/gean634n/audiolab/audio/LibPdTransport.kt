package com.gean634n.audiolab.audio

import org.puredata.core.PdBase

class LibPdTransport : AudioTransport {

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        PdBase.sendFloat(receiver, value)
    }
}