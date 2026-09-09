package com.gean634n.audiolab.drawing

import kotlin.math.abs
import kotlin.math.sqrt

data class StrokeMetrics(
    val durationMillis: Long,
    val averageSpeed: Float,
    val averageX: Float,
    val averageY: Float,
    val direction: StrokeDirection
)

fun Stroke.calculateMetrics(): StrokeMetrics {
    if (points.isEmpty()) {
        return StrokeMetrics(
            durationMillis = 0L,
            averageSpeed = 0f,
            averageX = 0f,
            averageY = 0f,
            direction = StrokeDirection.NEUTRAL
        )
    }

    val durationMillis =
        points.last().timeMillis - points.first().timeMillis

    val averageX =
        points.map { it.x }.average().toFloat()

    val averageY =
        points.map { it.y }.average().toFloat()

    var totalDistance = 0f

    var upDistance = 0f
    var downDistance = 0f
    var leftDistance = 0f
    var rightDistance = 0f

    for (i in 1 until points.size) {
        val previous = points[i - 1]
        val current = points[i]

        val segmentX = current.x - previous.x
        val segmentY = current.y - previous.y

        totalDistance += sqrt(
            segmentX * segmentX +
                    segmentY * segmentY
        )

        if (segmentX > 0f) {
            rightDistance += segmentX
        } else {
            leftDistance += -segmentX
        }

        if (segmentY > 0f) {
            downDistance += segmentY
        } else {
            upDistance += -segmentY
        }
    }

    val directionDistances = mapOf(
        StrokeDirection.UP to upDistance,
        StrokeDirection.DOWN to downDistance,
        StrokeDirection.LEFT to leftDistance,
        StrokeDirection.RIGHT to rightDistance
    )

    // TODO: Improve direction analysis.
    // The current implementation selects only the direction with the greatest
    // accumulated movement. Consider representing diagonals, ambiguity between
    // similar directions, and direction distribution along the stroke.
    val direction =
        directionDistances.maxByOrNull { it.value }
            ?.takeIf { it.value > 0f }
            ?.key
            ?: StrokeDirection.NEUTRAL

    val durationSeconds =
        durationMillis / 1000f

    val averageSpeed =
        if (durationSeconds > 0f) {
            totalDistance / durationSeconds
        } else {
            0f
        }

    return StrokeMetrics(
        durationMillis = durationMillis,
        averageSpeed = averageSpeed,
        averageX = averageX,
        averageY = averageY,
        direction = direction
    )
}