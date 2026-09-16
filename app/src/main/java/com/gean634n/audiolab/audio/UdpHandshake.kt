package com.gean634n.audiolab.audio

import android.util.Log
import io.github.termtate.kotlinosc.arg.toOscInt32
import io.github.termtate.kotlinosc.arg.toOscString
import io.github.termtate.kotlinosc.exception.OscCodecException
import io.github.termtate.kotlinosc.transport.OscClient
import io.github.termtate.kotlinosc.transport.OscTransportHook
import io.github.termtate.kotlinosc.transport.dsl.oscServer
import io.github.termtate.kotlinosc.type.OscMessage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.net.DatagramSocket
import java.net.InetSocketAddress
import kotlin.time.Duration.Companion.milliseconds

class UdpHandshake(
    host: String,
    private val sendPort: Int,
    private val replyPort: Int,
    private val timeoutMillis: Int
) {

    private val targetAddress = InetSocketAddress(
        host,
        sendPort
    )

    private fun resolveLocalIp(): String? {
        return try {
            DatagramSocket().use { socket ->
                socket.connect(targetAddress)
                socket.localAddress.hostAddress
            }
        } catch (_: Exception) {
            null
        }
    }

    fun check(): Boolean = runBlocking {
        val pongReceived = CompletableDeferred<Unit>()

        val server = oscServer(
            ipAddress = "0.0.0.0",
            port = replyPort
        ) {
            transportHook = object : OscTransportHook {

                override fun onDecodeError(
                    payload: ByteArray,
                    error: OscCodecException
                ) {
                    Log.d(
                        "AudioDebug",
                        "OSC decode error: ${error.message}; bytes=${payload.joinToString()}"
                    )
                }

                override fun onTransportError(error: Throwable) {
                    Log.d(
                        "AudioDebug",
                        "OSC transport error: ${error.message}"
                    )
                }
            }

            route {
                on("/system/pong") {
                    Log.d("AudioDebug", "OSC pong received")
                    pongReceived.complete(Unit)
                }
            }
        }

        val client = OscClient(
            targetAddress = targetAddress
        )

        try {
            server.start()
            val localIp = resolveLocalIp() ?: return@runBlocking false
            Log.d(
                "AudioDebug",
                "Local IP for $targetAddress: $localIp"
            )

            client.send(
                OscMessage(
                    address = "/system/ping",
                    args = listOf(
                        localIp.toOscString(),
                        replyPort.toOscInt32()
                    )
                )
            )

            withTimeoutOrNull(timeoutMillis.milliseconds) {
                pongReceived.await()
            } != null

        } catch (_: Exception) {
            false

        } finally {
            client.closeAndJoin()
            server.stop()
        }
    }
}