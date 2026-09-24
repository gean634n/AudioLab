package com.gean634n.audiolab.drawing

class DrawingPlaybackEmitter(
    strokes: List<Stroke>,
    private val controller: DrawingPlaybackController,
    private val sink: DrawingPlaybackSink,
    private val maxVoices: Int = 8
) {
    init {
        require(maxVoices in 1..8)
    }

    private enum class Status { PENDING, ACTIVE, DONE }

    private class PlaybackStroke(val stroke: Stroke) {
        var status = if (stroke.points.isEmpty()) Status.DONE else Status.PENDING
        var voice = -1
        var nextPoint = 0
        var activationOrder = 0L
    }

    private val playbackStrokes = strokes.map { stroke ->
        PlaybackStroke(stroke.copy(points = stroke.points.toList()))
    }
    private var nextActivationOrder = 0L

    fun advanceTo(elapsedMillis: Long) {
        for (entry in playbackStrokes) {
            if (entry.status == Status.DONE) continue
            val firstPoint = entry.stroke.points.first()

            if (entry.status == Status.PENDING) {
                if (!controller.hasReached(firstPoint.x, elapsedMillis)) continue
                entry.voice = allocateVoice()
                entry.activationOrder = nextActivationOrder++
                entry.status = Status.ACTIVE
                sink.strokeStarted(entry.voice, entry.stroke)
            }

            val strokeElapsed = controller.strokeElapsedMillis(firstPoint.x, elapsedMillis)
            while (entry.nextPoint < entry.stroke.points.size) {
                val point = entry.stroke.points[entry.nextPoint]
                val relative = point.timeMillis - firstPoint.timeMillis
                if (relative > strokeElapsed) break
                sink.pointReached(entry.voice, entry.stroke.id, point, relative)
                entry.nextPoint++
            }

            if (entry.nextPoint == entry.stroke.points.size) {
                end(entry)
            }
        }
    }

    private fun allocateVoice(): Int {
        val active = playbackStrokes.filter { it.status == Status.ACTIVE }
        val freeVoice = (0 until maxVoices).firstOrNull { voice ->
            active.none { it.voice == voice }
        }
        if (freeVoice != null) return freeVoice

        val oldest = active.minBy { it.activationOrder }
        val voice = oldest.voice
        end(oldest)
        return voice
    }

    private fun end(entry: PlaybackStroke) {
        sink.strokeEnded(entry.voice, entry.stroke.id)
        entry.status = Status.DONE
        entry.voice = -1
    }

    fun endAllActive() {
        playbackStrokes.filter { it.status == Status.ACTIVE }.forEach { end(it) }
    }
}
