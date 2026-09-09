package com.gean634n.audiolab.ui.oscillators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gean634n.audiolab.ui.waveform.WaveformType
import kotlin.math.pow

private const val MIN_FREQUENCY_HZ = 55f
private const val MAX_FREQUENCY_HZ = 880f

class OscillatorsViewModel : ViewModel() {

    var activeOscillator by mutableStateOf<WaveformType?>(null)
        private set

    var frequencyHz by mutableFloatStateOf(yToFrequency(0.5f))
        private set

    fun onOscillatorPressed(type: WaveformType) {
        activeOscillator = type
    }

    fun onOscillatorReleased() {
        activeOscillator = null
    }

    fun onPositionChange(y: Float) {
        frequencyHz = yToFrequency(y)
    }
}

private fun yToFrequency(y: Float): Float {
    val normalizedY = 1f - y.coerceIn(0f, 1f)

    return MIN_FREQUENCY_HZ *
            (MAX_FREQUENCY_HZ / MIN_FREQUENCY_HZ).pow(normalizedY)
}