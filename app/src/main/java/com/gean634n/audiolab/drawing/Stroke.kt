package com.gean634n.audiolab.drawing

data class StrokePoint(
    val x: Float,
    val y: Float,
    val timeMillis : Long
)

data class Stroke(
    val points: List<StrokePoint>,
    val tool: DrawingTool,
    val lineStyle: LineStyle,
    val color: DrawingColor
)