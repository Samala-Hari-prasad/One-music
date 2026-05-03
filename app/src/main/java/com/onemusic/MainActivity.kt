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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.onemusic.core.theme.AppleMusicUltraTheme
import com.onemusic.domain.model.Song
import com.onemusic.ui.screens.home.AppleMusicHomeScreen
import com.onemusic.ui.screens.home.HomeUiState
import com.onemusic.ui.screens.home.HomeViewModel
import com.onemusic.ui.player.AppleMusicPlayer
import com.onemusic.ui.player.AppleMusicMiniPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
fun MainScreen(viewModel: HomeViewModel = hiltViewModel()) {
    // State for player expansion
    var isPlayerExpanded by remember { mutableStateOf(false) }
    
    // UI State from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    
    // Selected Song State
    var currentSong by remember { mutableStateOf<Song?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is HomeUiState.Error -> {
                Text(state.message, modifier = Modifier.align(Alignment.Center))
            }
            is HomeUiState.Success -> {
                if (currentSong == null) {
                    currentSong = state.trendingSongs.firstOrNull()
                }

                Scaffold(
                    bottomBar = { }
                ) { padding ->
                    AppleMusicHomeScreen(
                        recentSongs = state.trendingSongs,
                        onSongClick = { 
                            currentSong = it
                            isPlayerExpanded = true 
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        // Mini Player (Only if song exists)
        currentSong?.let { song ->
            AnimatedVisibility(
                visible = !isPlayerExpanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AppleMusicMiniPlayer(
                    song = song,
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
                    song = song,
                    isPlaying = false,
                    onTogglePlay = { /* Handle */ },
                    onMinimize = { isPlayerExpanded = false }
                )
            }
        }
    }
}
