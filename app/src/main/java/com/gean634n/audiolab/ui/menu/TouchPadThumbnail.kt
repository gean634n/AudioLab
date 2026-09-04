package com.gean634n.audiolab.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.theme.OutlineColor
import com.gean634n.audiolab.ui.theme.YellowColor

@Composable
fun TouchPadThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                transformOrigin = TransformOrigin.Center
                rotationX = 8f
                rotationY = 5f
                cameraDistance = 12f
            }
    ) {
        val strokeWidth = 2.dp.toPx()

        // Table
        drawRect(
            color = YellowColor
        )

        drawRect(
            color = OutlineColor,
            style = Stroke(width = strokeWidth)
        )

        // Ball
        val ballRadius = 10.dp.toPx()
        val ballCenter = Offset(
            x = size.width * (4f / 5f),
            y = size.height * (1f / 3f)
        )

        drawCircle(
            color = Color(0xFFFF4D4D),
            radius = ballRadius,
            center = ballCenter
        )

        drawCircle(
            color = OutlineColor,
            radius = ballRadius,
            center = ballCenter,
            style = Stroke(width = strokeWidth)
        )
    }
}