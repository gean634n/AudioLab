package com.gean634n.audiolab.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.gean634n.audiolab.audio.AudioEngine

@Composable
fun AudioLabApp(
    audioEngine: AudioEngine
) {
    val navController = rememberNavController()
}