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

    fun close() {
        client.close()
    }
}