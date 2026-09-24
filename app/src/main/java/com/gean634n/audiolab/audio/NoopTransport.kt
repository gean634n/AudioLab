package com.gean634n.audiolab.audio

object NoopTransport : AudioTransport {
    override fun sendFloat(receiver: String, value: Float) {}
    override fun sendString(receiver: String, value: String) {}
    override fun sendMessage(receiver: String, vararg args: Any) {}
    override fun close() {}
}
