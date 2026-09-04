package com.gean634n.audiolab.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class MenuItem(
    val title: String,
    val onClick: () -> Unit,
    val preview: @Composable () -> Unit
)

@Composable
fun MenuScreen(
    onOpenVolume: () -> Unit,
    onOpenTouchPad: () -> Unit,
    onOpenWaveforms: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        MenuItem(
            title = "Intensidade (dB)",
            onClick = onOpenVolume,
            preview = {
                VolumeThumbnail()
            }
        ),
        MenuItem(
            title = "TouchPad",
            onClick = onOpenTouchPad,
            preview = {
               TouchPadThumbnail()
            }
        ),
        MenuItem(
            title = "Waveforms",
            onClick = onOpenWaveforms,
            preview = {}
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 350.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            ExperienceMenuItem(
                title = item.title,
                onClick = item.onClick,
                preview = item.preview
            )
        }
    }
}