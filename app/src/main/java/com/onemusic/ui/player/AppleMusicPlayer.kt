package com.onemusic.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.onemusic.domain.model.Song
import kotlin.math.roundToInt

@Composable
fun AppleMusicPlayer(
    song: Song,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onMinimize: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableStateOf(0f) }
    
    // Smooth Art Scale (85% when paused, 100% when playing)
    val artScale by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0.85f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "ArtScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetY = (offsetY + dragAmount.y).coerceAtLeast(0f)
                    },
                    onDragEnd = {
                        if (offsetY > 300f) onMinimize() else offsetY = 0f
                    }
                )
            }
            .background(Color.Black)
    ) {
        // 1. DYNAMIC BLUR BACKGROUND
        AsyncImage(
            model = song.albumArtUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().blur(80.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.5f
        )
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                )
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.2f))
            )

            Spacer(modifier = Modifier.height(60.dp))

            // 2. LARGE ROUNDED ALBUM ART (24dp)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .scale(artScale),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 32.dp)
            ) {
                AsyncImage(
                    model = song.albumArtUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 3. SONG INFO (Hyper-Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = song.artist,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    )
                }
                IconButton(
                    onClick = { /* More */ },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.MoreHoriz, null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 4. THICK PROGRESS BAR
            Column {
                Slider(
                    value = 0.4f,
                    onValueChange = {},
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1:42", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.labelSmall)
                    Text("-2:18", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 5. PLAYBACK CONTROLS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(50.dp))
                }
                
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.SkipNext, null, tint = Color.White, modifier = Modifier.size(50.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))
            
            // 6. EXTRA CONTROLS
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.Lyrics, null, tint = Color.White.copy(alpha = 0.6f)) }
                IconButton(onClick = {}) { Icon(Icons.Default.Speed, null, tint = Color.White.copy(alpha = 0.6f)) }
                IconButton(onClick = {}) { Icon(Icons.Default.QueueMusic, null, tint = Color.White.copy(alpha = 0.6f)) }
            }
        }
    }
}
