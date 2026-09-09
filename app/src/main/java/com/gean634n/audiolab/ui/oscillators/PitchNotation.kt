package com.gean634n.audiolab.ui.oscillators

import kotlin.math.ln
import kotlin.math.roundToInt

data class PitchNotation(
    val note: String,
    val cents: Int
)

fun frequencyToPitchNotation(frequencyHz: Float): PitchNotation {
    val midi =
        69f + 12f * (ln(frequencyHz / 440f) / ln(2f))

    val nearestMidi = midi.roundToInt()
    val cents = ((midi - nearestMidi) * 100f).roundToInt()

    val noteNames = arrayOf(
        "C", "C#", "D", "D#", "E", "F",
        "F#", "G", "G#", "A", "A#", "B"
    )

    val noteIndex = (nearestMidi % 12 + 12) % 12
    val octave = nearestMidi / 12 - 1

    val note = "${noteNames[noteIndex]}$octave"
    // val note = noteNames[noteIndex]


    return PitchNotation(
        note = note,
        cents = cents
    )
}