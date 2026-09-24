package com.gean634n.audiolab.drawing

import org.junit.Assert.assertEquals
import org.junit.Test

class DrawingPlaybackEmitterTest {
    private class RecordingSink : DrawingPlaybackSink by NoopDrawingPlaybackSink {
        val events = mutableListOf<String>()
        val points = mutableListOf<StrokePoint>()
        override fun strokeStarted(voice: Int, stroke: Stroke) {
            events += "start:$voice:${stroke.id}"
        }
        override fun pointReached(
            voice: Int,
            strokeId: Int,
            point: StrokePoint,
            relativeTimeMillis: Long
        ) {
            events += "point:$voice:$strokeId:$relativeTimeMillis"
            points += point
        }
        override fun strokeEnded(voice: Int, strokeId: Int) {
            events += "end:$voice:$strokeId"
        }
    }

    private fun stroke(id: Int, x: Float = 0f, vararg times: Long) = Stroke(
        id = id,
        points = times.map { StrokePoint(x, 0.5f, it) },
        tool = DrawingTool.PENCIL,
        lineStyle = LineStyle.SOLID,
        color = DrawingColor.BLACK
    )

    private fun emitter(strokes: List<Stroke>, sink: RecordingSink, voices: Int = 8) =
        DrawingPlaybackEmitter(strokes, DrawingPlaybackController(1000L), sink, voices)

    @Test
    fun simpleStrokeUsesTriggerAndRelativeTimingWithoutDuplicates() {
        val sink = RecordingSink()
        val stroke = stroke(1, 0.5f, 100L, 200L)
        val emitter = emitter(listOf(stroke), sink)
        emitter.advanceTo(499L)
        assertEquals(emptyList<String>(), sink.events)
        emitter.advanceTo(500L)
        emitter.advanceTo(599L)
        assertEquals(listOf("start:0:1", "point:0:1:0"), sink.events)
        emitter.advanceTo(600L)
        emitter.advanceTo(1000L)
        assertEquals(listOf("start:0:1", "point:0:1:0", "point:0:1:100", "end:0:1"), sink.events)
        assertEquals(stroke.points, sink.points)
    }

    @Test
    fun overlappingStrokesUseDistinctVoicesAndReuseLowestFreeSlot() {
        val sink = RecordingSink()
        val emitter = emitter(listOf(
            stroke(1, 0f, 0L, 100L),
            stroke(2, 0f, 0L, 500L),
            stroke(3, 0.2f, 0L, 100L)
        ), sink)
        emitter.advanceTo(0L)
        emitter.advanceTo(100L)
        emitter.advanceTo(200L)
        assertEquals(listOf(
            "start:0:1", "point:0:1:0", "start:1:2", "point:1:2:0",
            "point:0:1:100", "end:0:1", "start:0:3", "point:0:3:0"
        ), sink.events)
    }

    @Test
    fun emptyStrokesAndEmptyDrawingEmitNothing() {
        val sink = RecordingSink()
        emitter(listOf(stroke(1)), sink).apply {
            advanceTo(1000L)
            endAllActive()
        }
        emitter(emptyList(), sink).advanceTo(1000L)
        assertEquals(emptyList<String>(), sink.events)
    }

    @Test
    fun singlePointStartsEmitsAndEndsInSameTick() {
        val sink = RecordingSink()
        emitter(listOf(stroke(1, 0f, 42L)), sink).advanceTo(0L)
        assertEquals(listOf("start:0:1", "point:0:1:0", "end:0:1"), sink.events)
    }

    @Test
    fun timeJumpEmitsEveryDuePointInOrder() {
        val sink = RecordingSink()
        val emitter = emitter(listOf(stroke(1, 0f, 50L, 100L, 100L, 300L)), sink)
        emitter.advanceTo(1000L)
        emitter.advanceTo(2000L)
        assertEquals(listOf(
            "start:0:1", "point:0:1:0", "point:0:1:50", "point:0:1:50",
            "point:0:1:250", "end:0:1"
        ), sink.events)
    }

    @Test
    fun eightOverlappingStrokesUseAllEightVoices() {
        val sink = RecordingSink()
        val emitter = emitter((1..8).map { stroke(it, 0f, 0L, 1000L) }, sink)
        emitter.advanceTo(0L)
        assertEquals((1..8).flatMap { listOf("start:${it - 1}:$it", "point:${it - 1}:$it:0") }, sink.events)
        emitter.endAllActive()
        emitter.endAllActive()
        emitter.advanceTo(1000L)
        assertEquals((1..8).map { "end:${it - 1}:$it" }, sink.events.drop(16))
    }

    @Test
    fun ninthStrokeEndsOldestBeforeReusingItsVoice() {
        val sink = RecordingSink()
        val emitter = emitter((1..9).map { stroke(it, 0f, 0L, 1000L) }, sink)
        emitter.advanceTo(0L)
        assertEquals(listOf("end:0:1", "start:0:9", "point:0:9:0"), sink.events.drop(16))
        emitter.advanceTo(1000L)
        assertEquals((2..9).flatMap {
            val voice = if (it == 9) 0 else it - 1
            listOf("point:$voice:$it:1000", "end:$voice:$it")
        }, sink.events.drop(19))
    }

    @Test
    fun stealingUsesActivationOrderAndHonorsConfiguredLimit() {
        val sink = RecordingSink()
        val emitter = emitter(listOf(
            stroke(1, 0.5f, 0L, 1000L),
            stroke(2, 0f, 0L, 1000L),
            stroke(3, 0.6f, 0L, 1000L)
        ), sink, voices = 2)
        emitter.advanceTo(0L)
        emitter.advanceTo(500L)
        emitter.advanceTo(600L)
        assertEquals(listOf(
            "start:0:2", "point:0:2:0", "start:1:1", "point:1:1:0",
            "end:0:2", "start:0:3", "point:0:3:0"
        ), sink.events)
    }

    @Test
    fun snapshotSurvivesChangesToOriginalLists() {
        val sink = RecordingSink()
        val points = mutableListOf(StrokePoint(0f, 0.5f, 10L))
        val strokes = mutableListOf(stroke(1).copy(points = points))
        val emitter = emitter(strokes, sink)
        points.clear()
        strokes.clear()
        emitter.advanceTo(0L)
        assertEquals(listOf("start:0:1", "point:0:1:0", "end:0:1"), sink.events)
    }
}
