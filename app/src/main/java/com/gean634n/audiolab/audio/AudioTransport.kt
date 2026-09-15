package com.gean634n.audiolab.audio

interface AudioTransport {
    fun sendFloat(
        receiver: String,
        value: Float
    )

    fun sendString(
        receiver: String,
        value: String
    )
}