package com.gean634n.audiolab.audio

import android.util.Log
import io.github.termtate.kotlinosc.arg.toOscFloat32
import io.github.termtate.kotlinosc.arg.toOscString
import io.github.termtate.kotlinosc.transport.OscClient
import io.github.termtate.kotlinosc.type.OscMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import kotlinx.coroutines.cancel
import io.github.termtate.kotlinosc.arg.toOscInt32

class UdpTransport(
    host: String,
    port: Int
) : AudioTransport {

    private val client = OscClient(
        targetAddress = InetSocketAddress(host, port)
    )

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private val queue = Channel<OscMessage>(Channel.UNLIMITED)

    private val consumer = scope.launch {
        for (message in queue) {
            try {
                client.send(message)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.w("AudioDebug", "Failed to send UDP message", exception)
            }
        }
    }

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        queue.trySend(
            OscMessage(
                address = receiver,
                args = listOf(value.toOscFloat32())
            )
        )
    }

    override fun sendString(
        receiver: String,
        value: String
    ) {
        queue.trySend(
            OscMessage(
                address = receiver,
                args = listOf(value.toOscString())
            )
        )
    }

    override fun sendMessage(
        receiver: String,
        vararg args: Any
    ) {
        val clean = sanitizeOscArgs(receiver, args) ?: return
        val oscArgs = clean.map { arg ->
            when (arg) {
                is Float -> arg.toOscFloat32()
                is Int -> arg.toOscInt32()
                is String -> arg.toOscString()
                else -> {
                    Log.e("AudioDebug", "Unexpected sanitized OSC argument for $receiver")
                    return
                }
            }
        }

        queue.trySend(
            OscMessage(
                address = receiver,
                args = oscArgs
            )
        )
    }

    override fun close() {
        if (!queue.close()) return

        val closeScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        closeScope.launch {
            try {
                withTimeoutOrNull(300L) {
                    consumer.join()
                }
            } finally {
                scope.cancel()
                try {
                    client.close()
                } catch (exception: Exception) {
                    Log.w("AudioDebug", "Failed to close UDP client", exception)
                } finally {
                    closeScope.cancel()
                }
            }
        }
    }
}
