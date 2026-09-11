package com.gean634n.audiolab.audio

object AudioTransportFactory {

    fun create(debugAvailable: Boolean): AudioTransport {
        return when {
            AudioConfig.mode == AudioMode.DEBUG && debugAvailable ->
                UdpTransport(
                    host = AudioConfig.debugHost,
                    port = AudioConfig.debugPort
                )

            else ->
                LibPdTransport()
        }
    }
}