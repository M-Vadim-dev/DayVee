package com.example.dayvee.ui.screens.intro

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dayvee.R
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.components.CustomGradientButton
import com.example.dayvee.ui.theme.DayVeeTheme

@Composable
fun IntroScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.image),
            contentDescription = null,
            modifier = Modifier
                .height(300.dp)
                .padding(top = 8.dp)
        )
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            maxLines = 3,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.intro_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        CustomGradientButton(
            onClick = { navController.navigate(Screen.Main.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = stringResource(R.string.get_started)
        )

        Button(
            onClick = { navController.navigate(Screen.Main.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun IntroScreenPreview() {
    val fakeNavController = rememberNavControllerFake()

    DayVeeTheme {
        IntroScreen(navController = fakeNavController)
    }
}

@Composable
private fun rememberNavControllerFake(): NavHostController = rememberNavController()
