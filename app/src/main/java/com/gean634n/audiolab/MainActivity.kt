package com.gean634n.audiolab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gean634n.audiolab.audio.AudioEngine
import com.gean634n.audiolab.ui.volume.VolumeViewModel
import com.gean634n.audiolab.ui.volume.VolumeViewModelFactory
import com.gean634n.audiolab.ui.theme.AudioLabTheme
import com.gean634n.audiolab.ui.volume.MAX_LEVEL_DB
import com.gean634n.audiolab.ui.volume.MIN_LEVEL_DB
import com.gean634n.audiolab.ui.volume.TriangleSlider

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
    Column(modifier = modifier
        .fillMaxSize()
        .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${viewModel.levelDb} dB"
        )

        TriangleSlider(
            value = viewModel.levelDb,
            valueRange = MIN_LEVEL_DB..MAX_LEVEL_DB,
            onValueChange = viewModel::onLevelChange,
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

        TriangleSlider(
            value = levelDb,
            valueRange = MIN_LEVEL_DB..MAX_LEVEL_DB,
            onValueChange = onLevelChange,
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