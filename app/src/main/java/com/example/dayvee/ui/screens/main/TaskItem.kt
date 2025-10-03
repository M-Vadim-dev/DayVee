package com.example.dayvee.ui.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dayvee.R
import com.example.dayvee.ui.components.CustomCircularProgress
import com.example.dayvee.ui.theme.CriticalRed
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.Montserrat

@Composable
fun TaskItem(
    textTitle: String,
    textDescription: String,
    imageVector: ImageVector? = null,
    iconPainter: Painter? = null,
    timeStart: String,
    timeEnd: String,
    colorLabel: Color = Color.Transparent,
    progress: Float,
    isCompleted: Boolean,
    onProgressClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clip(shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(
                        color = colorLabel,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {

            }

            Spacer(modifier = Modifier.width(8.dp))

            when {
                iconPainter != null -> Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    tint = colorLabel,
                    modifier = Modifier.size(38.dp)
                )
                imageVector != null -> Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = colorLabel,
                    modifier = Modifier.size(38.dp)
                )
                else -> Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_account_circle),
                    contentDescription = null,
                    tint = colorLabel,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = textTitle,
                    style = TextStyle(
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
//                Text(
//                    text = textDescription,
//                    style = TextStyle(
//                        fontFamily = Montserrat,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 12.sp,
//                    ),
//                    color = MaterialTheme.colorScheme.outline,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
//                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_month),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "$timeStart - $timeEnd",
                        style = TextStyle(
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                        ),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            CustomCircularProgress(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = { onProgressClick() }
                    ),
                progress = progress,
                size = 45.dp
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskItemPreview() {
    DayVeeTheme {
        TaskItem(
            textTitle = "Устренняя разминка",
            textDescription = "Описание задачи",
            timeStart = "09:00",
            timeEnd = "10:00",
            colorLabel = CriticalRed,
            progress = 0.7f,
            isCompleted = false,
            onProgressClick = {}
        )
    }
}