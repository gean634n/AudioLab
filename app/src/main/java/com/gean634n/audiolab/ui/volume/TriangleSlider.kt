package com.gean634n.audiolab.ui.volume

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import com.gean634n.audiolab.ui.theme.BlueColor
import com.gean634n.audiolab.ui.theme.OutlineColor

@Composable
fun TriangleSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    val progress = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    fun xToLevelDb(x: Float, width: Float): Float {
        val progress = (x / width).coerceIn(0f, 1f)
        return valueRange.start + progress * (valueRange.endInclusive - valueRange.start)
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onValueChange( xToLevelDb(offset.x, size.width.toFloat()))
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    onValueChange(xToLevelDb(change.position.x, size.width.toFloat()))
                    change.consume()
                }
            }
    ) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, size.height)
            lineTo(size.width, 0f)
            close()
        }

        drawPath(
            path = path,
            color = Color.White
        )

        clipPath(path) {
            drawRect(
                color = BlueColor,
                size = Size(
                    width = size.width * progress,
                    height = size.height
                )
            )
        }

        drawPath(
            path = path,
            color = OutlineColor,
            style = Stroke(width = 2.dp.toPx())
        )

        val x = size.width * progress
        val y = size.height * (1f - progress)

        drawLine(
            color = OutlineColor,
            start = Offset(x, size.height),
            end = Offset(x, y),
            strokeWidth = 4f
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TriangleSliderPreview() {
    TriangleSlider(
        value = -12f,
        valueRange = -60f..0f,
        onValueChange = {},
        modifier = Modifier.size(
            width = 300.dp,
            height = 120.dp
        )
    )
}