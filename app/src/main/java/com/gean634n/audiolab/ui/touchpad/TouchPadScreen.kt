package com.gean634n.audiolab.ui.touchpad

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TouchPadScreen(
    modifier: Modifier = Modifier,
    viewModel: TouchPadViewModel = viewModel()
) {
    TouchPadContent(
        position = viewModel.position,
        onPositionChange = viewModel::onPositionChange,
        modifier = modifier
    )
}

@Composable
fun TouchPadContent(
    position: Offset,
    onPositionChange: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    TouchPad(
        position = position,
        onPositionChange = onPositionChange,
        modifier = modifier.fillMaxSize().padding(20.dp, 50.dp)
    )
}