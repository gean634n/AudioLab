package com.gean634n.audiolab.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.waveform.buildSawtoothPath
import com.gean634n.audiolab.ui.waveform.buildSinePath
import com.gean634n.audiolab.ui.waveform.buildSquarePath
import com.gean634n.audiolab.ui.waveform.buildTrianglePath

@Composable
fun OscillatorsThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val gap = 4.dp.toPx()

        val columnWidth =
            (size.width - gap * 3) / 4f

        val centerY = size.height / 2f
        val amplitude = size.height * 0.25f
        val strokeWidth = 2.dp.toPx()

        val colors = listOf(
            Color(0xFFA8D8A8),
            Color(0xFFFFE89A),
            Color(0xFFFFB3C1),
            Color(0xFFA5D8FF)
        )

        val paths = listOf(
            buildSquarePath(
                width = columnWidth,
                centerY = centerY,
                amplitude = amplitude,
                cycles = 2
            ),

            buildTrianglePath(
                width = columnWidth,
                centerY = centerY,
                amplitude = amplitude,
                cycles = 2
            ),

            buildSawtoothPath(
                width = columnWidth,
                centerY = centerY,
                amplitude = amplitude,
                cycles = 2
            ),

            buildSinePath(
                width = columnWidth,
                centerY = centerY,
                amplitude = amplitude,
                cycles = 2f
            )
        )

        paths.forEachIndexed { index, path ->

            val left =
                index * (columnWidth + gap)

            withTransform({
                translate(left = left)
            }) {

                // Fundo individual
                drawRect(
                    color = colors[index],
                    size = Size(
                        width = columnWidth,
                        height = size.height
                    )
                )

                // A onda só pode existir dentro deste quadro
                clipRect(
                    left = 0f,
                    top = 0f,
                    right = columnWidth,
                    bottom = size.height
                ) {
                    drawPath(
                        path = path,
                        color = Color(0xFF1E1E1E),
                        style = Stroke(
                            width = strokeWidth
                        )
                    )
                }

                // Borda individual
                drawRect(
                    color = Color(0xFF1E1E1E),
                    size = Size(
                        width = columnWidth,
                        height = size.height
                    ),
                    style = Stroke(
                        width = strokeWidth
                    )
                )
            }
        }
    }
}