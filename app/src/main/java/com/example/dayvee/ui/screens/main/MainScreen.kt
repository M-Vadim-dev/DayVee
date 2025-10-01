package com.example.dayvee.ui.screens.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dayvee.R
import com.example.dayvee.domain.model.Task
import com.example.dayvee.navigation.Screen
import com.example.dayvee.ui.components.CustomDatePicker
import com.example.dayvee.ui.components.CustomGradientBorderIcon
import com.example.dayvee.ui.components.bottomBar.CustomBottomBar
import com.example.dayvee.ui.components.bottomBar.bottomBarItems
import com.example.dayvee.ui.extensions.toColor
import com.example.dayvee.ui.screens.addTask.AddTaskScreen
import com.example.dayvee.ui.screens.addTask.AddTaskScreenViewModel
import com.example.dayvee.ui.theme.Gradients.verticalBlackOverlayGradient
import com.example.dayvee.ui.theme.Gradients.verticalDarkPurpleGradient
import com.example.dayvee.ui.theme.Gradients.verticalMidnightBlueGradient
import com.example.dayvee.ui.theme.Gradients.verticalPurpleGradient
import com.example.dayvee.ui.theme.Montserrat
import com.example.dayvee.utils.DateUtils.formatTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    addTaskScreenViewModel: AddTaskScreenViewModel = hiltViewModel(),
) {
    val uiState by mainScreenViewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val fabCenterX = remember { mutableFloatStateOf(0f) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var showAddTask by remember { mutableStateOf(false) }

    var isAnimated by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val density = LocalDensity.current

    val topBarOffset = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(0f) }

    LaunchedEffect(showAddTask) {
        val targetTopBar = if (showAddTask) with(density) { 14.dp.toPx() } else 0f
        val targetContent = if (showAddTask) -with(density) { 60.dp.toPx() } else 0f

        coroutineScope {
            launch {
                topBarOffset.animateTo(
                    targetValue = targetTopBar,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
            launch {
                contentOffset.animateTo(
                    targetValue = targetContent,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (isAnimated) 45f else 0f, animationSpec = tween()
    )

    Scaffold(containerColor = MaterialTheme.colorScheme.background, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(32.dp)
            )
        }
    }, topBar = {
        TopAppBar(
            modifier = Modifier.offset { IntOffset(0, topBarOffset.value.roundToInt()) },
            title = {
                MainTopBarTitle(
                    shouldHide = topBarOffset.value > 1f,
                    text = "${uiState.greeting}, ${uiState.activeUser?.username}",
                    badgeCount = uiState.tasks.size,
                )
            },
            actions = {
                Box(
                    Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable { },     //todo
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

    }, floatingActionButton = {
        CustomGradientBorderIcon(
            gradientCircle = verticalPurpleGradient,
            gradientBorder = verticalDarkPurpleGradient,
            icon = Icons.Rounded.Add,
            contentDescription = null,
            iconSize = 36.dp,
            modifier = Modifier
                .offset(y = 44.dp)
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.positionInParent()
                    val size = coordinates.size
                    fabCenterX.floatValue = position.x + size.width / 2f
                },
            rotation = rotation,
            onClick = {
                isAnimated = !isAnimated
                showAddTask = isAnimated
                addTaskScreenViewModel.resetUiState()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    }, floatingActionButtonPosition = FabPosition.Center, bottomBar = {
        CustomBottomBar(
            items = bottomBarItems,
            currentRoute = currentRoute.toString(),
            cutoutCenterX = fabCenterX.floatValue,
            onItemClick = { item ->
                if (item.route != currentRoute) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .offset { IntOffset(0, contentOffset.value.roundToInt()) }
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CustomDatePicker(
                selectedDate = uiState.selectedDate,
                currentMonth = uiState.currentMonth,
                isDatePickerVisible = uiState.isDatePickerVisible,
                isToday = uiState.isToday,
                isTasksDone = uiState.tasks.any { it.date != uiState.selectedDate && !it.isDone }, //todo
                hasTasks = uiState.tasks.any { it.date == uiState.selectedDate },
                onDateSelected = mainScreenViewModel::setSelectedDate,
                onMonthChange = mainScreenViewModel::setCurrentMonth,
                onShowPicker = { mainScreenViewModel.setDatePickerVisibility(true) },
                onDismissPicker = { mainScreenViewModel.setDatePickerVisibility(false) }
            )

            Box(modifier = Modifier.weight(1f)) {
                MainScreenContent(
                    tasks = uiState.tasks,
                    tasksProgress = uiState.tasksProgress,
                    onClickTask = { task ->
                        navController.navigate(Screen.Task.createRoute(task.id))
                    },
                    onMarkTaskDone = { task ->
                        mainScreenViewModel.markTaskDone(task.id)
                    },
                    onEditTaskClick = { task ->
                        mainScreenViewModel.onTaskSelected(task.id)
                        isAnimated = true
                        showAddTask = true
                    },
                    onDeleteTaskClick = mainScreenViewModel::deleteTask
                )
            }

            AnimatedVisibility(
                visible = showAddTask,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween()
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween()
                ),
            ) {
                AddTaskScreen(
                    onDismiss = {
                        isAnimated = false
                        showAddTask = false
                    },
                    snackBarHostState = snackBarHostState,
                )
            }
        }
    }
}

@Composable
private fun MainTopBarTitle(
    shouldHide: Boolean,
    text: String,
    badgeCount: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!shouldHide) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text, style = TextStyle(
                        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 16.sp
                    ), color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary
                ) {
                    Text(text = badgeCount.toString())
                }
            }, modifier = Modifier
                .padding(end = 16.dp)
                .size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MainScreenContent(
    tasks: List<Task>,
    tasksProgress: Map<Int, TaskProgressInfo>,
    onClickTask: (Task) -> Unit = {},
    onMarkTaskDone: (Task) -> Unit = {},
    onEditTaskClick: (Task) -> Unit = {},
    onDeleteTaskClick: (Task) -> Unit = {},
) {
    val listState = rememberLazyListState()

    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index == totalItemsCount - 1
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .background(verticalBlackOverlayGradient)
            )
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_tasks),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 6.dp),
                ) {
                    itemsIndexed(tasks) { index, task ->
                        val progressInfo = tasksProgress[task.id]
                        val progress = progressInfo?.progress ?: 0f
                        Log.w("!!!", "Task id=${task.id}, progress=$progress")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(50.dp)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                val lineColor = if (task.isDone) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSecondary

                                if (index != 0) {
                                    Canvas(
                                        modifier = Modifier
                                            .weight(1f)
                                            .width(4.dp)
                                    ) {
                                        drawLine(
                                            color = lineColor,
                                            start = Offset(size.width / 2, size.height),
                                            end = Offset(size.width / 2, 0f),
                                            strokeWidth = 4f,
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(12f, 8f), 2f
                                            )
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (task.isDone) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.task_alt),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                } else {
                                    Text(
                                        text = formatTime(task.startTime),
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                                        ),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                    )
//                                    Text(
//                                        text = formatTime(task.endTime),
//                                        color = MaterialTheme.colorScheme.onSecondary,
//                                        style = MaterialTheme.typography.labelSmall.copy(
//                                            fontWeight = FontWeight.SemiBold, fontSize = 14.sp
//                                        ),
//                                        textAlign = TextAlign.Center,
//                                        maxLines = 2,
//                                    )
                                }

                                if (index != tasks.lastIndex) {
                                    Canvas(
                                        modifier = Modifier
                                            .weight(1f)
                                            .width(4.dp)
                                    ) {
                                        drawLine(
                                            color = lineColor,
                                            start = Offset(size.width / 2, 0f),
                                            end = Offset(size.width / 2, size.height),
                                            strokeWidth = 4f,
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(12f, 8f), 2f
                                            )
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            SwipeTaskItem(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(6.dp),
                                textTitle = task.title,
                                textDescription = task.description,
                                priorityColor = task.priority.toColor(),
                                icon = task.icon,
                                onClick = { onClickTask(task) },
                                onProgressLongClick = { onMarkTaskDone(task) },
                                onEdit = { onEditTaskClick(task) },
                                onDelete = { onDeleteTaskClick(task) },
                                timeStart = formatTime(task.startTime),
                                timeEnd = formatTime(task.endTime),
                                isComplete = task.isDone,
                                progress = progress
                            )
                        }
                    }
                }
                if (!isAtBottom) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(brush = verticalMidnightBlueGradient)
                    )
                }
            }
        }

    }
}
