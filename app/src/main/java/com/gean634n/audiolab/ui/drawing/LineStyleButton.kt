package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.drawing.LineStyle

@Composable
fun LineStyleButton(
    lineStyle: LineStyle,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SelectableSwatch(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        val pathEffect = lineStyle.toPathEffect(previewScale = 0.5f)

        Canvas(modifier = Modifier.size(32.dp)) {
            drawLine(
                color = Color(0xFF1E1E1E),
                start = center.copy(x = size.width * 0.1f),
                end = center.copy(x = size.width * 0.9f),
                strokeWidth = 2.5.dp.toPx(),
                pathEffect = pathEffect
            )
        }
    }
}