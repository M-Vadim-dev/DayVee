package com.example.dayvee.ui.screens.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dayvee.R
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.components.BottomBarCutoutShape
import com.example.dayvee.ui.components.CustomDatePicker
import com.example.dayvee.ui.components.CustomGradientBorderIcon
import com.example.dayvee.ui.theme.DarkSlateGray
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.GhostWhite
import com.example.dayvee.ui.theme.MediumOrchid
import com.example.dayvee.ui.theme.MediumPurple
import com.example.dayvee.ui.theme.MidnightBlue
import com.example.dayvee.ui.theme.Montserrat
import com.example.dayvee.ui.theme.SlateGray
import kotlinx.coroutines.delay
import java.time.LocalDate

//import com.example.dayvee.ui.screens.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
//    viewModel: HomeScreenViewModel = viewModel(),

) {
//    val uiState by viewModel.uiState.collectAsState()

    val isAnimated = remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val showSheet = remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isAnimated.value) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isAnimated.value) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "rotation"
    )
    LaunchedEffect(isAnimated.value) {
        if (isAnimated.value) {
            delay(300)
            isAnimated.value = false
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Good Morning, Vadim", style = TextStyle(
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ), color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MediumOrchid, contentColor = MidnightBlue
                            ) {
                                Text(text = "3")
                            }
                        }, modifier = Modifier
                            .padding(end = 16.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = GhostWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }, actions = {
                Box(
                    Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSlateGray)
                ) {
                    IconButton(
                        onClick = { }, modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = SlateGray
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }, floatingActionButton = {
        CustomGradientBorderIcon(
            gradientCircle = Brush.verticalGradient(
                colors = listOf(MediumPurple, MediumOrchid)
            ),
            gradientBorder = Brush.verticalGradient(
                colors = listOf(MediumPurple, DarkSlateGray)
            ),
            icon = Icons.Rounded.Add,
            contentDescription = null,
            iconSize = 32.dp,
            modifier = Modifier.offset(y = 44.dp),
            scale = scale,
            rotation = rotation,
            onClick = {
                isAnimated.value = true
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                navController.navigate(Screen.Task.route)
//                showSheet.value = true
            })
    }, floatingActionButtonPosition = FabPosition.Center, bottomBar = {
        HomeScreenBottomBar(
            onClickIcon = { navController.navigate(Screen.Task.route) })
    }) { padding ->
        HomeScreenContent(
            padding = padding,
            onEditTaskCLick = { navController.navigate(Screen.Task.route) }
        )
//        if (showSheet.value) {
//            ModalBottomSheet(
//                onDismissRequest = { showSheet.value = false },
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onSurface,
//                dragHandle = {
//                    Icon(
//                        imageVector = Icons.Rounded.KeyboardArrowUp,
//                        contentDescription = null,
//                        tint = Color.Gray,
//                        modifier = Modifier.size(25.dp)
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .systemBarsPadding()
//            ) {
//                AddTaskScreen(
//                    onDismiss = { showSheet.value = false }, navController = navController
//                )
//            }
//    }
    }
}

@Composable
fun HomeScreenBottomBar(
    onClickIcon: () -> Unit,
) {
    val fabDiameter = 56.dp
    val fabRadius = fabDiameter / 2

    val density = LocalDensity.current
    val fabRadiusPx = with(density) { fabRadius.toPx() }
    val cornerRadiusPx = with(density) { 24.dp.toPx() }

    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val cutoutCenterX = screenWidthPx / 2f


    val items = listOf("Home", "Search", "Stats", "Profile")
    val icons = listOf(
        Pair(Icons.Outlined.Home, Icons.Filled.Home),
        Pair(Icons.Outlined.Search, Icons.Filled.Search),
        Pair(Icons.Outlined.Info, Icons.Filled.Info),
        Pair(Icons.Outlined.Person, Icons.Filled.Person)
    )
    val selectedIndex = remember { mutableIntStateOf(0) }

    Surface(
        color = DarkSlateGray, shadowElevation = 8.dp, shape = BottomBarCutoutShape(
            cornerRadius = cornerRadiusPx,
            cutoutRadius = fabRadiusPx + 8f,
            cutoutCenterX = cutoutCenterX
        ), modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                (0..1).forEach { index ->
                    BottomBarItem(
                        iconPair = icons[index],
                        label = items[index],
                        selected = selectedIndex.intValue == index,
                        onClick = { selectedIndex.intValue = index })
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                (2..3).forEach { index ->
                    BottomBarItem(
                        iconPair = icons[index],
                        label = items[index],
                        selected = selectedIndex.intValue == index,
                        onClick = { selectedIndex.intValue = index })
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(
    iconPair: Pair<ImageVector, ImageVector>,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = if (selected) iconPair.second else iconPair.first
    val color by animateColorAsState(
        if (selected) MediumPurple else Color.LightGray, label = "iconColor"
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
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        AnimatedVisibility(visible = selected) {
            Text(
                text = label,
                color = color,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    onEditTaskCLick: () -> Unit = {},
    onDeleteTaskCLick: () -> Unit = {},
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val baseDate = LocalDate.now()

    val sampleTasks = listOf(
        "Проверить почту",
        "Встреча с командой",
        "Подготовить отчет",
        "Звонок клиенту",
        "Обновить документацию",
        "Планирование задач на неделю",
        "Тестирование нового функционала",
        "Рефакторинг кода",
        "Обсуждение дизайна"
    )

    val sampleDescriptions = listOf(
        "Проверить входящие письма и ответить на важные.",
        "Обсудить прогресс проекта с командой.",
        "Собрать данные и подготовить отчет по итогам недели.",
        "Позвонить клиенту и уточнить детали заказа.",
        "Обновить документацию проекта до актуального состояния.",
        "Распределить задачи на следующую неделю.",
        "Провести тестирование нового функционала на баги.",
        "Оптимизировать и улучшить качество кода.",
        "Обсудить с дизайнером новые макеты."
    )

    val tasksByDate = remember {
        (0..30).associate { offset ->
            val date = baseDate.plusDays(offset.toLong())
            date to sampleTasks.mapIndexed { index, task ->
                // Пара заголовок + описание по индексу
                task to sampleDescriptions.getOrElse(index) { "Описание отсутствует" }
            }
        }
    }

    val times = List(9) { index ->
        val hour = 8 + (index * 45) / 60
        val minute = (index * 45) % 60
        "%02d:%02d".format(hour, minute)
    }

    val completedTasks = 5

    val listState = rememberLazyListState()

    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index == totalItemsCount - 1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBlue)
            .padding(padding)
    ) {
        CustomDatePicker(
            selectedDate = selectedDate, onDateSelected = { selectedDate = it })

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                itemsIndexed(times) { index, time ->

                    val task = tasksByDate[selectedDate]?.getOrNull(index)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(60.dp)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            val topLineHeight = 20.dp
                            val textHeight = 25.dp
                            val bottomLineHeight = 80.dp - topLineHeight - textHeight

                            Canvas(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(topLineHeight)
                            ) {
                                drawLine(
                                    color = if (index <= completedTasks) MediumPurple else Color.Gray,
                                    start = Offset(size.width / 2, size.height),
                                    end = Offset(size.width / 2, 0f),
                                    strokeWidth = 4f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f), 0f
                                    )
                                )
                            }

                            Text(
                                text = time,
                                color = if (index < completedTasks) MediumPurple else Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.height(textHeight)
                            )


                            Canvas(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(bottomLineHeight)
                            ) {
                                drawLine(
                                    color = if (index < completedTasks - 1) MediumPurple else Color.Gray,
                                    start = Offset(size.width / 2, 0f),
                                    end = Offset(size.width / 2, size.height),
                                    strokeWidth = 4f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f), 0f
                                    )
                                )
                            }
                        }


                        if (task != null) {
                            SwipeTaskItem(
                                textTitle = task.first,
                                textDescription = task.second,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = if (index < times.lastIndex) 12.dp else 0.dp),
                                onEdit = onEditTaskCLick,
                                onDelete = onDeleteTaskCLick

                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                }

            }
            if (!isAtBottom) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent, MidnightBlue
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun SwipeTaskItem(
    textTitle: String,
    textDescription: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.3f },
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onDelete()
                    true
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onEdit()
                    false
                }

                else -> false
            }
        })

    SwipeToDismissBox(state = dismissState, modifier = modifier, backgroundContent = {
        val direction = dismissState.dismissDirection
        val progress = dismissState.progress

        if (direction == SwipeToDismissBoxValue.EndToStart) {
            val offsetAnim by animateFloatAsState(
                targetValue = progress, label = "offsetAnimation"
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3f)
                        .graphicsLayer {
                            translationX = (1f - offsetAnim) * 300f
                        }
                        .background(MediumOrchid), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = GhostWhite
                        )
                        Text(
                            text = "Редактировать",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .graphicsLayer {
                            translationX = (1f - offsetAnim) * 150f
                        }
                        .background(MediumPurple), contentAlignment = Alignment.CenterEnd) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = GhostWhite
                        )
                        Text(
                            text = "Удалить",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        } else if (direction == SwipeToDismissBoxValue.StartToEnd) {
            val alphaAnim by animateFloatAsState(
                targetValue = progress, label = "alphaStartToEnd"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = alphaAnim }
                    .background(MediumPurple, shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp), contentAlignment = Alignment.CenterStart) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = GhostWhite
                )
            }
        }
    }, content = {
        TaskItem(
            textTitle = textTitle,
            text = textDescription,
            isCompleted = false,
            onCheckClick = {})
    })
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreenContent() {
    DayVeeTheme(darkTheme = true) {
        HomeScreenContent(
            padding = PaddingValues(0.dp)
        )
    }
}
