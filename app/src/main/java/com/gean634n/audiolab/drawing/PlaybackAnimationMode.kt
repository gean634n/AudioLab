package com.gean634n.audiolab.drawing

enum class PlaybackAnimationMode {
    // All strokes hide.
    // The stroke appears full when the playhead touches its start.
    HIDE_ALL_SHOW_FULL,

    // All strokes hide.
    // The stroke is redrawn respecting its original timing.
    HIDE_ALL_REPLAY_TIMING,

    // Strokes remain visible.
    // When touched, the stroke hides and reappears full.
    BLINK_FULL,

    // Strokes remain visible.
    // When touched, the stroke hides and is redrawn respecting its timing.
    BLINK_REPLAY_TIMING
}