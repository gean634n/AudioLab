package com.gean634n.audiolab.audio

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpTransport(
    host: String,
    private val port: Int
) : AudioTransport {

    private val address = InetAddress.getByName(host)
    private val socket = DatagramSocket()

    override fun sendFloat(
        receiver: String,
        value: Float
    ) {
        val message = "$receiver $value;"
        val data = message.toByteArray()

        val packet = DatagramPacket(
            data,
            data.size,
            address,
            port
        )

        socket.send(packet)
    }

    fun close() {
        socket.close()
    }
}