package com.example.dayvee

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dayvee.navigation.DayVeeNavHost
import com.example.dayvee.ui.theme.DayVeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestNotificationPermissionIfNeeded()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(TRANSPARENT),
        )
        setContent {
            val isDarkTheme = true
            DayVeeTheme(darkTheme = isDarkTheme) {
                val insetsController = WindowInsetsControllerCompat(window, window.decorView)
                insetsController.isAppearanceLightStatusBars = !isDarkTheme
                insetsController.isAppearanceLightNavigationBars = !isDarkTheme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DayVeeNavHost()
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    REQUEST_NOTIFICATIONS_PERMISSION
                )
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATIONS_PERMISSION = 1001
    }
}
