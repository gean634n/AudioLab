package com.gean634n.audiolab.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gean634n.audiolab.audio.AudioEngine
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gean634n.audiolab.ui.menu.MenuScreen
import com.gean634n.audiolab.ui.touchpad.TouchPadScreen
import com.gean634n.audiolab.ui.volume.VolumeScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.LaunchedEffect
import com.gean634n.audiolab.ui.oscillators.OscillatorScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gean634n.audiolab.settings.SettingsViewModel
import com.gean634n.audiolab.ui.drawing.DrawingScreen
import com.gean634n.audiolab.ui.settings.SettingsScreen

@Composable
fun AudioLabApp(
    audioEngine: AudioEngine
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(context)
    )
    val audioSettings by settingsViewModel.audioSettings.collectAsStateWithLifecycle()

    val audioExecutionState by audioEngine.executionState.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(audioSettings) {
        audioSettings?.let { settings ->
            audioEngine.applySettings(settings)
        }
    }

    LaunchedEffect(currentRoute) {
        if (currentRoute == "menu") {
            audioEngine.mute()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("menu") {
                MenuScreen(
                    onOpenVolume = {
                        navController.navigate("volume")
                    },
                    onOpenTouchPad = {
                        navController.navigate("touchpad")
                    },
                    onOpenOscillators = {
                        navController.navigate("oscillators")
                    },
                    onOpenDrawing = {
                        navController.navigate("drawing")
                    },
                    onOpenSettings = {
                        navController.navigate("settings")
                    }
                )
            }

            composable("volume") {
                VolumeScreen(
                    audioEngine = audioEngine,
                )
            }

            composable("touchpad") {
                TouchPadScreen(
                    audioEngine = audioEngine,
                )
            }

            composable("oscillators") {
                OscillatorScreen(
                    audioEngine = audioEngine,
                )
            }

            composable("drawing") {
                DrawingScreen()
            }

            composable("settings") {
                audioSettings?.let {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        executionState = audioExecutionState,
                        onRetryComputerConnection = audioEngine::retryComputerConnection
                    )
                }
            }
        }
    }

}