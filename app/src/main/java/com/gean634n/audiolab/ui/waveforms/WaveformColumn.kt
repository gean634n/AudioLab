package com.gean634n.audiolab.ui.waveforms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun WaveformColumn(
    backgroundColor: Color,
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
    }
}