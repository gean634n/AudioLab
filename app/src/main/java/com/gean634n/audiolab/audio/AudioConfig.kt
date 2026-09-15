package com.gean634n.audiolab.audio

object AudioConfig {

    var mode = AudioMode.DEBUG
//    var mode = AudioMode.NORMAL

    const val debugHost = "10.42.0.1"
//const val debugHost = "192.168.0.7"

    const val debugPort = 9000
    const val debugReplyPort = 9001
    const val handshakeTimeoutMillis = 500
}