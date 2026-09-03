package com.gean634n.audiolab.ui.touchpad

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.ui.theme.OutlineColor
import com.gean634n.audiolab.ui.theme.YellowColor

@Composable
fun TouchPad(
    position: Offset,
    onPositionChange: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxTilt = -12f

    val normalizedX = (position.x - 0.5f) * 2f
    val normalizedY = (position.y - 0.5f) * 2f

    val tiltX = normalizedY * maxTilt
    val tiltY = -normalizedX * maxTilt

    // TODO: Improve touch mapping so the ball follows the finger projection on the tilted table.
    Box(
        modifier = modifier .pointerInput(Unit) {
            detectTapGestures { offset ->
                onPositionChange(
                    Offset(
                        x = (offset.x / size.width).coerceIn(0f, 1f),
                        y = (offset.y / size.height).coerceIn(0f, 1f)
                    )
                )
            }
        }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    onPositionChange(
                        Offset(
                            x = (change.position.x / size.width).coerceIn(0f, 1f),
                            y = (change.position.y / size.height).coerceIn(0f, 1f)
                        )
                    )

                    change.consume()
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin.Center
                    rotationX = tiltX
                    rotationY = tiltY
                    cameraDistance = size.height * 2f
                }
        ) {

            val x = size.width * position.x
            val y = size.height * position.y
            val strokeWidth = 3.dp.toPx()
            val ballRadius = 14.dp.toPx()

            drawRect(
                color = YellowColor,
            )

            drawRect(
                color = OutlineColor,
                style = Stroke(strokeWidth)
            )

            drawCircle(
                color = Color(0xFFFF4D4D),
                radius = ballRadius,
                center = Offset(x, y)
            )

            drawCircle(
                color = OutlineColor,
                radius = ballRadius,
                center = Offset(x, y),
                style = Stroke(strokeWidth)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TouchPadPreview() {
    var position by remember { mutableStateOf(Offset(0.8f, 0.2f)) }

    TouchPad(
        position = position,
        onPositionChange = { position = it },
        modifier = Modifier.size(
            width = 500.dp,
            height = 300.dp
        )
    )
}