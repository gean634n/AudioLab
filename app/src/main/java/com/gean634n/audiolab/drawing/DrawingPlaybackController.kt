package com.gean634n.audiolab.drawing

class DrawingPlaybackController(
    val durationMillis: Long
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


    fun playbackEndMillis(
        strokes: List<Stroke>,
        mode: PlaybackAnimationMode
    ): Long {
        if (
            mode == PlaybackAnimationMode.HIDE_ALL_SHOW_FULL ||
            mode == PlaybackAnimationMode.BLINK_FULL
        ) {
            return durationMillis
        }

        val lastStrokeEnd = strokes.maxOfOrNull { stroke ->
            val startX = stroke.points.firstOrNull()?.x ?: 0f

            val strokeDuration =
                if (stroke.points.size >= 2) {
                    stroke.points.last().timeMillis -
                            stroke.points.first().timeMillis
                } else {
                    0L
                }

            triggerTimeMillis(startX) + strokeDuration
        } ?: 0L

        return maxOf(
            durationMillis,
            lastStrokeEnd
        )
    }
}