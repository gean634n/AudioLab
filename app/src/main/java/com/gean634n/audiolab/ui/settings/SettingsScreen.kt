package com.gean634n.audiolab.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gean634n.audiolab.audio.AudioExecutionMode
import com.gean634n.audiolab.settings.SettingsViewModel
import com.gean634n.audiolab.ui.theme.OutlineColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.gean634n.audiolab.audio.AudioExecutionState
import com.gean634n.audiolab.ui.drawing.SketchButton

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    executionState: AudioExecutionState,
    onRetryComputerConnection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val applied by viewModel.audioSettings.collectAsStateWithLifecycle()
    val currentSettings = applied ?: return

    var mode by rememberSaveable(currentSettings) {
        mutableStateOf(currentSettings.executionMode)
    }

    var host by rememberSaveable(currentSettings) {
        mutableStateOf(currentSettings.computerHost)
    }

    var port by rememberSaveable(currentSettings) {
        mutableStateOf(currentSettings.computerPort)
    }

    val draft = currentSettings.copy(
        executionMode = mode,
        computerHost = host,
        computerPort = port
    )

    val canApply =
        draft != currentSettings && (
                draft.executionMode == AudioExecutionMode.DEVICE || draft.isComputerConfigValid
        )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Execução de áudio")

        AudioModeOption(
            label = "No dispositivo",
            selected = mode == AudioExecutionMode.DEVICE,
            onClick = { mode = AudioExecutionMode.DEVICE }
        )

        AudioModeOption(
            label = "No computador",
            selected = mode == AudioExecutionMode.COMPUTER,
            onClick = { mode = AudioExecutionMode.COMPUTER }
        )

        if (mode == AudioExecutionMode.COMPUTER) {
            SketchTextField(
                label = "IP",
                value = host,
                onValueChange = { host = it },
                isError = !draft.isComputerIpValid,
                modifier = Modifier
                    .fillMaxWidth()
            )

            SketchTextField(
                label = "Porta",
                value = port,
                onValueChange = { value ->
                    if (value.all(Char::isDigit)) {
                        port = value
                    }
                },
                keyboardType = KeyboardType.Number,
                isError = !draft.isComputerPortValid,
                modifier = Modifier.fillMaxWidth()
            )

            when (executionState) {
                AudioExecutionState.COMPUTER_CONNECTING -> {
                    Text("Conectando...")
                }

                AudioExecutionState.COMPUTER -> {
                    Text("Áudio no computador")
                }

                AudioExecutionState.COMPUTER_UNAVAILABLE -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Computador indisponível — áudio no dispositivo")

                        SketchButton(
                            onClick = onRetryComputerConnection
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }

                AudioExecutionState.DEVICE -> Unit
            }
        }

        SketchButton(
            onClick = { viewModel.apply(draft) },
            enabled = canApply,
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Text("Aplicar")
        }
    }
}

@Composable
private fun AudioModeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            val strokeWidth = 2.dp.toPx()

            drawCircle(
                color = OutlineColor,
                style = Stroke(width = strokeWidth)
            )

            if (selected) {
                drawCircle(
                    color = OutlineColor,
                    radius = (size.minDimension / 2f) * 0.5f
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SketchTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OutlineColor,
            unfocusedBorderColor = OutlineColor,
            focusedLabelColor = OutlineColor,
            unfocusedLabelColor = OutlineColor,
            focusedTextColor = OutlineColor,
            unfocusedTextColor = OutlineColor,
            cursorColor = OutlineColor
        ),
        modifier = modifier
            .padding(top = 4.dp)
    )
}
