package com.gean634n.audiolab.audio

object AudioTransportFactory {

    fun create(): AudioTransport {
        return when (AudioConfig.mode) {
            AudioMode.NORMAL -> LibPdTransport()

            AudioMode.DEBUG -> UdpTransport(
                host = AudioConfig.debugHost,
                port = AudioConfig.debugPort
            )
        }
    }
}