package com.example.dayvee.ui.screens.addTask

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dayvee.R
import com.example.dayvee.domain.model.TaskIcon
import com.example.dayvee.ui.common.TaskIcons.defaultIcons
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.ui.extensions.toColor
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.NoneTransparent

@Composable
fun TaskCustomizationSection(
    selectedPriority: TaskPriority,
    onPrioritySelected: (TaskPriority) -> Unit,
    selectedIcon: TaskIcon?,
    onIconSelected: (TaskIcon) -> Unit,
) {
    var showPriorityPicker by remember { mutableStateOf(false) }
    var showIconPicker by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        showPriorityPicker = !showPriorityPicker
                        showIconPicker = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_drop_fill),
                    contentDescription = null,
                    tint = selectedPriority.toColor(),
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = stringResource(R.string.text_color),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        showIconPicker = !showIconPicker
                        showPriorityPicker = false
                    },
                contentAlignment = Alignment.Center
            ) {
                selectedIcon?.let { icon ->
                    when (icon) {
                        is TaskIcon.Resource -> {
                            Icon(
                                painter = painterResource(id = icon.resId),
                                contentDescription = null,
                                tint = selectedPriority.toColor()
                            )
                        }

                        is TaskIcon.Custom -> {
                            // AsyncImage(model = icon.url, contentDescription = null) //todo
                        }

                        is TaskIcon.Default -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_assignment),
                                contentDescription = null,
                                tint = selectedPriority.toColor()
                            )
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.text_icon),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        AnimatedVisibility(
            visible = showPriorityPicker,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            PriorityColorPicker(
                selectedColorPriority = selectedPriority,
                onPrioritySelected = {
                    onPrioritySelected(it)
                    showPriorityPicker = false
                }
            )
        }

        AnimatedVisibility(
            visible = showIconPicker,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            IconPicker(
                selectedIcon = selectedIcon,
                onIconSelected = {
                    onIconSelected(it)
                    showIconPicker = false
                }
            )
        }

    }
}

@Composable
private fun IconPicker(
    selectedIcon: TaskIcon?,
    onIconSelected: (TaskIcon) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .heightIn(max = 100.dp)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            items(defaultIcons.size) { index ->
                val icon = defaultIcons[index]
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (icon == selectedIcon) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                        .clickable { onIconSelected(icon) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon.resId),
                        contentDescription = null,
                        tint = if (icon == selectedIcon) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityColorPicker(
    selectedColorPriority: TaskPriority,
    onPrioritySelected: (TaskPriority) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        TaskPriority.entries.forEach { priority ->
            val isSelected = priority == selectedColorPriority

            val animatedSize by animateDpAsState(if (isSelected) 34.dp else 28.dp)
            val iconSize by animateDpAsState(if (isSelected) 20.dp else 18.dp)

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(animatedSize)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        brush = SolidColor(if (isSelected) MaterialTheme.colorScheme.onPrimary else NoneTransparent),
                        shape = CircleShape
                    )
                    .background(priority.toColor())
                    .clickable { onPrioritySelected(priority) },
                contentAlignment = Alignment.Center
            ) {
                when {
                    priority == TaskPriority.NONE -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_nosign),
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    isSelected -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TaskCustomizationSectionPreview() {
    DayVeeTheme {
        var selectedPriority by remember { mutableStateOf(TaskPriority.CRITICAL) }
        var selectedIcon by remember { mutableStateOf<TaskIcon>(TaskIcon.Resource(R.drawable.ic_alarm)) }

        TaskCustomizationSection(
            selectedPriority = selectedPriority,
            onPrioritySelected = { selectedPriority = it },
            selectedIcon = selectedIcon,
            onIconSelected = { selectedIcon = it }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PriorityColorPickerPreview() {
    DayVeeTheme {
        PriorityColorPicker(
            selectedColorPriority = TaskPriority.HIGH,
            onPrioritySelected = {}
        )
    }
}

@Preview()
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun IconPickerPreview() {
    DayVeeTheme {
        IconPicker(
            selectedIcon = TaskIcon.Resource(R.drawable.ic_info),
            onIconSelected = { R.drawable.ic_info }
        )
    }
}
