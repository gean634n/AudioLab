package com.gean634n.audiolab.ui.volume

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.theme.AudioLabTheme

@Composable
fun VolumeScreen(
    audioEngine: AudioEngine,
    modifier: Modifier = Modifier,
    viewModel: VolumeViewModel = viewModel(
        factory = VolumeViewModelFactory(audioEngine)
    )
) {
    VolumeContent(
        levelDb = viewModel.levelDb,
        onLevelChange = viewModel::onLevelChange,
        modifier = modifier
    )
}

@Composable
fun VolumeContent(
    levelDb: Float,
    onLevelChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val screenPadding = maxWidth / 25
        val availableWidth = maxWidth - (screenPadding * 2)
        val availableHeight = maxHeight - (screenPadding)

        Text(
            text = String.format("%.1f dB", levelDb),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = screenPadding),
            fontSize = 12.sp,
        )



        TriangleSlider(
            value = levelDb,
            onValueChange = onLevelChange,
            valueRange = MIN_LEVEL_DB..MAX_LEVEL_DB,
            modifier = Modifier
                .padding(screenPadding, screenPadding * 2)
                .size(
                    width = availableWidth,
                    height = availableHeight / 2
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    AudioLabTheme {
        VolumeContent(
            levelDb = -12f,
            onLevelChange = {},
            modifier = Modifier.size(
                width = 300.dp,
                height = 120.dp
            )
        )
    }
}