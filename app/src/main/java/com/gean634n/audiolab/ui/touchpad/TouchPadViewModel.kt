package com.gean634n.audiolab.ui.touchpad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gean634n.audiolab.audio.AudioEngine
import kotlin.math.pow

private const val MIN_FREQUENCY_HZ = 110f
private const val MAX_FREQUENCY_HZ = 1760f

class TouchPadViewModel(
    private val audioEngine: AudioEngine
) : ViewModel() {

    var position by mutableStateOf(Offset(0.5f, 0.5f))
        private set

    var frequencyHz by mutableFloatStateOf(yToFrequency(position.y))
        private set

    fun onPositionChange(position: Offset) {
        this.position = position
        frequencyHz = yToFrequency(position.y)
        audioEngine.setFrequencyHz(frequencyHz)
    }
}

private fun yToFrequency(y: Float): Float {
    val normalizedY = 1f - y

    return MIN_FREQUENCY_HZ * (MAX_FREQUENCY_HZ / MIN_FREQUENCY_HZ).pow(normalizedY)
}

class TouchPadViewModelFactory(
    private val audioEngine: AudioEngine
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TouchPadViewModel(audioEngine) as T
    }
}