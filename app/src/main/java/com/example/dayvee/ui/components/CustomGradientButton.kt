package com.example.dayvee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dayvee.ui.theme.DarkSlateGray
import com.example.dayvee.ui.theme.MediumOrchid
import com.example.dayvee.ui.theme.MediumPurple

@Composable
fun CustomGradientButton(
    modifier: Modifier = Modifier,
    text: String,
    gradient: Brush = Brush.horizontalGradient(colors = listOf(MediumOrchid, MediumPurple)),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {

    val buttonGradient: Brush =
        if (enabled) gradient else Brush.horizontalGradient(listOf(DarkSlateGray, DarkSlateGray))

    val textColor =
        if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .height(70.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(brush = buttonGradient)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
private fun CustomGradientButtonPreview() {
    CustomGradientButton(
        text = "Text",
        modifier = Modifier
            .height(30.dp)
            .width(120.dp),
        enabled = true,
        onClick = {},

        )
}
