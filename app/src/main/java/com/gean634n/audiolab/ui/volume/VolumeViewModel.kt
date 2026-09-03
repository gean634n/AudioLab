package com.gean634n.audiolab.ui.volume

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gean634n.audiolab.audio.AudioEngine

const val MIN_LEVEL_DB = -60f
const val MAX_LEVEL_DB = 0f
class VolumeViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {
    var levelDb by mutableFloatStateOf(MIN_LEVEL_DB + 0.8f * (MAX_LEVEL_DB - MIN_LEVEL_DB))
        private set

    init {
        audioEngine.setLevelDb(levelDb)
    }

    fun onLevelChange(value: Float) {
        levelDb = value
        audioEngine.setLevelDb(value)
    }
}

class VolumeViewModelFactory(
    private val audioEngine: AudioEngine
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VolumeViewModel(audioEngine) as T
    }
}