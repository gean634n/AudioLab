package com.gean634n.audiolab.audio

import com.gean634n.audiolab.ui.waveform.WaveformType

data class SynthState(
    val levelDb: Float = -6f,
    val frequencyHz: Float = 440f,
    val waveform: WaveformType = WaveformType.SINE
)