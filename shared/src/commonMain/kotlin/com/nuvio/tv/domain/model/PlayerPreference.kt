package com.nuvio.tv.domain.model

enum class PlayerPreference {
    INTERNAL,
    EXTERNAL
}

enum class DecoderPriority {
    AUTO,
    HARDWARE,
    SOFTWARE
}

data class SubtitleStyle(
    val size: Int = 16,
    val textColor: Int = 0xFFFFFFFF.toInt(),
    val backgroundColor: Int = 0x80000000.toInt(),
    val bold: Boolean = false,
    val outlineEnabled: Boolean = true,
    val outlineColor: Int = 0xFF000000.toInt(),
    val outlineWidth: Int = 2
)
