package com.gean634n.audiolab.ui.drawing

import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle
import com.gean634n.audiolab.drawing.PlaybackAnimationMode
import com.gean634n.audiolab.drawing.Stroke

data class DrawingState(
    val strokes: List<Stroke> = emptyList(),
    val undoneStrokes: List<Stroke> = emptyList(),

    val selectedTool: DrawingTool = DrawingTool.PENCIL,
    val selectedLineStyle: LineStyle = LineStyle.SOLID,
    val selectedColor: DrawingColor = DrawingColor.BLACK,
    val playbackAnimationMode: PlaybackAnimationMode =
        PlaybackAnimationMode.BLINK_REPLAY_TIMING,

    // val playingStrokeIndex: Int? = null,
    val isPlaying: Boolean = false,

    val isPaused: Boolean = false
)