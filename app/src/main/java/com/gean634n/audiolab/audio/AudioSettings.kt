package com.gean634n.audiolab.audio

data class AudioSettings(
    val executionMode: AudioExecutionMode = AudioExecutionMode.DEVICE,
    val computerHost: String = "",
    val computerPort: Int = 9000,
    val replyPort: Int = 9001
)