package com.gean634n.audiolab.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import com.gean634n.audiolab.audio.AudioSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsStore: AudioSettingsStore
) : ViewModel() {

    private val _audioSettings = MutableStateFlow<AudioSettings?>(null)
    val audioSettings: StateFlow<AudioSettings?> =
        _audioSettings.asStateFlow()

    init {
        viewModelScope.launch {
            settingsStore.settings.collect { settings ->
                _audioSettings.value = settings
            }
        }
    }

    fun apply(settings: AudioSettings) {
        viewModelScope.launch {
            settingsStore.save(settings)
        }
    }

    companion object {
        fun factory(context: Context) = viewModelFactory {
            initializer {
                SettingsViewModel(
                    settingsStore = AudioSettingsStore(
                        context.applicationContext
                    )
                )
            }
        }
    }
}