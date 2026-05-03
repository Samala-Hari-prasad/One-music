package com.onemusic.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Apple Music Ultra-Premium Tokens
val AppleCornerLarge = RoundedCornerShape(24.dp)
val AppleCornerMedium = RoundedCornerShape(16.dp)

private val UltraDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFA243C),
    background = Color(0xFF000000), // AMOLED
    surface = Color(0xFF1C1C1E),
    onBackground = Color.White,
    onSurface = Color.White,
    secondaryContainer = Color(0xFF2C2C2E)
)

private val UltraLightColorScheme = lightColorScheme(
    primary = Color(0xFFFA243C),
    background = Color(0xFFF2F2F7),
    surface = Color.White,
    onBackground = Color(0xFF1C1C1E),
    onSurface = Color(0xFF1C1C1E),
    secondaryContainer = Color(0xFFF2F2F7)
)

@Composable
fun AppleMusicUltraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) UltraDarkColorScheme else UltraLightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            headlineMedium = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            ),
            titleLarge = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            bodyLarge = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal
            )
        ),
        shapes = Shapes(
            medium = AppleCornerMedium,
            large = AppleCornerLarge
        ),
        content = content
    )
}
