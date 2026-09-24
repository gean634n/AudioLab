package com.gean634n.audiolab.drawing

interface DrawingPlaybackSink {
    fun playbackStarted(durationMillis: Long, mode: PlaybackAnimationMode)
    fun strokeStarted(voice: Int, stroke: Stroke)
    fun pointReached(
        voice: Int,
        strokeId: Int,
        point: StrokePoint,
        relativeTimeMillis: Long
    )
    fun strokeEnded(voice: Int, strokeId: Int)
    fun playbackPaused()
    fun playbackResumed()
    fun playbackFinished()
    fun playbackAborted()
}

object NoopDrawingPlaybackSink : DrawingPlaybackSink {
    override fun playbackStarted(durationMillis: Long, mode: PlaybackAnimationMode) {}
    override fun strokeStarted(voice: Int, stroke: Stroke) {}
    override fun pointReached(
        voice: Int,
        strokeId: Int,
        point: StrokePoint,
        relativeTimeMillis: Long
    ) {}
    override fun strokeEnded(voice: Int, strokeId: Int) {}
    override fun playbackPaused() {}
    override fun playbackResumed() {}
    override fun playbackFinished() {}
    override fun playbackAborted() {}
}
