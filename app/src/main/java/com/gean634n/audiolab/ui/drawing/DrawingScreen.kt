package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle

@Composable
fun DrawingScreen(
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = viewModel()
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        DrawingCanvas(
            strokes = viewModel.state.strokes,
            onStrokeFinished = viewModel::addStroke,
            selectedTool = viewModel.state.selectedTool,
            selectedLineStyle = viewModel.state.selectedLineStyle,
            selectedColor = viewModel.state.selectedColor,
            modifier = Modifier.fillMaxSize()
        )

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
                    .align(Alignment.TopStart)
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