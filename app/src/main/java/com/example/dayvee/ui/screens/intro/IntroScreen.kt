package com.example.dayvee.ui.screens.intro

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dayvee.R
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.components.CustomGradientButton
import com.example.dayvee.ui.theme.DayVeeTheme


@Composable
fun IntroScreen(
    navController: NavHostController,
    viewModel: IntroScreenViewModel = hiltViewModel(),
) {
    IntroScreenContent(
        onGetStartedClick = {
            viewModel.setLaunched()
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        },
        onSignInClick = {
            viewModel.setLaunched()
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        }
    )
}

data class IntroPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun IntroScreenContent(
    onGetStartedClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    val pages = listOf(
        IntroPage(
            R.drawable.image,
            stringResource(R.string.app_title),
            stringResource(R.string.intro_text)
        ),
        IntroPage(
            R.drawable.image,
            "Another Title",
            "Description for second screen..."
        ),
        IntroPage(
            R.drawable.image,
            "Third Title",
            "More info here..."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            IntroPageItem(page = pages[page])
        }
        PagerDotsIndicator(
            totalPages = pages.size,
            currentPage = pagerState.currentPage
        )
        CustomGradientButton(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = stringResource(R.string.get_started)
        )
        Button(
            onClick = onSignInClick,
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

@Composable
private fun IntroPageItem(page: IntroPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 280.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = page.title,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            maxLines = 3
        )
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            maxLines = 6
        )
    }
}

@Composable
private fun PagerDotsIndicator(
    totalPages: Int,
    currentPage: Int,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
    selectedSize: Dp = 12.dp,
    unSelectedSize: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .padding(spacing)
                    .size(if (currentPage == index) selectedSize else unSelectedSize)
                    .clip(CircleShape)
                    .background(if (currentPage == index) selectedColor else unSelectedColor)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ru")
@Composable
private fun IntroScreenPreview() {
    DayVeeTheme {
        IntroScreenContent(
            onGetStartedClick = {},
            onSignInClick = {}
        )
    }
}
