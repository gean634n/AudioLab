package com.gean634n.audiolab.ui.volume

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gean634n.audiolab.audio.AudioEngine

class VolumeViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {
    var levelDb by mutableStateOf(-60f)
        private set

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