package com.gean634n.audiolab.ui

import androidx.compose.runtime.Composable
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

@Composable
fun AudioLabApp(
    audioEngine: AudioEngine
) {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (currentRoute == "menu") {
            audioEngine.mute()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            MenuScreen(
                onOpenVolume = {
                    navController.navigate("volume")
                },
                onOpenTouchPad = {
                    navController.navigate("touchpad")
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
    }
}