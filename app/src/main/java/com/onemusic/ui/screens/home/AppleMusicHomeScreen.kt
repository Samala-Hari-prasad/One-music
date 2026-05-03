package com.onemusic.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.onemusic.domain.model.Song

@Composable
fun AppleMusicHomeScreen(
    recentSongs: List<Song>,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 100.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)) {
            Text(
                text = "YOUR MUSIC",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Listen Now",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Hero Section (Local Highlight)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(280.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable { recentSongs.firstOrNull()?.let(onSongClick) }
        ) {
            AsyncImage(
                model = recentSongs.firstOrNull()?.albumArtUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text("MOST PLAYED", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                Text(
                    text = recentSongs.firstOrNull()?.title ?: "No Music Found",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Horizontal Sections
        UltraHomeSection("Recently Played", recentSongs, onSongClick)
        Spacer(modifier = Modifier.height(32.dp))
        UltraHomeSection("Albums", recentSongs.shuffled(), onSongClick)
        Spacer(modifier = Modifier.height(32.dp))
        UltraHomeSection("Favorites", recentSongs.reversed(), onSongClick)
    }
}

@Composable
fun UltraHomeSection(title: String, songs: List<Song>, onSongClick: (Song) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(songs) { song ->
                UltraSongCard(song, onSongClick)
            }
        }
    }
}

@Composable
fun UltraSongCard(song: Song, onClick: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .width(170.dp)
            .clickable { onClick(song) }
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.size(170.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AsyncImage(
                model = song.albumArtUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
        Text(
            text = song.artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            maxLines = 1
        )
    }
}
