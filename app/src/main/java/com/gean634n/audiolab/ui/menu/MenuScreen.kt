package com.gean634n.audiolab.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class MenuItem(
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun MenuScreen(
    onOpenVolume: () -> Unit,
    onOpenTouchPad: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        MenuItem(
            title = "Volume",
            onClick = onOpenVolume
        ),
        MenuItem(
            title = "TouchPad",
            onClick = onOpenTouchPad
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .clickable(onClick = item.onClick)
                    .padding(8.dp)
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}