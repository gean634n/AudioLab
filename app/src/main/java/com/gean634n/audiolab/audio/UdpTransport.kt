package com.gean634n.audiolab.audio

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.Executors

class UdpTransport(
    host: String,
    private val port: Int
) : AudioTransport {

    private val address = InetAddress.getByName(host)
    private val socket = DatagramSocket()

    private val executor = Executors.newSingleThreadExecutor()

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        executor.execute {
            val message = "$receiver $value;\n"
            val data = message.toByteArray()

            val packet = DatagramPacket(
                data,
                data.size,
                address,
                port
            )

            socket.send(packet)
        }
    }

    fun close() {
        executor.shutdown()
        socket.close()
    }
}