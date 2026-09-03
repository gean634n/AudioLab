package com.gean634n.audiolab.ui.touchpad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gean634n.audiolab.audio.AudioEngine

@Composable
fun TouchPadScreen(
    audioEngine: AudioEngine,
    modifier: Modifier = Modifier,
    viewModel: TouchPadViewModel = viewModel(
        factory = TouchPadViewModelFactory(audioEngine)
    )
) {
    TouchPadContent(
        position = viewModel.position,
        frequencyHz = viewModel.frequencyHz,
        onPositionChange = viewModel::onPositionChange,
        modifier = modifier
    )
}

@Composable
fun TouchPadContent(
    position: Offset,
    frequencyHz: Float,
    onPositionChange: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier) {
        TouchPad(
            position = position,
            onPositionChange = onPositionChange,
            modifier = modifier.fillMaxSize().padding(20.dp, 50.dp)
        )

        Text(
            text = "${frequencyHz.toInt()} Hz",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(50.dp, 70.dp)
        )
    }
}