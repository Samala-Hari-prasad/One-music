package com.onemusic.features.lyrics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class LyricLine(val text: String, val timestampMs: Long)

@Composable
fun LyricsScreen(
    currentTimestampMs: Long,
    lyrics: List<LyricLine>,
    onLineClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Find active line index
    val activeIndex = lyrics.indexOfLast { it.timestampMs <= currentTimestampMs }.coerceAtLeast(0)

    // Auto-scroll to active line
    LaunchedEffect(activeIndex) {
        scope.launch {
            listState.animateScrollToItem(activeIndex, scrollOffset = -200)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 300.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(lyrics) { index, line ->
                val isActive = index == activeIndex
                val color by animateColorAsState(
                    targetValue = if (isActive) Color.White else Color.White.copy(alpha = 0.2f),
                    label = "LyricColor"
                )
                val scale by animateFloatAsState(
                    targetValue = if (isActive) 1.1f else 1.0f,
                    label = "LyricScale"
                )

                Text(
                    text = line.text,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        lineHeight = 36.sp
                    ),
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .clickable { onLineClick(line.timestampMs) }
                )
            }
        }
    }
}
