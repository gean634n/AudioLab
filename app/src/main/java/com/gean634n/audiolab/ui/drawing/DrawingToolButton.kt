package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.HistoryEdu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.drawing.DrawingTool

@Composable
fun DrawingToolButton(
    tool: DrawingTool,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SelectableSwatch(
        selected = selected,
        onClick = onClick,
        modifier = modifier
    ) {
        val icon = when (tool) {
            DrawingTool.PENCIL -> Icons.Rounded.Create
            DrawingTool.MARKER -> Icons.Rounded.Edit
            DrawingTool.NIB -> Icons.Rounded.HistoryEdu
        }

        Icon(
            imageVector = icon,
            contentDescription = tool.name,
            tint = Color(0xFF1E1E1E),
            modifier = Modifier.size(24.dp)
        )
    }
}
