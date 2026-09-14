package com.gean634n.audiolab.drawing

class DrawingPlaybackController(
    val durationMillis: Long = 8_000L
) {

    fun playheadX(elapsedMillis: Long): Float {
        return (elapsedMillis.toFloat() / durationMillis)
            .coerceIn(0f, 1f)
    }

    fun triggerTimeMillis(startX: Float): Long {
        return (startX.coerceIn(0f, 1f) * durationMillis)
            .toLong()
    }

    fun strokeElapsedMillis(
        startX: Float,
        elapsedMillis: Long
    ): Long {
        return (
                elapsedMillis - triggerTimeMillis(startX)
                ).coerceAtLeast(0L)
    }

    fun hasReached(
        startX: Float,
        elapsedMillis: Long
    ): Boolean {
        return elapsedMillis >= triggerTimeMillis(startX)
    }

    fun isFinished(elapsedMillis: Long): Boolean {
        return elapsedMillis >= durationMillis
    }
}