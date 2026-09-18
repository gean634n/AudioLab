package com.gean634n.audiolab.audio

import io.github.termtate.kotlinosc.arg.toOscFloat32
import io.github.termtate.kotlinosc.arg.toOscString
import io.github.termtate.kotlinosc.transport.OscClient
import io.github.termtate.kotlinosc.type.OscMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        scope.launch {
            client.send(
                OscMessage(
                    address = receiver,
                    args = listOf(value.toOscFloat32())
                )
            )
        }
    }

    override fun sendString(
        receiver: String,
        value: String
    ) {
        scope.launch {
            client.send(
                OscMessage(
                    address = receiver,
                    args = listOf(value.toOscString())
                )
            )
        }
    }

    override fun sendMessage(
        receiver: String,
        vararg args: Any
    ) {
        val oscArgs = args.map { arg ->
            when (arg) {
                is Float -> arg.toOscFloat32()
                is Int -> arg.toOscInt32()
                is String -> arg.toOscString()
                else -> error(
                    "Unsupported OSC argument type: ${arg::class.simpleName}"
                )
            }
        }

        scope.launch {
            client.send(
                OscMessage(
                    address = receiver,
                    args = oscArgs
                )
            )
        }
    }

    override fun close() {
        scope.cancel()
        client.close()
    }
}