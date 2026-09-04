package com.gean634n.audiolab.ui.waveforms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun WaveformColumn(
    backgroundColor: Color,
    waveformType: WaveformType,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        drawRect(
            color = backgroundColor
        )

        drawRect(
                color = Color(0xFF1E1E1E),
            style = Stroke(width = 2.dp.toPx())
        )

        val centerY = size.height / 2f
        val amplitude = size.height * 0.25f
        val cycles = 2
        val cycleWidth = size.width / cycles
        val path = Path()

        when (waveformType) {
            WaveformType.SQUARE -> {
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
            }

            WaveformType.TRIANGLE -> {
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
            }

            WaveformType.SAWTOOTH -> {
                path.moveTo(0f, centerY + amplitude)

                for (i in 0 until cycles) {
                    val xEnd = (i + 1) * cycleWidth

                    path.lineTo(xEnd, centerY - amplitude)

                    if (i < cycles - 1) {
                        path.lineTo(xEnd, centerY + amplitude)
                    }
                }
            }

            WaveformType.SINE -> {
                for (x in 0..size.width.toInt()) {
                    val angle =
                        (x / size.width) * 2f * PI.toFloat() * cycles

                    val y = centerY - sin(angle) * amplitude

                    if (x == 0) {
                        path.moveTo(x.toFloat(), y)
                    } else {
                        path.lineTo(x.toFloat(), y)
                    }
                }
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF1E1E1E),
            style = Stroke(width = 2.dp.toPx())
        )
//
//        if (waveformType == WaveformType.SQUARE) {
//            val path = Path()
//
//            val centerY = size.height / 2f
//            val amplitude = size.height * 0.25f
//            val cycles = 4
//            val cycleWidth = size.width / cycles
//
//            path.moveTo(0f, centerY - amplitude)
//
//            for (i in 0 until cycles) {
//                val xStart = i * cycleWidth
//                val xMiddle = xStart + cycleWidth / 2f
//                val xEnd = xStart + cycleWidth
//
//                path.lineTo(xMiddle, centerY - amplitude)
//                path.lineTo(xMiddle, centerY + amplitude)
//                path.lineTo(xEnd, centerY + amplitude)
//
//                if (i < cycles - 1) {
//                    path.lineTo(xEnd, centerY - amplitude)
//                }
//            }
//
//            drawPath(
//                path = path,
//                color = Color(0xFF1E1E1E),
//                style = Stroke(width = 2.dp.toPx())
//            )
//        }
//
//        if (waveformType == WaveformType.TRIANGLE) {
//            val path = Path()
//
//            val centerY = size.height / 2f
//            val amplitude = size.height * 0.25f
//            val cycles = 4
//            val cycleWidth = size.width / cycles
//
//            path.moveTo(0f, centerY)
//
//            for (i in 0 until cycles) {
//                val xStart = i * cycleWidth
//                val xQuarter = xStart + cycleWidth * 0.25f
//                val xThreeQuarter = xStart + cycleWidth * 0.75f
//                val xEnd = xStart + cycleWidth
//
//                path.lineTo(xQuarter, centerY - amplitude)
//                path.lineTo(xThreeQuarter, centerY + amplitude)
//                path.lineTo(xEnd, centerY)
//            }
//
//            drawPath(
//                path = path,
//                color = Color(0xFF1E1E1E),
//                style = Stroke(width = 2.dp.toPx())
//            )
//        }
//
//        if (waveformType == WaveformType.SAWTOOTH) {
//            val path = Path()
//
//            val centerY = size.height / 2f
//            val amplitude = size.height * 0.25f
//            val cycles = 4
//            val cycleWidth = size.width / cycles
//
//            path.moveTo(0f, centerY + amplitude)
//
//            for (i in 0 until cycles) {
//                val xEnd = (i + 1) * cycleWidth
//
//                path.lineTo(xEnd, centerY - amplitude)
//
//                if (i < cycles - 1) {
//                    path.lineTo(xEnd, centerY + amplitude)
//                }
//            }
//
//            drawPath(
//                path = path,
//                color = Color(0xFF1E1E1E),
//                style = Stroke(width = 2.dp.toPx())
//            )
//        }
//
//        if (waveformType == WaveformType.SINE) {
//            val path = Path()
//
//            val centerY = size.height / 2f
//            val amplitude = size.height * 0.25f
//            val cycles = 4f
//
//            for (x in 0..size.width.toInt()) {
//                val angle = (x / size.width) * 2f * PI.toFloat() * cycles
//
//                val y = centerY - sin(angle) * amplitude
//
//                if (x == 0) {
//                    path.moveTo(x.toFloat(), y)
//                } else {
//                    path.lineTo(x.toFloat(), y)
//                }
//            }
//
//            drawPath(
//                path = path,
//                color = Color(0xFF1E1E1E),
//                style = Stroke(width = 2.dp.toPx())
//            )
//        }
    }
}