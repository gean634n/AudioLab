package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle
import kotlinx.coroutines.delay

@Composable
fun DrawingScreen(
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = viewModel()
) {
    val playingStrokeIndex = viewModel.state.playingStrokeIndex
    val isPaused = viewModel.state.isPaused

    var playbackPointCount by remember {
        mutableIntStateOf(0)
    }

    var playbackStrokeIndex by remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(playingStrokeIndex, isPaused) {
        if (playingStrokeIndex == null) {
            playbackPointCount = 0
            playbackStrokeIndex = null
            return@LaunchedEffect
        }

        if (playbackStrokeIndex != playingStrokeIndex) {
            playbackPointCount = 0
            playbackStrokeIndex = playingStrokeIndex
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE9ECEF))
            .padding(16.dp)
    ) {

        // ------------------------------------------------
        // Top Bar
        // ------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            
            // Playback Group
            Row(
                modifier = Modifier
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SketchButton(
                    onClick = {
                        if (viewModel.state.isPaused) viewModel.resume() else viewModel.play()
                    },
                    backgroundColor = Color(0xFF6FCF97),
                    enabled = viewModel.state.strokes.isNotEmpty() && (playingStrokeIndex == null || isPaused)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }

                SketchButton(
                    onClick = viewModel::pause,
                    backgroundColor = Color(0xFFF2C94C),
                    enabled = playingStrokeIndex != null && !isPaused
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = "Pause",
                        tint = Color.White
                    )
                }

                SketchButton(
                    onClick = viewModel::stop,
                    backgroundColor = Color(0xFFE85D5D),
                    enabled = playingStrokeIndex != null
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stop,
                        contentDescription = "Stop",
                        tint = Color.White
                    )
                }
            }

            // Actions Group
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SketchButton(onClick = viewModel::undo, enabled = viewModel.canUndo) {
                    Icon(Icons.AutoMirrored.Rounded.Undo, "Undo")
                }
                SketchButton(onClick = viewModel::redo, enabled = viewModel.canRedo) {
                    Icon(Icons.AutoMirrored.Rounded.Redo, "Redo")
                }
                SketchButton(onClick = viewModel::clear, enabled = viewModel.state.strokes.isNotEmpty()) {
                    Icon(Icons.Rounded.DeleteOutline, "Clear")
                }
                SketchButton(onClick = { /* TODO: Download */ }) {
                    Icon(Icons.Rounded.FileDownload, "Save")
                }
            }
        }

        // ------------------------------------------------
        // Body
        // ------------------------------------------------
        Row(modifier = Modifier.fillMaxSize()) {
            
            // Sidebar
            Column(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tools Section
                SidebarSection {
                    DrawingTool.entries.forEach { tool ->
                        DrawingToolButton(
                            tool = tool,
                            selected = tool == viewModel.state.selectedTool,
                            onClick = { viewModel.selectTool(tool) }
                        )
                    }
                }

                // Styles Section
                SidebarSection {
                    LineStyle.entries.forEach { style ->
                        LineStyleButton(
                            lineStyle = style,
                            selected = style == viewModel.state.selectedLineStyle,
                            onClick = { viewModel.selectLineStyle(style) }
                        )
                    }
                }

                // Colors Section
                SidebarSection {
                    DrawingColor.entries.forEach { color ->
                        DrawingColorButton(
                            drawingColor = color,
                            selected = color == viewModel.state.selectedColor,
                            onClick = { viewModel.selectColor(color) }
                        )
                    }
                }
            }

            // Canvas Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(
                        width = 2.dp,
                        color = Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(Color.White)
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

                viewModel.state.strokes.lastOrNull()?.let { stroke ->
                    viewModel.lastStrokeMetrics?.let { metrics ->
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.9f),
                                    // shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF1E1E1E),
                                    // shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = """
                                    Ferramenta: ${stroke.tool}
                                    Linha: ${stroke.lineStyle}
                                    Cor: ${stroke.color}
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
                                fontSize = 12.sp,
                                lineHeight = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}
