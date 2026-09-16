package com.gean634n.audiolab.settings

import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.audio.AudioExecutionMode
import com.gean634n.audiolab.audio.AudioSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _audioSettings = MutableStateFlow(AudioSettings())
    val audioSettings: StateFlow<AudioSettings> = _audioSettings.asStateFlow()

    fun setExecutionMode(mode: AudioExecutionMode) {
        _audioSettings.update {
            it.copy(executionMode = mode)
        }
    }

    fun setComputerHost(host: String) {
        _audioSettings.update {
            it.copy(computerHost = host)
        }
    }

    fun setComputerPort(port: Int) {
        _audioSettings.update {
            it.copy(computerPort = port)
        }
    }
}