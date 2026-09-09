package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun SketchButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.35f)
            .then(
                Modifier
                    .border(
                        border = BorderStroke(
                            width = 2.dp,
                            color = Color(0xFF1E1E1E)
                        ),
                        shape = RectangleShape
                    )
                    .clickable(
                        enabled = enabled,
                        onClick = onClick
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF1E1E1E)
        )
    }
}