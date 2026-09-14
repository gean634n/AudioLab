package com.gean634n.audiolab.ui.oscillators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.waveform.WaveformType

private const val DEFAULT_LEVEL_DB = -6f

class OscillatorsViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {

    var activeOscillator by mutableStateOf<WaveformType?>(null)
        private set

    var frequencyHz by mutableFloatStateOf(yToFrequency(0.5f))
        private set

    fun onOscillatorPressed(type: WaveformType) {
        activeOscillator = type

        audioEngine.setWaveform(type)
        audioEngine.setFrequencyHz(frequencyHz)
        audioEngine.setLevelDb(DEFAULT_LEVEL_DB)
    }

    fun onOscillatorReleased() {
        activeOscillator = null

        audioEngine.mute()
    }

    fun onPositionChange(y: Float) {
        frequencyHz = yToFrequency(y)

        audioEngine.setFrequencyHz(frequencyHz)
    }
}