package com.onemusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.onemusic.core.theme.AppleMusicUltraTheme
import com.onemusic.domain.model.Song
import com.onemusic.ui.screens.home.AppleMusicHomeScreen
import com.onemusic.ui.screens.home.HomeUiState
import com.onemusic.ui.screens.home.HomeViewModel
import com.onemusic.ui.screens.search.AppleMusicSearchScreen
import com.onemusic.ui.screens.search.SearchUiState
import com.onemusic.ui.screens.search.SearchViewModel
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
fun MainScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    // Navigation State
    var currentTab by remember { mutableStateOf("home") }
    
    // State for player expansion
    var isPlayerExpanded by remember { mutableStateOf(false) }
    
    // UI States
    val homeState by homeViewModel.uiState.collectAsState()
    val searchState by searchViewModel.uiState.collectAsState()
    
    // Selected Song State
    var currentSong by remember { mutableStateOf<Song?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {
                    NavigationBarItem(
                        selected = currentTab == "home",
                        onClick = { currentTab = "home" },
                        icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
                        label = { Text("Listen Now") }
                    )
                    NavigationBarItem(
                        selected = currentTab == "search",
                        onClick = { currentTab = "search" },
                        icon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                        label = { Text("Search") }
                    )
                }
            }
        ) { padding ->
            when (currentTab) {
                "home" -> {
                    when (val state = homeState) {
                        is HomeUiState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is HomeUiState.Error -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(state.message)
                            }
                        }
                        is HomeUiState.Success -> {
                            if (currentSong == null) currentSong = state.trendingSongs.firstOrNull()
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
                "search" -> {
                    AppleMusicSearchScreen(
                        uiState = searchState,
                        onSearch = { searchViewModel.search(it) },
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
