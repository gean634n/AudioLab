package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectableSwatch(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.4f)
            .size(48.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier
                        .background(Color(0xFFE3F2FD))
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF1E1E1E),
                            shape = shape
                        )
                } else {
                    Modifier
                }
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
        content = content
    )
}