package com.gean634n.audiolab.ui.drawing

import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.drawing.DrawingPlaybackSink
import com.gean634n.audiolab.drawing.PlaybackAnimationMode
import com.gean634n.audiolab.drawing.Stroke
import com.gean634n.audiolab.drawing.StrokePoint

class AudioEngineDrawingSink(
    private val audioEngine: AudioEngine
) : DrawingPlaybackSink {
    override fun playbackStarted(durationMillis: Long, mode: PlaybackAnimationMode) {
        audioEngine.playStart(
            durationMillis = durationMillis.coerceIn(0L, Int.MAX_VALUE.toLong()).toInt(),
            mode = mode.name.lowercase(),
        )
    }

    override fun strokeStarted(voice: Int, stroke: Stroke) {
        val (red, green, blue) = stroke.color.toNormalizedRgb()

        audioEngine.playStroke(
            voice = voice,
            id = stroke.id,
            tool = stroke.tool.name.lowercase(),
            lineStyle = stroke.lineStyle.name.lowercase(),
            red = red,
            green = green,
            blue = blue,
        )
    }

    override fun pointReached(
        voice: Int,
        strokeId: Int,
        point: StrokePoint,
        relativeTimeMillis: Long
    ) {
        audioEngine.playPoint(
            voice = voice,
            id = strokeId,
            x = point.x,
            y = point.y,
            timeMillis = relativeTimeMillis.toFloat(),
        )
    }

    override fun strokeEnded(voice: Int, strokeId: Int) {
        audioEngine.playEnd(voice, strokeId)
    }

    override fun playbackPaused() {
        audioEngine.playPause()
    }

    override fun playbackResumed() {
        audioEngine.playResume()
    }

    override fun playbackFinished() {
        audioEngine.playFinish()
    }

    override fun playbackAborted() {
        audioEngine.playAbort()
    }
}
