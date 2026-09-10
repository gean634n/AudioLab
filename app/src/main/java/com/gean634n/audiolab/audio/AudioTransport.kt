package com.gean634n.audiolab.audio

interface AudioTransport {
    fun sendFloat(
        receiver: String,
        value: Float
    )
}