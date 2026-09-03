package com.gean634n.audiolab.ui.touchpad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel

class TouchPadViewModel : ViewModel() {

    var position by mutableStateOf(
        Offset(0.5f, 0.5f)
    )
        private set

    fun onPositionChange(position: Offset) {
        this.position = position
    }
}