package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.drawing.DrawingColor

@Composable
fun DrawingColorButton(
    drawingColor: DrawingColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = when (drawingColor) {
        DrawingColor.BLACK -> Color(0xFF1E1E1E)
        DrawingColor.BLUE -> Color(0xFFC4E2FF)
        DrawingColor.RED -> Color(0xFFE85D5D)
        DrawingColor.YELLOW -> Color(0xFFFFF4CC)
        DrawingColor.GREEN -> Color(0xFFD4EDDA)
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(32.dp)) {
            drawCircle(
                color = color,
                radius = size.minDimension * 0.4f
            )

            if (selected) {
                drawCircle(
                    color = Color(0xFF1E1E1E),
                    radius = size.minDimension * 0.5f,
                    style = Stroke(
                        width = 1.5.dp.toPx()
                    )
                )
            }
        }
    }
}
