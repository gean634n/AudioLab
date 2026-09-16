package com.gean634n.audiolab.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gean634n.audiolab.audio.AudioExecutionMode
import com.gean634n.audiolab.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.audioSettings.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Execução de áudio")

        AudioModeOption(
            text = "No dispositivo",
            selected = settings.executionMode == AudioExecutionMode.DEVICE,
            onClick = {
                viewModel.setExecutionMode(AudioExecutionMode.DEVICE)
            }
        )

        AudioModeOption(
            text = "No computador",
            selected = settings.executionMode == AudioExecutionMode.COMPUTER,
            onClick = {
                viewModel.setExecutionMode(AudioExecutionMode.COMPUTER)
            }
        )

        if (settings.executionMode == AudioExecutionMode.COMPUTER) {
            OutlinedTextField(
                value = settings.computerHost,
                onValueChange = viewModel::setComputerHost,
                label = { Text("IP") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = settings.computerPort.toString(),
                onValueChange = { value ->
                    value.toIntOrNull()?.let(viewModel::setComputerPort)
                },
                label = { Text("Porta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AudioModeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(text = text)
    }
}