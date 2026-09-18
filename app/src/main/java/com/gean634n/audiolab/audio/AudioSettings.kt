package com.gean634n.audiolab.audio

data class AudioSettings(
    val executionMode: AudioExecutionMode = AudioExecutionMode.DEVICE,
    val computerHost: String = "10.42.0.1",
    val computerPort: String = "9000",
    val replyPort: String = "9001"
) {
    val isComputerPortValid: Boolean get() = computerPort.toIntOrNull() in 1..65535

    val isComputerIpValid: Boolean get() {
        val parts = computerHost.split(".")
        return parts.size == 4 &&
                    parts.all { part ->
                        part.isNotEmpty() &&
                                part.toIntOrNull() in 0..255
                    }
    }

    val isComputerConfigValid: Boolean get() = isComputerIpValid && isComputerPortValid
}