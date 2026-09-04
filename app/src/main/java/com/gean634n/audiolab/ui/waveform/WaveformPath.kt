package com.gean634n.audiolab.ui.waveform

import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.sin

private fun buildLinearWavePath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int,
    points: List<Pair<Float, Float>>
): Path {
    val path = Path()
    val cycleWidth = width / cycles

    for (cycle in 0 until cycles) {
        val cycleStartX = cycle * cycleWidth

        for ((index, point) in points.withIndex()) {
            val normalizedX = point.first
            val normalizedY = point.second

            val x = cycleStartX + normalizedX * cycleWidth
            val y = centerY + normalizedY * amplitude

            if (cycle == 0 && index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
    }

    return path
}

fun buildSinePath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Float,
    phase: Float = 0f
): Path {
    val path = Path()

    for (x in 0..width.toInt()) {
        val angle = (x / width) * 2f * PI.toFloat() * cycles + phase

        val y = centerY - sin(angle) * amplitude

        if (x == 0) {
            path.moveTo(x.toFloat(), y)
        } else {
            path.lineTo(x.toFloat(), y)
        }
    }

    return path
}

fun buildSquarePath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int
): Path {
    val points = listOf(
        0f to -1f,
        0.5f to -1f,
        0.5f to 1f,
        1f to 1f
    )

    return buildLinearWavePath(
        width = width,
        centerY = centerY,
        amplitude = amplitude,
        cycles = cycles,
        points = points
    )
}

fun buildTrianglePath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int
): Path {
    val points = listOf(
        0f to 0f,
        0.25f to -1f,
        0.75f to 1f,
        1f to 0f
    )

    return buildLinearWavePath(
        width = width,
        centerY = centerY,
        amplitude = amplitude,
        cycles = cycles,
        points = points
    )
}

fun buildSawtoothPath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int
): Path {
    val points = listOf(
        0f to 1f,
        1f to -1f
    )

    return buildLinearWavePath(
        width = width,
        centerY = centerY,
        amplitude = amplitude,
        cycles = cycles,
        points = points
    )
}