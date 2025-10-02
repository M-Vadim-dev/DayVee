package com.example.dayvee.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MediumPurple,           // основной акцентный цвет (кнопки, выделения)
    onPrimary = GhostWhite,           // текст/иконки на primary

    primaryContainer = DarkSlateGray, // фон для контейнеров (карточки, кнопки)
    onPrimaryContainer = GhostWhite,  // текст/иконки на primaryContainer

    secondary = DarkSlateGrayDarker,  // второстепенные элементы (иконки, кнопки)
    onSecondary = SlateGray,               // текст/иконки на secondary

    tertiary = MediumOrchid,          // дополнительный акцентный цвет
    onTertiary = GhostWhite,          // текст/иконки на tertiary

    background = MidnightBlue,        // общий фон приложения
    onBackground = GhostWhite,        // текст/иконки на фоне

    surface = DarkSlateGray,           // фон поверхностей (card, bottom sheet)
    onSurface = GhostWhite,

    surfaceVariant = GhostWhite,   // альтернативный фон для разделения блоков
    onSurfaceVariant = Gray,     // текст/иконки на surfaceVariant

    outline = Gray,              // цвет для бордеров, разделителей

    error = CriticalRed,              // ошибки, предупреждения
)

private val LightColorScheme = lightColorScheme(
    primary = MediumPurple,
    onPrimary = MidnightBlue,

    primaryContainer = GhostWhite,
    onPrimaryContainer = MidnightBlue,

    secondary = Lavender,
    onSecondary = SlateGray,

    tertiary = MediumOrchid,
    onTertiary = GhostWhite,

    background = MintCream,
    onBackground = MidnightBlue,

    surface = GhostWhite,
    onSurface = MidnightBlue,

    surfaceVariant = GhostWhite,
    onSurfaceVariant = SlateGray,

    )

@Composable
fun DayVeeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}