package com.gean634n.audiolab.ui.drawing

import android.os.SystemClock
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
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.ui.input.pointer.pointerInput
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingPlaybackController
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.Stroke
import com.gean634n.audiolab.drawing.StrokePoint
import com.gean634n.audiolab.drawing.LineStyle
import com.gean634n.audiolab.drawing.PlaybackAnimationMode

@Composable
fun DrawingCanvas(
    strokes: List<Stroke>,
    selectedTool: DrawingTool,
    selectedLineStyle: LineStyle,
    selectedColor: DrawingColor,
    playheadX: Float,
    playbackElapsedMillis: Long,
    playbackController: DrawingPlaybackController,
    playbackAnimationMode: PlaybackAnimationMode,
    isPlaying: Boolean,
    onStrokeStarted: (
        DrawingTool,
        LineStyle,
        DrawingColor
    ) -> Unit,
    onPointAdded: (Float, Float, Long) -> Unit,
    onStrokeEnded: () -> Unit,
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
                    var strokeStartTime: Long? = null

                    onStrokeStarted(
                        selectedTool,
                        selectedLineStyle,
                        selectedColor
                    )

                    fun addPoint(position: Offset) {
                        val normalizedX =
                            (position.x / size.width).coerceIn(0f, 1f)

                        val normalizedY =
                            (position.y / size.height).coerceIn(0f, 1f)

                        val now = SystemClock.uptimeMillis()

                        if (strokeStartTime == null) {
                            strokeStartTime = now
                        }

                        val point = StrokePoint(
                            x = normalizedX,
                            y = normalizedY,
                            timeMillis = now
                        )

                        points += point
                        currentPoints = points.toList()

                        onPointAdded(
                            point.x,
                            point.y,
                            now - strokeStartTime!!
                        )
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

                    onStrokeEnded()

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

            drawPath(
                path = path,
                color = drawingColor.toComposeColor(),
                style = DrawStroke(
                    width = strokeWidth,
                    pathEffect = lineStyle.toPathEffect()
                )
            )
        }

        // Traços já concluídos.
        strokes.forEach { stroke ->
            val firstPoint = stroke.points.firstOrNull()
                ?: return@forEach

            val startX = firstPoint.x

            val strokeElapsedMillis = playbackController.strokeElapsedMillis(
                    startX = startX,
                    elapsedMillis = playbackElapsedMillis
                )

            val hasReachedStroke =
                playbackController.hasReached(
                    startX = startX,
                    elapsedMillis = playbackElapsedMillis
                )

            val blinkDurationMillis = 120L

            val pointsToDraw =
                if (!isPlaying) {
                    stroke.points
                } else {
                    when (playbackAnimationMode) {
                        PlaybackAnimationMode.HIDE_ALL_SHOW_FULL -> {
                            if (!hasReachedStroke) {
                                emptyList()
                            } else {
                                stroke.points
                            }
                        }

                        PlaybackAnimationMode.HIDE_ALL_REPLAY_TIMING -> {
                            if (!hasReachedStroke) {
                                emptyList()
                            } else {
                                stroke.points.takeWhile { point ->
                                    val pointElapsedMillis =
                                        point.timeMillis - firstPoint.timeMillis

                                    pointElapsedMillis <= strokeElapsedMillis
                                }
                            }
                        }

                        PlaybackAnimationMode.BLINK_FULL -> {
                            when {
                                !hasReachedStroke ->
                                    stroke.points

                                strokeElapsedMillis < blinkDurationMillis ->
                                    emptyList()

                                else ->
                                    stroke.points
                            }
                        }

                        PlaybackAnimationMode.BLINK_REPLAY_TIMING -> {
                            when {
                                !hasReachedStroke ->
                                    stroke.points

                                strokeElapsedMillis < blinkDurationMillis ->
                                    emptyList()

                                else -> {
                                    val replayElapsedMillis =
                                        strokeElapsedMillis - blinkDurationMillis

                                    stroke.points.takeWhile { point ->
                                        val pointElapsedMillis =
                                            point.timeMillis - firstPoint.timeMillis

                                        pointElapsedMillis <= replayElapsedMillis
                                    }
                                }
                            }
                        }
                    }
                }

            drawPoints(
                points = pointsToDraw,
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

        if (isPlaying) {
            val x = playheadX * size.width

            drawLine(
                color = Color.Red,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 3f
            )
        }
    }
}