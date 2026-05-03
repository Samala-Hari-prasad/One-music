package com.onemusic.domain.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val albumArtUrl: String,
    val durationMs: Long
)
