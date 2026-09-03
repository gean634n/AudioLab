package com.gean634n.audiolab.ui.volume

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.theme.OutlineColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import com.gean634n.audiolab.ui.theme.TransparentColor
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun Waveform(
    levelDb: Float,
    cycles: Float = 1f,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "wave")
    val progress = ((levelDb - MIN_LEVEL_DB) / (MAX_LEVEL_DB - MIN_LEVEL_DB)).coerceIn(0f, 1f)

    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            )
        ),
        label = "phase"
    )
    Canvas(
        modifier = modifier
    ) {
        val path = Path()

        val centerY = size.height / 2f
        val amplitude = (size.height / 2f) * progress

        for (x in 0..size.width.toInt()) {
            val angle = (x / size.width) * 2f * PI.toFloat() * cycles + phase
            val y = centerY - sin(angle) * amplitude

            if (x == 0) {
                path.moveTo(x.toFloat(), y)
            } else {
                path.lineTo(x.toFloat(), y)
            }
        }

        drawPath(
            path = path,
            color = TransparentColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Preview
@Composable
fun WaveformPreview() {
    Waveform(
        levelDb = -12f,
        cycles = 3f,
        modifier = Modifier
            .size(
                width = 300.dp,
                height = 120.dp
            )
            .background(Color.White)
    )
}