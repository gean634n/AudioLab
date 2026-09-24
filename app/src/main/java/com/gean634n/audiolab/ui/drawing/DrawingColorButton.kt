package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.drawing.DrawingColor
import androidx.compose.ui.graphics.drawscope.Stroke
@Composable
fun DrawingColorButton(
    drawingColor: DrawingColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val color = drawingColor.toComposeColor()

    SelectableSwatch(
        selected = false,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.size(32.dp)) {
            if (selected) {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.5f,
                    style = Stroke(
                        width = 1.5.dp.toPx()
                    )
                )
            }

            drawCircle(
                color = color,
                radius = size.minDimension * 0.4f
            )
        }
    }
}