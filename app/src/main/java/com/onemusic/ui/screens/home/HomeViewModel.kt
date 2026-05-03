package com.onemusic.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onemusic.data.repository.MusicRepository
import com.onemusic.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadTrendingMusic()
    }

    fun loadTrendingMusic() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getTrendingMusic().collect { songs ->
                _uiState.value = if (songs.isEmpty()) {
                    HomeUiState.Error("No music found. Check your internet connection.")
                } else {
                    HomeUiState.Success(songs)
                }
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val trendingSongs: List<Song>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
