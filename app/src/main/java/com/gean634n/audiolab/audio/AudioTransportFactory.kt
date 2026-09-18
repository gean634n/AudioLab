package com.gean634n.audiolab.audio

object AudioTransportFactory {

    fun create(
        settings: AudioSettings,
        computerAvailable: Boolean
    ): AudioTransport {
        return when {
            settings.executionMode == AudioExecutionMode.COMPUTER &&
                    computerAvailable -> {
                UdpTransport(
                    host = settings.computerHost,
                    port = settings.computerPort.toInt()
                )
            }

            else -> LibPdTransport()
        }
    }
}