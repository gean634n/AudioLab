package com.gean634n.audiolab.audio

import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class UdpHandshake(
    host: String,
    private val sendPort: Int,
    private val replyPort: Int,
    private val timeoutMillis: Int
) {

    private val address = InetAddress.getByName(host)

    fun check(): Boolean {
        return try {
            DatagramSocket(replyPort).use { socket ->
                socket.soTimeout = timeoutMillis

                sendPing(socket)
                waitForPong(socket)
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun sendPing(socket: DatagramSocket) {
        val data = "ping;\n".toByteArray()

        val packet = DatagramPacket(
            data,
            data.size,
            address,
            sendPort
        )

        socket.send(packet)
    }

    private fun waitForPong(socket: DatagramSocket): Boolean {
        val buffer = ByteArray(64)

        val packet = DatagramPacket(
            buffer,
            buffer.size
        )

        return try {
            socket.receive(packet)

            val response = String(
                packet.data,
                0,
                packet.length
            ).trim()

//            Log.d(
//                "AudioDebug",
//                "Handshake response: '$response' from ${packet.address.hostAddress}:${packet.port}"
//            )

            response == "pong;"
        } catch (_: SocketTimeoutException) {
            false
        }
    }
}