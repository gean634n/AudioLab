package com.gean634n.audiolab.settings

import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.audio.AudioSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    private val _audioSettings = MutableStateFlow(AudioSettings())
    val audioSettings: StateFlow<AudioSettings> = _audioSettings.asStateFlow()

    fun apply(settings: AudioSettings) {
        _audioSettings.value = settings
    }
}