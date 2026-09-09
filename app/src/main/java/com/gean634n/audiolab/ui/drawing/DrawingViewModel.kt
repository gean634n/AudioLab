package com.gean634n.audiolab.ui.drawing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle
import com.gean634n.audiolab.drawing.Stroke

class DrawingViewModel : ViewModel() {

    var state by mutableStateOf(DrawingState())
        private set

    val canUndo: Boolean
        get() = state.strokes.isNotEmpty()

    val canRedo: Boolean
        get() = state.undoneStrokes.isNotEmpty()

    fun addStroke(stroke: Stroke) {
        state = state.copy(
            strokes = state.strokes + stroke,
            undoneStrokes = emptyList()
        )
    }

    fun undo() {
        if (state.strokes.isEmpty()) return

        val stroke = state.strokes.last()

        state = state.copy(
            strokes = state.strokes.dropLast(1),
            undoneStrokes = state.undoneStrokes + stroke
        )
    }

    fun redo() {
        if (state.undoneStrokes.isEmpty()) return

        val stroke = state.undoneStrokes.last()

        state = state.copy(
            strokes = state.strokes + stroke,
            undoneStrokes = state.undoneStrokes.dropLast(1)
        )
    }

    fun clear() {
        state = DrawingState()
    }

    fun selectTool(tool: DrawingTool) {
        state = state.copy(
            selectedTool = tool
        )
    }

    fun selectLineStyle(lineStyle: LineStyle) {
        state = state.copy(
            selectedLineStyle = lineStyle
        )
    }

    fun selectColor(color: DrawingColor) {
        state = state.copy(
            selectedColor = color
        )
    }
}