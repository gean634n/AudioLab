package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.drawing.LineStyle

@Composable
fun LineStyleButton(
    lineStyle: LineStyle,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (selected) {
                    Modifier
                        .background(Color(0xFFE3F2FD))
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF1E1E1E),
                            shape = RoundedCornerShape(12.dp)
                        )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val pathEffect = when (lineStyle) {
            LineStyle.SOLID -> null
            LineStyle.DASHED -> PathEffect.dashPathEffect(floatArrayOf(10f, 7f))
            LineStyle.DOTTED -> PathEffect.dashPathEffect(floatArrayOf(2f, 6f))
        }

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
