package com.gean634n.audiolab.ui.drawing

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.LineStyle
import com.gean634n.audiolab.ui.theme.DrawingInkBlack
import com.gean634n.audiolab.ui.theme.DrawingInkBlue
import com.gean634n.audiolab.ui.theme.DrawingInkGreen
import com.gean634n.audiolab.ui.theme.DrawingInkRed
import com.gean634n.audiolab.ui.theme.DrawingInkYellow

fun DrawingColor.toComposeColor(): Color = when (this) {
    DrawingColor.BLACK -> DrawingInkBlack
    DrawingColor.BLUE -> DrawingInkBlue
    DrawingColor.RED -> DrawingInkRed
    DrawingColor.YELLOW -> DrawingInkYellow
    DrawingColor.GREEN -> DrawingInkGreen
}

fun LineStyle.toPathEffect(previewScale: Float = 1f): PathEffect? = when (this) {
    LineStyle.SOLID -> null

    LineStyle.DASHED -> PathEffect.dashPathEffect(
        intervals = floatArrayOf(20f * previewScale, 12f * previewScale)
    )

    LineStyle.DOTTED -> PathEffect.dashPathEffect(
        intervals = floatArrayOf(2f * previewScale, 12f * previewScale)
    )
}