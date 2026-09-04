package com.gean634n.audiolab.ui.oscillators

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.waveform.WaveformType
import com.gean634n.audiolab.ui.waveform.buildSawtoothPath
import com.gean634n.audiolab.ui.waveform.buildSinePath
import com.gean634n.audiolab.ui.waveform.buildSquarePath
import com.gean634n.audiolab.ui.waveform.buildTrianglePath

@Composable
fun OscillatorColumn(
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
        var path = Path()

        when (waveformType) {
            WaveformType.SQUARE -> {
                path = buildSquarePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles
                )
            }

            WaveformType.TRIANGLE -> {
                path = buildTrianglePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles
                )
            }

            WaveformType.SAWTOOTH -> {
                path = buildSawtoothPath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles
                )
            }

            WaveformType.SINE -> {
                path = buildSinePath(
                    width = size.width,
                    centerY = centerY,
                    amplitude = amplitude,
                    cycles = cycles.toFloat()
                )
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF1E1E1E),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}