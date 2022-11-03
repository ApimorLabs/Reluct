package work.racka.reluct.android.screens.tasks.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.taskEntry.TaskDetailsHeading
import work.racka.reluct.android.compose.components.cards.taskEntry.TaskInfoCard
import work.racka.reluct.android.compose.components.cards.taskLabelEntry.TaskLabelPill
import work.racka.reluct.android.compose.components.cards.taskLabelEntry.TaskLabelsEntryMode
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.CurrentTaskLabels
import work.racka.reluct.android.screens.tasks.components.ManageTaskLabelsSheet
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.android.screens.tasks.components.TaskLabelsPage
import work.racka.reluct.common.features.tasks.states.TaskDetailsState
import work.racka.reluct.common.features.tasks.states.TaskState
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TaskLabel

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun TaskDetailsUI(
    snackbarState: SnackbarHostState,
    uiState: TaskDetailsState,
    onEditTask: (task: Task) -> Unit,
    onDeleteTask: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onBackClicked: () -> Unit,
    onModifyTaskLabel: (ModifyTaskLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }

    val taskState = uiState.taskState
    val labelsState by getLabelState(availableLabels = uiState.availableTaskLabels)
    var taskLabelsPage: TaskLabelsPage by remember { mutableStateOf(TaskLabelsPage.ShowLabels) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            ManageTaskLabelsSheet(
                modifier = Modifier.statusBarsPadding(),
                entryMode = TaskLabelsEntryMode.ViewLabels,
                startPage = taskLabelsPage,
                labelsState = labelsState,
                onSaveLabel = { onModifyTaskLabel(ModifyTaskLabel.SaveLabel(it)) },
                onDeleteLabel = { onModifyTaskLabel(ModifyTaskLabel.Delete(it)) },
                onClose = { scope.launch { modalSheetState.hide() } }
            )
        },
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                ReluctSmallTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = stringResource(R.string.task_details_text),
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (taskState is TaskState.Data) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        DetailsBottomBar(
                            onEditTaskClicked = { onEditTask(taskState.task) },
                            onDeleteTaskClicked = { openDialog.value = true }
                        )
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarState) { data ->
                    Snackbar(
                        modifier = Modifier.navigationBarsPadding(),
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        actionColor = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .navigationBarsPadding()
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize()
            ) {
                AnimatedContent(
                    targetState = taskState,
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { targetState ->
                    when (targetState) {
                        is TaskState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is TaskState.Data -> {}
                        else -> {
                            LottieAnimationWithDescription(
                                lottieResId = R.raw.no_data,
                                imageSize = 300.dp,
                                description = stringResource(R.string.task_not_found_text)
                            )
                        }
                    }
                }

                if (taskState is TaskState.Data) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement
                            .spacedBy(Dimens.MediumPadding.size)
                    ) {
                        item {
                            TaskDetailsHeading(
                                modifier = Modifier.fillMaxWidth(),
                                text = taskState.task.title,
                                textStyle = MaterialTheme.typography.headlineMedium
                                    .copy(fontWeight = FontWeight.Medium),
                                isChecked = taskState.task.done,
                                onCheckedChange = { isDone ->
                                    onToggleTaskDone(isDone, taskState.task)
                                }
                            )
                        }

                        item {
                            Text(
                                text = taskState.task.description
                                    .ifBlank { stringResource(R.string.no_description_text) },
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalContentColor.current
                                    .copy(alpha = .8f)
                            )
                        }

                        // Task Labels
                        if (taskState.task.taskLabels.isNotEmpty()) {
                            item {
                                LazyRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement
                                        .spacedBy(Dimens.SmallPadding.size),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(
                                        taskState.task.taskLabels,
                                        key = { it.id }
                                    ) { item ->
                                        TaskLabelPill(
                                            modifier = Modifier.clickable {
                                                scope.launch {
                                                    taskLabelsPage =
                                                        TaskLabelsPage.ModifyLabel(item)
                                                    modalSheetState.show()
                                                }
                                            },
                                            name = item.name,
                                            colorHex = item.colorHexString
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            TaskInfoCard(
                                task = taskState.task,
                                shape = Shapes.large
                            )
                        }

                        // Bottom Space
                        item {
                            Spacer(modifier = Modifier.navigationBarsPadding())
                        }
                    }
                }
            }
        }

        // Delete Task Dialog
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(text = stringResource(R.string.delete_task))
                },
                text = {
                    Text(text = stringResource(R.string.delete_task_message))
                },
                confirmButton = {
                    ReluctButton(
                        buttonText = stringResource(R.string.ok),
                        icon = null,
                        shape = Shapes.large,
                        buttonColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onButtonClicked = {
                            openDialog.value = false
                            if (taskState is TaskState.Data) taskState.task.run(onDeleteTask)
                        }
                    )
                },
                dismissButton = {
                    ReluctButton(
                        buttonText = stringResource(R.string.cancel),
                        icon = null,
                        shape = Shapes.large,
                        buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onButtonClicked = { openDialog.value = false }
                    )
                }
            )
        }
    }
}

@Composable
private fun DetailsBottomBar(
    onEditTaskClicked: () -> Unit,
    onDeleteTaskClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.MediumPadding.size)
            .padding(bottom = Dimens.MediumPadding.size)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.MediumPadding.size)
    ) {
        ReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(R.string.edit_button_text),
            icon = Icons.Rounded.Edit,
            onButtonClicked = onEditTaskClicked,
            buttonColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = Shapes.large
        )

        OutlinedReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(R.string.delete_button_text),
            icon = Icons.Rounded.Delete,
            onButtonClicked = onDeleteTaskClicked,
            borderColor = MaterialTheme.colorScheme.primary,
            shape = Shapes.large
        )
    }
}

@Composable
private fun getLabelState(availableLabels: ImmutableList<TaskLabel>) = remember(availableLabels) {
    derivedStateOf {
        CurrentTaskLabels(
            availableLabels = availableLabels,
            selectedLabels = persistentListOf(),
            onUpdateSelectedLabels = {}
        )
    }
}
