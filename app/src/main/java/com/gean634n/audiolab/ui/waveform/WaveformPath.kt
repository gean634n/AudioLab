package com.gean634n.audiolab.ui.waveform

import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.sin

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
    val path = Path()
    val cycleWidth = width / cycles

    path.moveTo(0f, centerY - amplitude)

    for (i in 0 until cycles) {
        val xStart = i * cycleWidth
        val xMiddle = xStart + cycleWidth / 2f
        val xEnd = xStart + cycleWidth

        path.lineTo(xMiddle, centerY - amplitude)
        path.lineTo(xMiddle, centerY + amplitude)
        path.lineTo(xEnd, centerY + amplitude)

        if (i < cycles - 1) {
            path.lineTo(xEnd, centerY - amplitude)
        }
    }

    return path
}

fun buildTrianglePath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int
): Path {
    val path = Path()
    val cycleWidth = width / cycles

    path.moveTo(0f, centerY)

    for (i in 0 until cycles) {
        val xStart = i * cycleWidth
        val xQuarter = xStart + cycleWidth * 0.25f
        val xThreeQuarter = xStart + cycleWidth * 0.75f
        val xEnd = xStart + cycleWidth

        path.lineTo(xQuarter, centerY - amplitude)
        path.lineTo(xThreeQuarter, centerY + amplitude)
        path.lineTo(xEnd, centerY)
    }

    return path
}

fun buildSawtoothPath(
    width: Float,
    centerY: Float,
    amplitude: Float,
    cycles: Int
): Path {
    val path = Path()
    val cycleWidth = width / cycles

    path.moveTo(0f, centerY + amplitude)

    for (i in 0 until cycles) {
        val xEnd = (i + 1) * cycleWidth

        path.lineTo(xEnd, centerY - amplitude)

        if (i < cycles - 1) {
            path.lineTo(xEnd, centerY + amplitude)
        }
    }

    return path
}