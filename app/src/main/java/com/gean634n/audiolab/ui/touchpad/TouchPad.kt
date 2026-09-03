package com.gean634n.audiolab.ui.touchpad

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TouchPad(
    position: Offset,
    onPositionChange: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
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
        val x = size.width * position.x
        val y = size.height * position.y

        drawRect(
            color = Color(0xFFFFE89A)
        )

        drawCircle(
            color = Color(0xFFFF4D4D),
            radius = 18f,
            center = Offset(x, y)
        )
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