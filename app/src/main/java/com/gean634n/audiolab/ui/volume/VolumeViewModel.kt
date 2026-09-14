package com.gean634n.audiolab.ui.volume

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.waveform.WaveformType

const val MIN_LEVEL_DB = -30f
const val MAX_LEVEL_DB = 0f
private const val LESSON_FREQUENCY_HZ = 440f
private const val DEFAULT_LEVEL_DB = MIN_LEVEL_DB + 0.8f * (MAX_LEVEL_DB - MIN_LEVEL_DB)

class VolumeViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {
    var levelDb by mutableFloatStateOf(DEFAULT_LEVEL_DB)
        private set

    init {
        audioEngine.setFrequencyHz(LESSON_FREQUENCY_HZ)
        audioEngine.setWaveform(WaveformType.SINE)
        audioEngine.setLevelDb(levelDb)
    }

    override fun onCleared() {
        audioEngine.mute()
    }

    fun onLevelChange(value: Float) {
        levelDb = value
        audioEngine.setLevelDb(value)
    }
}