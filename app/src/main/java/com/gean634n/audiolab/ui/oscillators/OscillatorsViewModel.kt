package com.gean634n.audiolab.ui.oscillators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.ui.waveform.WaveformType

class OscillatorsViewModel : ViewModel() {

    var activeOscillator by mutableStateOf<WaveformType?>(null)
        private set

    fun onOscillatorPressed(type: WaveformType) {
        activeOscillator = type
    }

    fun onOscillatorReleased() {
        activeOscillator = null
    }
}