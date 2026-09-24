package com.gean634n.audiolab.audio

import android.util.Log

internal fun sanitizeOscArgs(
    receiver: String,
    args: Array<out Any>
): List<Any>? {
    return args.map { arg ->
        when (arg) {
            is Int, is Float, is String -> arg
            is Long -> if (arg in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) {
                arg.toInt()
            } else {
                arg.toFloat()
            }
            is Double -> arg.toFloat()
            is Short -> arg.toInt()
            is Byte -> arg.toInt()
            is Boolean -> if (arg) 1 else 0
            else -> {
                Log.e("AudioDebug", "Unsupported OSC argument for $receiver: ${arg.javaClass.name}")
                return null
            }
        }
    }
}
