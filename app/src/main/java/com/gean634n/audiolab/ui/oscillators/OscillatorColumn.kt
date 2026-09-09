package com.gean634n.audiolab.ui.oscillators

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.waveform.WaveformType
import com.gean634n.audiolab.ui.waveform.buildSawtoothPath
import com.gean634n.audiolab.ui.waveform.buildSinePath
import com.gean634n.audiolab.ui.waveform.buildSquarePath
import com.gean634n.audiolab.ui.waveform.buildTrianglePath
import kotlin.math.PI
import kotlin.math.ln

@Composable
fun OscillatorColumn(
    backgroundColor: Color,
    waveformType: WaveformType,
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onPositionChange: (Float) -> Unit,
    frequencyHz: Float,
    cycles: Int = 2
) {

    val pitchNotation = frequencyToPitchNotation(frequencyHz)

    var phase by remember { mutableFloatStateOf(0f) }

    var visualFrequencyHz by remember { mutableFloatStateOf(frequencyHz) }

    val currentFrequencyHz by rememberUpdatedState(visualFrequencyHz)

    LaunchedEffect(isActive) {
        if (!isActive) {
            phase = 0f
            return@LaunchedEffect
        }

        var previousFrameTime = 0L

        while (true) {
            withFrameNanos { frameTime ->
                if (previousFrameTime != 0L) {
                    val deltaSeconds = (frameTime - previousFrameTime) / 1_000_000_000f

                    val visualSpeed = frequencyToMotionRate(currentFrequencyHz)

                    val angularVelocity = 2f * PI.toFloat() * visualSpeed

                    phase += angularVelocity * deltaSeconds
                    phase %= (2f * PI.toFloat())
                }

                previousFrameTime = frameTime
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()

                        onPress()

                        val normalizedY = (down.position.y / size.height).coerceIn(0f, 1f)
                        visualFrequencyHz = yToFrequency(normalizedY)
                        onPositionChange(normalizedY)

                        var change = down

                        while (change.pressed) {
                            val event = awaitPointerEvent()
                            change = event.changes.first()

                            if (change.pressed) {
                                val normalizedY = (change.position.y / size.height).coerceIn(0f, 1f)
                                visualFrequencyHz = yToFrequency(normalizedY)
                                onPositionChange(normalizedY)
                            }
                        }

                        onRelease()
                    }
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = if (pitchNotation.cents == 0) {
                    pitchNotation.note
                } else {
                    "%s %+.0f".format(
                        pitchNotation.note,
                        pitchNotation.cents.toFloat()
                    )
                }
            )

            Text(
                text = "%.1f Hz".format(frequencyHz),
                fontWeight = FontWeight.Bold
            )
        }
    }

}

private fun frequencyToMotionRate(frequencyHz: Float): Float {
    val minFrequency = 55f
    val maxFrequency = 880f

    val bottomRate = 0.55f
    val topRate = 8.8f

    val normalized =
        (
                ln(frequencyHz / minFrequency) /
                        ln(maxFrequency / minFrequency)
                ).coerceIn(0f, 1f)

    return bottomRate +
            normalized * (topRate - bottomRate)
}