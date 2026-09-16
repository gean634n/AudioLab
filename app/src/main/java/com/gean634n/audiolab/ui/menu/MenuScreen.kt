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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment

private data class MenuItem(
    val title: String,
    val onClick: () -> Unit,
    val preview: @Composable () -> Unit
)

@Composable
fun MenuScreen(
    onOpenVolume: () -> Unit,
    onOpenTouchPad: () -> Unit,
    onOpenOscillators: () -> Unit,
    onOpenDrawing: () -> Unit,
    onOpenSettings: () -> Unit,
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
            title = "Osciladores",
            onClick = onOpenOscillators,
            preview = {
                OscillatorsThumbnail()
            }
        ),
        MenuItem(
            title = "Drawing",
            onClick = onOpenDrawing,
            preview = {}
        )

    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 350.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding =  PaddingValues(
                start = 16.dp,
                top = 64.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
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

        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configurações"
            )
        }
    }
}