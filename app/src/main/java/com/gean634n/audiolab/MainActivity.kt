package com.gean634n.audiolab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.volume.VolumeViewModel
import com.gean634n.audiolab.ui.volume.VolumeViewModelFactory
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
                        audioEngine = audioEngine,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Home(
    audioEngine: AudioEngine,
    modifier: Modifier = Modifier,
    viewModel: VolumeViewModel = viewModel(
        factory = VolumeViewModelFactory(audioEngine)
    )
) {
    Column(modifier = modifier) {
        Text(
            text = "${viewModel.levelDb} dB"
        )

        Slider(
            value = viewModel.levelDb,
            onValueChange = viewModel::onLevelChange,
            valueRange = -60f..0f
        )
    }

}

@Composable
fun HomeContent(
    levelDb: Float,
    onLevelChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "$levelDb dB"
        )

        Slider(
            value = levelDb,
            onValueChange = onLevelChange,
            valueRange = -60f..0f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    AudioLabTheme {
        HomeContent(
            levelDb = -12f,
            onLevelChange = {}
        )
    }
}