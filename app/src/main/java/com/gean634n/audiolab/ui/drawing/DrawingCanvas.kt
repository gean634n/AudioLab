package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.ui.input.pointer.pointerInput
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.Stroke
import com.gean634n.audiolab.drawing.StrokePoint
import com.gean634n.audiolab.drawing.LineStyle

@Composable
fun DrawingCanvas(
    strokes: List<Stroke>,
    selectedTool: DrawingTool,
    selectedLineStyle: LineStyle,
    selectedColor: DrawingColor,
    onStrokeFinished: (Stroke) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPoints by remember {
        mutableStateOf<List<StrokePoint>>(emptyList())
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(
                selectedTool,
                selectedLineStyle,
                selectedColor
            )  {
                awaitEachGesture {
                    val down = awaitFirstDown()

                    val points = mutableListOf<StrokePoint>()

                    fun addPoint(position: Offset) {
                        val normalizedX =
                            (position.x / size.width)
                                .coerceIn(0f, 1f)

                        val normalizedY =
                            (position.y / size.height)
                                .coerceIn(0f, 1f)

                        val point = StrokePoint(
                            x = normalizedX,
                            y = normalizedY,
                            timeMillis = System.currentTimeMillis()
                        )

                        points += point

                        currentPoints = points.toList()
                    }

                    addPoint(down.position)

                    var change = down

                    while (change.pressed) {
                        val event = awaitPointerEvent()
                        change = event.changes.first()

                        if (change.pressed) {
                            addPoint(change.position)
                        }
                    }

                    if (points.isNotEmpty()) {
                        onStrokeFinished(
                            Stroke(
                                points = points,
                                tool = selectedTool,
                                lineStyle = selectedLineStyle,
                                color = selectedColor
                            )
                        )
                    }

                    currentPoints = emptyList()
                }
            }
    ) {
        /*
         * Converte os pontos normalizados do Stroke
         * novamente para coordenadas do Canvas.
         */
        fun drawPoints(
            points: List<StrokePoint>,
            tool: DrawingTool,
            lineStyle: LineStyle,
            drawingColor: DrawingColor
        ) {
            if (points.isEmpty()) return

            val path = Path()

            val first = points.first()

            path.moveTo(
                first.x * size.width,
                first.y * size.height
            )

            points.drop(1).forEach { point ->
                path.lineTo(
                    point.x * size.width,
                    point.y * size.height
                )
            }

            val strokeWidth = when (tool) {
                DrawingTool.PENCIL -> 3f
                DrawingTool.MARKER -> 8f
                DrawingTool.NIB -> 5f
            }

            val pathEffect = when (lineStyle) {
                LineStyle.SOLID -> null

                LineStyle.DASHED ->
                    PathEffect.dashPathEffect(
                        intervals = floatArrayOf(20f, 12f)
                    )

                LineStyle.DOTTED ->
                    PathEffect.dashPathEffect(
                        intervals = floatArrayOf(2f, 12f)
                    )
            }

            val color = when (drawingColor) {
                DrawingColor.BLACK -> Color(0xFF1E1E1E)
                DrawingColor.BLUE -> Color(0xFF4A90E2)
                DrawingColor.RED -> Color(0xFFE85D5D)
                DrawingColor.YELLOW -> Color(0xFFF2C94C)
                DrawingColor.GREEN -> Color(0xFF6FCF97)
            }

            drawPath(
                path = path,
                color = color,
                style = DrawStroke(
                    width = strokeWidth,
                    pathEffect = pathEffect
                )
            )
        }

        // Traços já concluídos.
        strokes.forEach { stroke ->
            drawPoints(
                points = stroke.points,
                tool = stroke.tool,
                lineStyle = stroke.lineStyle,
                drawingColor = stroke.color
            )
        }

        // Traço que está sendo desenhado neste momento.
        drawPoints(
            points = currentPoints,
            tool = selectedTool,
            lineStyle = selectedLineStyle,
            drawingColor = selectedColor
        )
    }
}