package com.gean634n.audiolab.ui.oscillators

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.waveform.WaveformType
import com.gean634n.audiolab.ui.waveform.buildSawtoothPath
import com.gean634n.audiolab.ui.waveform.buildSinePath
import com.gean634n.audiolab.ui.waveform.buildSquarePath
import com.gean634n.audiolab.ui.waveform.buildTrianglePath
import kotlin.math.PI

@Composable
fun OscillatorColumn(
    backgroundColor: Color,
    waveformType: WaveformType,
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    cycles: Int = 2
) {

    val transition = rememberInfiniteTransition(label = "oscillator")

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
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()

                        tryAwaitRelease()

                        onRelease()
                    }
                )
            }
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
        // val cycles = 2
        // val cycleWidth = size.width / cycles
        var path: Path

        when (waveformType) {
            WaveformType.SQUARE -> {
                path = buildSquarePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles,
                    phase = if (isActive) phase else 0f
                )
            }

            WaveformType.TRIANGLE -> {
                path = buildTrianglePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles,
                    phase = if (isActive) phase else 0f
                )
            }

            WaveformType.SAWTOOTH -> {
                path = buildSawtoothPath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles,
                    phase = if (isActive) phase else 0f
                )
            }

            WaveformType.SINE -> {
                path = buildSinePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles.toFloat(),
                    phase = if (isActive) phase else 0f
                )
            }
        }

        clipRect {
            drawPath(
                path = path,
                color = Color(0xFF1E1E1E),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}