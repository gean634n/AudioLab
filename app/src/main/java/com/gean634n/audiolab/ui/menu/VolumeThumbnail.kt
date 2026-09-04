package com.gean634n.audiolab.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.theme.TransparentColor
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun VolumeThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val triangle = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            close()
        }

        drawPath(
            path = triangle,
            color = Color(0xFFA5D8FF)
        )

        drawPath(
            path = triangle,
            color = Color(0xFF1E1E1E),
            style = Stroke(width = 2.dp.toPx())
        )

        val wave = Path()

        val centerY = size.height / 2f
        val amplitude = size.height * 0.30f
        val cycles = 5f

        for (x in 0..size.width.toInt()) {
            val angle = (x / size.width) * 2f * PI.toFloat() * cycles

            val y = centerY - sin(angle) * amplitude

            if (x == 0) {
                wave.moveTo(x.toFloat(), y)
            } else {
                wave.lineTo(x.toFloat(), y)
            }
        }

        drawPath(
            path = wave,
            color = TransparentColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}