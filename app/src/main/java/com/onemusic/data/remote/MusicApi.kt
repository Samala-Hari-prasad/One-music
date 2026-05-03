package com.onemusic.data.remote

import com.onemusic.domain.model.Song

/**
 * Interface for fetching ad-free music from a remote source.
 * You can implement this using Retrofit or any other networking library.
 */
interface MusicApi {
    suspend fun getTrendingMusic(): List<Song>
    suspend fun searchMusic(query: String): List<Song>
    suspend fun getLyrics(songId: String): String
}

class MusicApiImpl : MusicApi {
    override suspend fun getTrendingMusic(): List<Song> {
        // In a real app, this would be a Retrofit call to an ad-free music API
        return listOf(
            Song("1", "Blinding Lights", "The Weeknd", "https://example.com/art1.jpg", 200000),
            Song("2", "Shape of You", "Ed Sheeran", "https://example.com/art2.jpg", 233000),
            Song("3", "Levitating", "Dua Lipa", "https://example.com/art3.jpg", 210000)
        )
    }

    override suspend fun searchMusic(query: String): List<Song> {
        return emptyList()
    }

    override suspend fun getLyrics(songId: String): String {
        return "Lyrics would be fetched here..."
    }
}
