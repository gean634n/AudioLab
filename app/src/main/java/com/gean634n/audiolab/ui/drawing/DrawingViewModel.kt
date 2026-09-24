package com.gean634n.audiolab.ui.drawing

import android.os.SystemClock
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.viewModelScope
import com.gean634n.audiolab.drawing.DrawingPlaybackController
import com.gean634n.audiolab.drawing.DrawingPlaybackEmitter
import com.gean634n.audiolab.drawing.DrawingPlaybackSink
import com.gean634n.audiolab.drawing.NoopDrawingPlaybackSink
import com.gean634n.audiolab.drawing.StrokePoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.drawing.DrawingColor
import com.gean634n.audiolab.drawing.DrawingTool
import com.gean634n.audiolab.drawing.LineStyle
import com.gean634n.audiolab.drawing.PlaybackAnimationMode
import com.gean634n.audiolab.drawing.Stroke
import com.gean634n.audiolab.drawing.StrokeMetrics
import com.gean634n.audiolab.drawing.calculateMetrics

class DrawingViewModel : ViewModel() {

    private var nextStrokeId = 1

    var playbackElapsedMillis by mutableLongStateOf(0L)
        private set

    private var sink: DrawingPlaybackSink = NoopDrawingPlaybackSink
    private var emitter: DrawingPlaybackEmitter? = null
    private var playbackJob: Job? = null
    private var playbackEndMillis = 0L
    private var elapsedAtResume = 0L
    private var resumeRealtime = 0L

    // Resolve the current sink for every emitter event after Activity recreation.
    private val emitterSink = object : DrawingPlaybackSink by NoopDrawingPlaybackSink {
        override fun strokeStarted(voice: Int, stroke: Stroke) {
            sink.strokeStarted(voice, stroke)
        }

        override fun pointReached(
            voice: Int,
            strokeId: Int,
            point: StrokePoint,
            relativeTimeMillis: Long
        ) {
            sink.pointReached(voice, strokeId, point, relativeTimeMillis)
        }

        override fun strokeEnded(voice: Int, strokeId: Int) {
            sink.strokeEnded(voice, strokeId)
        }
    }

    fun bindSink(sink: DrawingPlaybackSink) {
        this.sink = sink
    }

    var state by mutableStateOf(DrawingState())
        private set

    var lastStrokeMetrics by mutableStateOf<StrokeMetrics?>(null)
        private set

    val canUndo: Boolean
        get() = state.strokes.isNotEmpty()

    val canRedo: Boolean
        get() = state.undoneStrokes.isNotEmpty()

    val playbackStrokes: List<Stroke>
        get() = state.strokes.sortedBy { stroke ->
            stroke.points.firstOrNull()?.x ?: 0f
        }

    fun createStrokeId(): Int = nextStrokeId++

    fun addStroke(stroke: Stroke) {
        lastStrokeMetrics = stroke.calculateMetrics()

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

    fun selectPlaybackAnimationMode(mode: PlaybackAnimationMode) {
        state = state.copy(
            playbackAnimationMode = mode
        )
    }

    fun setPlaybackDurationMillis(durationMillis: Long) {
        state = state.copy(
            playbackDurationMillis = durationMillis
        )
    }

    fun play() {
        if (state.isPlaying || state.strokes.isEmpty()) return

        val controller = DrawingPlaybackController(state.playbackDurationMillis)
        val snapshot = playbackStrokes.map { it.copy(points = it.points.toList()) }
        playbackEndMillis = controller.playbackEndMillis(
            strokes = snapshot,
            mode = PlaybackAnimationMode.HIDE_ALL_REPLAY_TIMING
        )
        emitter = DrawingPlaybackEmitter(snapshot, controller, emitterSink)
        playbackElapsedMillis = 0L
        elapsedAtResume = 0L
        resumeRealtime = 0L
        state = state.copy(isPlaying = true, isPaused = false)
        sink.playbackStarted(controller.durationMillis, state.playbackAnimationMode)
        startClock()
    }

    private fun startClock() {
        resumeRealtime = SystemClock.elapsedRealtime()
        playbackJob = viewModelScope.launch {
            while (true) {
                advancePlayback(SystemClock.elapsedRealtime())
                if (playbackElapsedMillis >= playbackEndMillis) {
                    finishPlayback()
                    return@launch
                }
                delay(16L)
            }
        }
    }

    private fun advancePlayback(realtime: Long) {
        val elapsed = elapsedAtResume + (realtime - resumeRealtime)
        playbackElapsedMillis = minOf(elapsed, playbackEndMillis)
        emitter?.advanceTo(playbackElapsedMillis)
    }

    fun pause() {
        if (!state.isPlaying || state.isPaused) return

        advancePlayback(SystemClock.elapsedRealtime())
        playbackJob?.cancel()
        playbackJob = null
        if (playbackElapsedMillis >= playbackEndMillis) {
            finishPlayback()
            return
        }
        elapsedAtResume = playbackElapsedMillis
        state = state.copy(isPaused = true)
        sink.playbackPaused()
    }

    fun resume() {
        if (!state.isPlaying || !state.isPaused) return

        state = state.copy(isPaused = false)
        sink.playbackResumed()
        startClock()
    }

    private fun finishPlayback() {
        emitter?.endAllActive()
        sink.playbackFinished()
        resetPlayback()
    }

    fun stop() {
        if (!state.isPlaying) return

        sink.playbackAborted()
        resetPlayback()
    }

    private fun resetPlayback() {
        playbackJob?.cancel()
        playbackJob = null
        emitter = null
        playbackElapsedMillis = 0L
        playbackEndMillis = 0L
        elapsedAtResume = 0L
        resumeRealtime = 0L
        state = state.copy(isPlaying = false, isPaused = false)
    }
}
