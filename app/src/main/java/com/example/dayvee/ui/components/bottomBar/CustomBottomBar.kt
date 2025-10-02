package com.example.dayvee.ui.components.bottomBar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.theme.DayVeeTheme

@Composable
fun CustomBottomBar(
    items: List<BottomBarItemRes>,
    currentRoute: String,
    onItemClick: (BottomBarItemRes) -> Unit,
    cutoutCenterX: Float
) {
    val fabDiameter = 56.dp
    val fabRadius = fabDiameter / 2

    val density = LocalDensity.current
    val fabRadiusPx = with(density) { fabRadius.toPx() }
    val cornerRadiusPx = with(density) { 24.dp.toPx() }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 8.dp,
        shape = BottomBarCutoutShape(
            cornerRadius = cornerRadiusPx,
            cutoutRadius = fabRadiusPx + 8f,
            cutoutCenterX = cutoutCenterX,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items.take(2).forEach { item ->
                    BottomBarIcon(item, currentRoute, onItemClick)
                }
            }

            Spacer(modifier = Modifier.width(fabDiameter + 24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items.takeLast(2).forEach { item ->
                    BottomBarIcon(item, currentRoute, onItemClick)
                }
            }
        }
    }
}


@Composable
private fun BottomBarIcon(
    item: BottomBarItemRes,
    currentRoute: String,
    onItemClick: (BottomBarItemRes) -> Unit
) {
    IconButton(onClick = { onItemClick(item) }) {
        Icon(
            painter = painterResource(
                if (item.route == currentRoute) item.iconFilledRes
                else item.iconOutlinedRes
            ),
            contentDescription = stringResource(item.labelRes),
            tint = if (item.route == currentRoute)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CustomBottomBarPreview() {
    DayVeeTheme {
        CustomBottomBar(
            items = bottomBarItems,
            currentRoute = Screen.Stats.route,
            onItemClick = {},
            cutoutCenterX = 550f
        )
    }
}
