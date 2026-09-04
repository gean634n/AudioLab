package com.gean634n.audiolab.ui.waveforms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WaveformsScreen(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        WaveformColumn(
            backgroundColor = Color(0xFFA8D8A8),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        WaveformColumn(
            backgroundColor = Color(0xFFFFE89A),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        WaveformColumn(
            backgroundColor = Color(0xFFFFB3C1),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        WaveformColumn(
            backgroundColor = Color(0xFFA5D8FF),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WaveformsScreenPreview() {
    WaveformsScreen()
}