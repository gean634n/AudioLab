package com.gean634n.audiolab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.theme.AudioLabTheme
import com.gean634n.audiolab.ui.volume.VolumeScreen

class MainActivity : ComponentActivity() {

    private lateinit var audioEngine: AudioEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioEngine = AudioEngine(this)

        configureWindow()

        setContent {
            AudioLabTheme {
                VolumeScreen( audioEngine = audioEngine )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        audioEngine.start()
    }

    override fun onStop() {
        audioEngine.stop()
        super.onStop()
    }

    private fun configureWindow() {
        WindowCompat.enableEdgeToEdge(window)

        WindowCompat
            .getInsetsController(window, window.decorView)
            .apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
    }
}
