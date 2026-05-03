package com.onemusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.onemusic.core.theme.AppleMusicUltraTheme
import com.onemusic.domain.model.Song
import com.onemusic.ui.screens.home.AppleMusicHomeScreen
import com.onemusic.ui.player.AppleMusicPlayer
import com.onemusic.ui.player.AppleMusicMiniPlayer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppleMusicUltraTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    // State for player expansion
    var isPlayerExpanded by remember { mutableStateOf(false) }
    
    // Sample current song
    val currentSong = remember {
        Song("1", "Blinding Lights", "The Weeknd", "", 200000)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content (Navigation goes here, keeping it simple with Home for now)
        Scaffold(
            bottomBar = {
                // Bottom Navigation
            }
        ) { padding ->
            AppleMusicHomeScreen(
                recentSongs = listOf(currentSong),
                onSongClick = { isPlayerExpanded = true },
                modifier = Modifier.padding(padding)
            )
        }

        // Mini Player
        AnimatedVisibility(
            visible = !isPlayerExpanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AppleMusicMiniPlayer(
                song = currentSong,
                isPlaying = false,
                onTogglePlay = { /* Handle */ },
                onExpand = { isPlayerExpanded = true }
            )
        }

        // Full Player
        AnimatedVisibility(
            visible = isPlayerExpanded,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            AppleMusicPlayer(
                song = currentSong,
                isPlaying = false,
                onTogglePlay = { /* Handle */ },
                onMinimize = { isPlayerExpanded = false }
            )
        }
    }
}
