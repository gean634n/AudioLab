package com.gean634n.audiolab.ui.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.HistoryEdu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (selected) {
                    Modifier
                        .background(Color(0xFFE3F2FD))
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF1E1E1E),
                            shape = RoundedCornerShape(12.dp)
                        )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
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
