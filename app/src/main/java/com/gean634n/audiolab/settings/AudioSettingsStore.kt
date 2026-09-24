package com.gean634n.audiolab.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gean634n.audiolab.audio.AudioExecutionMode
import com.gean634n.audiolab.audio.AudioSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.audioSettingsDataStore by preferencesDataStore(
    name = "audio_settings"
)

class AudioSettingsStore(
    appContext: Context
) {
    private val context = appContext.applicationContext
    private val defaults = AudioSettings()

    private object Keys {
        val EXECUTION_MODE = stringPreferencesKey("execution_mode")
        val COMPUTER_HOST = stringPreferencesKey("computer_host")
        val COMPUTER_PORT = stringPreferencesKey("computer_port")
        val REPLY_PORT = stringPreferencesKey("reply_port")
    }

    val settings: Flow<AudioSettings> =
        context.audioSettingsDataStore.data.map { preferences ->
            AudioSettings(
                executionMode = preferences[Keys.EXECUTION_MODE]
                    ?.let { value ->
                        AudioExecutionMode.entries
                            .firstOrNull { it.name == value }
                    }
                    ?: defaults.executionMode,

                computerHost = preferences[Keys.COMPUTER_HOST] ?: defaults.computerHost,
                computerPort = preferences[Keys.COMPUTER_PORT] ?: defaults.computerPort,
                replyPort = preferences[Keys.REPLY_PORT] ?: defaults.replyPort
            )
        }
    suspend fun save(settings: AudioSettings) {
        context.audioSettingsDataStore.edit { preferences ->
            preferences[Keys.EXECUTION_MODE] = settings.executionMode.name
            preferences[Keys.COMPUTER_HOST] = settings.computerHost
            preferences[Keys.COMPUTER_PORT] = settings.computerPort
            preferences[Keys.REPLY_PORT] = settings.replyPort
        }
    }
}
