package com.example.dayvee.ui.components.bottomBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

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

    val leftItems = items.take(2)
    val rightItems = items.takeLast(2)

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
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                leftItems.forEach { item ->
                    BottomBarItem(
                        data = item,
                        selected = item.route == currentRoute,
                        onClick = { onItemClick(item) },
                        textFirst = false
                    )
                }
            }

            Spacer(modifier = Modifier.width(fabDiameter + 24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rightItems.forEach { item ->
                    BottomBarItem(
                        data = item,
                        selected = item.route == currentRoute,
                        onClick = { onItemClick(item) },
                        textFirst = true
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(
    data: BottomBarItemRes,
    selected: Boolean,
    onClick: () -> Unit,
    textFirst: Boolean
) {
    val icon = if (selected) data.iconFilledRes else data.iconOutlinedRes

    val color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
        label = "BottomBarItemColor"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        if (textFirst) {
            AnimatedVisibility(visible = selected) {
                Text(
                    text = stringResource(id = data.labelRes),
                    color = color,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(id = data.labelRes),
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(id = data.labelRes),
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = stringResource(id = data.labelRes),
                    color = color,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}
