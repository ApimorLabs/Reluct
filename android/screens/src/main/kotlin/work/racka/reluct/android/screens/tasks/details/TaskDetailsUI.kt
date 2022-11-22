package work.racka.reluct.android.screens.tasks.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.CurrentTaskLabels
import work.racka.reluct.android.screens.tasks.components.ManageTaskLabelsSheet
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.android.screens.tasks.components.TaskLabelsPage
import work.racka.reluct.common.features.tasks.states.TaskDetailsState
import work.racka.reluct.common.features.tasks.states.TaskState
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.cards.taskEntry.TaskDetailsHeading
import work.racka.reluct.compose.common.components.cards.taskEntry.TaskInfoCard
import work.racka.reluct.compose.common.components.cards.taskLabelEntry.TaskLabelPill
import work.racka.reluct.compose.common.components.cards.taskLabelEntry.TaskLabelsEntryMode
import work.racka.reluct.compose.common.components.images.LottieAnimationWithDescription
import work.racka.reluct.compose.common.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun TaskDetailsUI(
    snackbarState: SnackbarHostState,
    uiState: State<TaskDetailsState>,
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

    val taskState = remember { derivedStateOf { uiState.value.taskState } }
    val labelsState by getLabelState(availableLabelsProvider = { uiState.value.availableTaskLabels })
    val taskLabelsPage = remember { mutableStateOf<TaskLabelsPage>(TaskLabelsPage.ShowLabels) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            ManageTaskLabelsSheet(
                modifier = Modifier.statusBarsPadding(),
                entryMode = TaskLabelsEntryMode.ViewLabels,
                startPage = taskLabelsPage.value,
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
                when (val state = taskState.value) {
                    is TaskState.Data -> {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            DetailsBottomBar(
                                onEditTaskClicked = { onEditTask(state.task) },
                                onDeleteTaskClicked = { openDialog.value = true }
                            )
                        }
                    }
                    else -> {}
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
                    targetState = taskState.value,
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
                                lottieResource = SharedRes.files.no_data,
                                imageSize = 300.dp,
                                description = stringResource(R.string.task_not_found_text),
                                descriptionTextStyle = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                DetailsLazyColumn(
                    stateProvider = { taskState.value },
                    listState = listState,
                    onToggleTaskDone = onToggleTaskDone,
                    onUpdateTaskLabelsPage = { taskLabelsPage.value = it },
                    onShowBottomSheet = { scope.launch { modalSheetState.show() } }
                )
            }
        }

        // Delete Task Dialog
        DeleteTaskDialog(
            openDialog = openDialog,
            onClose = { openDialog.value = false },
            getCurrentTask = {
                when (val state = taskState.value) {
                    is TaskState.Data -> state.task
                    else -> null
                }
            },
            onDeleteTask = onDeleteTask
        )
    }
}

@Composable
private fun DetailsLazyColumn(
    stateProvider: () -> TaskState,
    listState: LazyListState,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onUpdateTaskLabelsPage: (TaskLabelsPage) -> Unit,
    onShowBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val providedState = remember { derivedStateOf { stateProvider() } }
    when (val state = providedState.value) {
        is TaskState.Data ->
            LazyColumn(
                modifier = modifier,
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.MediumPadding.size)
            ) {
                item {
                    TaskDetailsHeading(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.task.title,
                        textStyle = MaterialTheme.typography.headlineMedium
                            .copy(fontWeight = FontWeight.Medium),
                        isChecked = state.task.done,
                        onCheckedChange = { isDone ->
                            onToggleTaskDone(isDone, state.task)
                        }
                    )
                }

                item {
                    Text(
                        text = state.task.description
                            .ifBlank { stringResource(R.string.no_description_text) },
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalContentColor.current
                            .copy(alpha = .8f)
                    )
                }

                // Task Labels
                if (state.task.taskLabels.isNotEmpty()) {
                    item {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement
                                .spacedBy(Dimens.SmallPadding.size),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(
                                state.task.taskLabels,
                                key = { it.id }
                            ) { item ->
                                TaskLabelPill(
                                    modifier = Modifier.clickable {
                                        onUpdateTaskLabelsPage(TaskLabelsPage.ModifyLabel(item))
                                        onShowBottomSheet()
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
                        task = state.task,
                        shape = Shapes.large
                    )
                }

                // Bottom Space
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        else -> {}
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
private fun DeleteTaskDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    getCurrentTask: () -> Task?,
    onDeleteTask: (task: Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (openDialog.value) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onClose,
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
                        onClose()
                        getCurrentTask()?.run(onDeleteTask)
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
                    onButtonClicked = onClose
                )
            }
        )
    }
}

@Composable
private fun getLabelState(availableLabelsProvider: () -> ImmutableList<TaskLabel>) =
    remember {
        derivedStateOf {
            CurrentTaskLabels(
                availableLabels = availableLabelsProvider(),
                selectedLabels = persistentListOf(),
                onUpdateSelectedLabels = {}
            )
        }
    }
