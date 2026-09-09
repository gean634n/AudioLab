package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun DrawingScreen(
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = viewModel()
) {
    val playingStrokeIndex = viewModel.state.playingStrokeIndex
    var playbackPointCount by remember { mutableIntStateOf(0) }

    val isPaused = viewModel.state.isPaused

    LaunchedEffect(
        playingStrokeIndex,
        isPaused
    ) {
        if (playingStrokeIndex == null) {
            playbackPointCount = 0
            return@LaunchedEffect
        }

        if (isPaused) {
            return@LaunchedEffect
        }

        val stroke = viewModel.state.strokes[playingStrokeIndex]
        val points = stroke.points

        if (playbackPointCount == 0 && points.isNotEmpty()) {
            playbackPointCount = 1
        }

        for (i in playbackPointCount until points.size) {
            val delayMillis =
                points[i].timeMillis - points[i - 1].timeMillis

            delay(delayMillis.coerceAtLeast(0L))

            playbackPointCount = i + 1
        }

        viewModel.playNextStroke()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        DrawingCanvas(
            strokes = viewModel.state.strokes,
            onStrokeFinished = viewModel::addStroke,
            selectedTool = viewModel.state.selectedTool,
            selectedLineStyle = viewModel.state.selectedLineStyle,
            selectedColor = viewModel.state.selectedColor,
            playingStrokeIndex = viewModel.state.playingStrokeIndex,
            playbackPointCount = playbackPointCount,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (viewModel.state.isPaused) {
                        viewModel.resume()
                    } else {
                        viewModel.play()
                    }
                },
                enabled = viewModel.state.strokes.isNotEmpty()
            ) {
                Text(
                    if (viewModel.state.isPaused) {
                        "Continuar"
                    } else {
                        "Play"
                    }
                )
            }

            Button(
                onClick = { viewModel.pause() },
                enabled =
                    viewModel.state.playingStrokeIndex != null &&
                            !viewModel.state.isPaused
            ) {
                Text("Pause")
            }

            Button(
                onClick = { viewModel.stop() },
                enabled = viewModel.state.playingStrokeIndex != null
            ) {
                Text("Stop")
            }
        }

        viewModel.lastStrokeMetrics?.let { metrics ->
            Text(
                text = """
            Tool: ${viewModel.state.selectedTool}
            Linha: ${viewModel.state.selectedLineStyle}
            Cor: ${viewModel.state.selectedColor}
            Duração: ${metrics.durationMillis} ms
            Velocidade: %.2f
            X médio: %.2f
            Y médio: %.2f
            Direção: ${metrics.direction}
        """.trimIndent().format(
                    metrics.averageSpeed,
                    metrics.averageX,
                    metrics.averageY
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = viewModel::undo,
                enabled = viewModel.canUndo
            ) {
                Text("↶")
            }

            IconButton(
                onClick = viewModel::redo,
                enabled = viewModel.canRedo
            ) {
                Text("↷")
            }

            IconButton(
                onClick = viewModel::clear,
                enabled = viewModel.state.strokes.isNotEmpty()
            ) {
                Text("✕")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 112.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrawingColor.entries.forEach { drawingColor ->
                val selected = drawingColor == viewModel.state.selectedColor

                Button(
                    onClick = {
                        viewModel.selectColor(drawingColor)
                    }
                ) {
                    Text(
                        when (drawingColor) {
                            DrawingColor.BLACK ->
                                if (selected) "✓ Preto" else "Preto"

                            DrawingColor.BLUE ->
                                if (selected) "✓ Azul" else "Azul"

                            DrawingColor.RED ->
                                if (selected) "✓ Vermelho" else "Vermelho"

                            DrawingColor.YELLOW ->
                                if (selected) "✓ Amarelo" else "Amarelo"

                            DrawingColor.GREEN ->
                                if (selected) "✓ Verde" else "Verde"
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrawingTool.entries.forEach { tool ->
                val selected = tool == viewModel.state.selectedTool

                Button(
                    onClick = { viewModel.selectTool(tool) }
                ) {
                    Text(
                        when (tool) {
                            DrawingTool.PENCIL -> if (selected) "✓ Lápis" else "Lápis"
                            DrawingTool.MARKER -> if (selected) "✓ Canetinha" else "Canetinha"
                            DrawingTool.NIB -> if (selected) "✓ Bico de pena" else "Bico de pena"
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LineStyle.entries.forEach { lineStyle ->
                val selected = lineStyle == viewModel.state.selectedLineStyle

                Button(
                    onClick = { viewModel.selectLineStyle(lineStyle) }
                ) {
                    Text(
                        when (lineStyle) {
                            LineStyle.SOLID ->
                                if (selected) "✓ Contínuo" else "Contínuo"

                            LineStyle.DASHED ->
                                if (selected) "✓ Tracejado" else "Tracejado"

                            LineStyle.DOTTED ->
                                if (selected) "✓ Pontilhado" else "Pontilhado"
                        }
                    )
                }
            }
        }
    }
}