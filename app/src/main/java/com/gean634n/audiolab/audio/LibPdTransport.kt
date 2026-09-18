package com.gean634n.audiolab.audio

import android.util.Log
import org.puredata.core.PdBase

class LibPdTransport : AudioTransport {

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        Log.d("AudioDebug", "libpd float: $receiver = $value")
        PdBase.sendFloat(receiver, value)
    }

    override fun sendString(
        receiver: String,
        value: String
    ) {
        Log.d("AudioDebug", "libpd string: $receiver = $value")
        PdBase.sendSymbol(receiver, value)
    }

    override fun sendMessage(
        receiver: String,
        vararg args: Any
    ) {
        PdBase.sendList(receiver, *args)
    }

    override fun close() {
        // Nada é necessário para liberar
    }
}