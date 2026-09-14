package com.gean634n.audiolab.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.oscillators.OscillatorsViewModel
import com.gean634n.audiolab.ui.touchpad.TouchPadViewModel
import com.gean634n.audiolab.ui.volume.VolumeViewModel

fun audioEngineViewModelFactory(
    audioEngine: AudioEngine
): ViewModelProvider.Factory {
    return viewModelFactory {
        initializer {
            VolumeViewModel(audioEngine)
        }

        initializer {
            OscillatorsViewModel(audioEngine)
        }

        initializer {
            TouchPadViewModel(audioEngine)
        }
    }
}