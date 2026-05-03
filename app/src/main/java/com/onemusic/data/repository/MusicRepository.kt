package com.onemusic.data.repository

import com.nikhil.yt.innertube.YouTube
import com.nikhil.yt.innertube.models.SongItem
import com.onemusic.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicRepository {

    /**
     * Fetches trending music using the Velune (YouTube Music) method.
     */
    fun getTrendingMusic(): Flow<List<Song>> = flow {
        YouTube.home().onSuccess { homePage ->
            val songs = homePage.sections
                .flatMap { it.items }
                .filterIsInstance<SongItem>()
                .map { songItem ->
                    Song(
                        id = songItem.id,
                        title = songItem.title,
                        artist = songItem.artists.joinToString { it.name },
                        albumArtUrl = songItem.thumbnail,
                        durationMs = 0L // Can be fetched from playerResponse if needed
                    )
                }
            emit(songs)
        }.onFailure {
            emit(emptyList())
        }
    }

    /**
     * Searches for ad-free music using InnerTube.
     */
    suspend fun searchMusic(query: String): List<Song> {
        return YouTube.searchSummary(query).getOrNull()?.summaries
            ?.flatMap { it.items }
            ?.filterIsInstance<SongItem>()
            ?.map { songItem ->
                Song(
                    id = songItem.id,
                    title = songItem.title,
                    artist = songItem.artists.joinToString { it.name },
                    albumArtUrl = songItem.thumbnail,
                    durationMs = 0L
                )
            } ?: emptyList()
    }
}
