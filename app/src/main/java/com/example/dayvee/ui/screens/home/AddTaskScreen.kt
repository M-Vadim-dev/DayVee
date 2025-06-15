package com.example.dayvee.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.components.CustomGradientButton
import com.example.dayvee.ui.screens.rememberNavControllerFake
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.SlateGray
import kotlinx.coroutines.launch

@Composable
fun AddTaskScreen(onDismiss: () -> Unit, navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Select Date") }
    var time by remember { mutableStateOf("Select Time") }

    // Picker options
    val days = (1..31).map { it.toString().padStart(2, '0') }
    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59 step 5).map { it.toString().padStart(2, '0') }

    var selectedDay by remember { mutableIntStateOf(0) }
    var selectedHour by remember { mutableIntStateOf(12) }
    var selectedMinute by remember { mutableIntStateOf(6) }

    val titleInteraction = remember { MutableInteractionSource() }
    val isTitleFocused by titleInteraction.collectIsFocusedAsState()
    val titleLabelColor = if (isTitleFocused) MaterialTheme.colorScheme.primary else SlateGray

    val descInteraction = remember { MutableInteractionSource() }
    val isDescFocused by descInteraction.collectIsFocusedAsState()
    val descLabelColor = if (isDescFocused) MaterialTheme.colorScheme.primary else SlateGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text(text = "Title", color = titleLabelColor)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            interactionSource = titleInteraction
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            ScrollPickerColumn(
                items = days,
                selectedIndex = selectedDay,
                onSelected = { selectedDay = it },
            )
            ScrollPickerColumn(
                items = hours,
                selectedIndex = selectedHour,
                onSelected = { selectedHour = it },
            )
            ScrollPickerColumn(
                items = minutes,
                selectedIndex = selectedMinute,
                onSelected = { selectedMinute = it },
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description, onValueChange = { description = it }, label = {
            Text(
                text = "Description", color = descLabelColor
            )
        }, modifier = Modifier.fillMaxWidth(), maxLines = 2, interactionSource = descInteraction
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomGradientButton(
            onClick = {
                navController.navigate(Screen.Home.route)
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(40.dp),
            text = "Add Task",
        )
    }
}

@Composable
fun ScrollPickerColumn(
    items: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    // Снаппер (аналог ViewPager-like snapping)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centeredItem =
                listState.firstVisibleItemIndex + if (listState.firstVisibleItemScrollOffset > 50) 1 else 0
            coroutineScope.launch {
                listState.animateScrollToItem(centeredItem)
                onSelected(centeredItem.coerceIn(0, items.lastIndex))
            }
        }
    }

    Box(
        modifier = Modifier
            .height(120.dp) // Высота всей колонны
            .width(60.dp)   // Ширина одной колонки
            .clip(RoundedCornerShape(8.dp))
    ) {
        HorizontalDivider()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 40.dp), // Отступы сверху и снизу
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .height(40.dp) // Высота одного элемента
                        .fillMaxWidth(),
                    color = if (index == selectedIndex) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Массируем центр — можно линию или эффект затемнения
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddTaskScreenPreview() {

    DayVeeTheme(darkTheme = true) {
        AddTaskScreen(
            navController = rememberNavControllerFake(), onDismiss = {})
    }
}