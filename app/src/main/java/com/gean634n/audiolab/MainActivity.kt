package com.gean634n.audiolab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.theme.AudioLabTheme

class MainActivity : ComponentActivity() {
    private lateinit var audioEngine: AudioEngine

    override fun onStart() {
        super.onStart()
        audioEngine.start()
    }

    override fun onStop() {
        super.onStop()
        audioEngine.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioEngine = AudioEngine(this)

        enableEdgeToEdge()
        setContent {
            AudioLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Home( modifier: Modifier = Modifier) {
    Text(
        text = "AudioLab",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AudioLabTheme {
        Home()
    }
}