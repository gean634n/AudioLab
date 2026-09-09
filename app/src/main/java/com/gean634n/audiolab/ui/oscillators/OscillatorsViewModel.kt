package com.gean634n.audiolab.ui.oscillators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.waveform.WaveformType

class OscillatorsViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {

    var activeOscillator by mutableStateOf<WaveformType?>(null)
        private set

    var frequencyHz by mutableFloatStateOf(yToFrequency(0.5f))
        private set

    fun onOscillatorPressed(type: WaveformType) {
        activeOscillator = type

        audioEngine.setFrequencyHz(frequencyHz)
        audioEngine.setLevelDb(0f)
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

class OscillatorsViewModelFactory(
    private val audioEngine: AudioEngine
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OscillatorsViewModel(audioEngine) as T
    }
}