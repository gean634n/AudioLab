package com.gean634n.audiolab.ui.oscillators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.waveform.WaveformType

@Composable
fun OscillatorScreen(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp, 5.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OscillatorColumn(
            backgroundColor = Color(0xFFA8D8A8),
            waveformType = WaveformType.SQUARE,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        OscillatorColumn(
            backgroundColor = Color(0xFFFFE89A),
            waveformType = WaveformType.TRIANGLE,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        OscillatorColumn(
            backgroundColor = Color(0xFFFFB3C1),
            waveformType = WaveformType.SAWTOOTH,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        OscillatorColumn(
            backgroundColor = Color(0xFFA5D8FF),
            waveformType = WaveformType.SINE,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OscillatorScreenPreview() {
    OscillatorScreen()
}