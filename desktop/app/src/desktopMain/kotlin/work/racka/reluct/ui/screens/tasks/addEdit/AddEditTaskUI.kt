package work.racka.reluct.ui.screens.tasks.addEdit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.common.model.states.tasks.AddEditTaskState
import work.racka.reluct.common.model.states.tasks.ModifyTaskState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.bottomSheet.addEditTask.LazyColumnAddEditTaskFields
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.dialogs.DiscardPromptDialog
import work.racka.reluct.compose.common.components.images.ImageWithDescription
import work.racka.reluct.compose.common.components.images.LottieAnimationWithDescription
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.compose.common.components.util.EditTitles
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes
import work.racka.reluct.ui.screens.tasks.components.CurrentTaskLabels
import work.racka.reluct.ui.screens.tasks.components.ManageTaskLabelsSheet
import work.racka.reluct.ui.screens.tasks.components.ModifyTaskLabel

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun AddEditTaskUI(
    snackbarState: SnackbarHostState,
    uiState: State<AddEditTaskState>,
    onSaveTask: () -> Unit,
    onAddTaskClicked: () -> Unit,
    onUpdateTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit,
    onModifyTaskLabel: (ModifyTaskLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val modifyTaskState = remember { derivedStateOf { uiState.value.modifyTaskState } }

    val labelsState = getLabelState(
        modifyTaskStateProvider = { modifyTaskState.value },
        availableLabelsProvider = { uiState.value.availableTaskLabels },
        onUpdateTask = onUpdateTask
    )

    val titles = getTitles(modifyTaskStateProvider = { modifyTaskState.value })

    val canGoBack by remember { derivedStateOf { modifyTaskState.value !is ModifyTaskState.Data } }
    val openDialog = remember { mutableStateOf(false) }

    // Call this when you trying to Go Back safely!
    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onBackClicked() else openDialog.value = true
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            ManageTaskLabelsSheet(
                modifier = Modifier,
                labelsState = labelsState.value,
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
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                ReluctSmallTopAppBar(
                    title = titles.value.appBarTitle,
                    navigationIcon = {
                        IconButton(onClick = { goBackAttempt(canGoBack) }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarState) { data ->
                    Snackbar(
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
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize()
            ) {
                AnimatedContent(
                    targetState = modifyTaskState.value,
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { targetState ->
                    when (targetState) {
                        is ModifyTaskState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is ModifyTaskState.Saved -> {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement
                                    .spacedBy(Dimens.MediumPadding.size)
                            ) {
                                LottieAnimationWithDescription(
                                    lottieResource = SharedRes.files.task_saved,
                                    imageSize = 300.dp,
                                    description = null,
                                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                                )
                                ReluctButton(
                                    buttonText = stringResource(SharedRes.strings.add_task_button_text),
                                    icon = Icons.Rounded.Add,
                                    shape = Shapes.large,
                                    buttonColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    onButtonClicked = onAddTaskClicked
                                )
                                OutlinedReluctButton(
                                    buttonText = stringResource(SharedRes.strings.exit_text),
                                    icon = Icons.Rounded.ArrowBack,
                                    shape = Shapes.large,
                                    borderColor = MaterialTheme.colorScheme.primary,
                                    onButtonClicked = onBackClicked
                                )
                            }
                        }
                        is ModifyTaskState.NotFound -> {
                            ImageWithDescription(
                                painter = painterResource(SharedRes.assets.no_tasks),
                                imageSize = 300.dp,
                                description = stringResource(SharedRes.strings.task_not_found_text),
                            )
                        }
                        else -> {}
                    }
                }

                EditTasksList(
                    getModifyTaskState = { modifyTaskState.value },
                    onUpdateTask = onUpdateTask,
                    onSaveTask = onSaveTask,
                    onGoBack = { goBackAttempt(canGoBack) },
                    onEditLabels = { scope.launch { modalSheetState.show() } }
                )
            }
        }
    }

    // Discard Dialog
    DiscardPromptDialog(
        dialogTitleProvider = { titles.value.dialogTitle },
        dialogTextProvider = { SharedRes.strings.discard_task_message.localized() },
        openDialog = openDialog,
        onClose = { openDialog.value = false },
        onPositiveClick = onBackClicked
    )
}

@Composable
private fun EditTasksList(
    getModifyTaskState: () -> ModifyTaskState,
    onUpdateTask: (task: EditTask) -> Unit,
    onSaveTask: () -> Unit,
    onGoBack: () -> Unit,
    onEditLabels: () -> Unit
) {
    val tasksState = getModifyTaskState()
    if (tasksState is ModifyTaskState.Data) {
        LazyColumnAddEditTaskFields(
            task = tasksState.task,
            saveButtonText = stringResource(SharedRes.strings.save_button_text),
            discardButtonText = stringResource(SharedRes.strings.discard_button_text),
            onSave = { onSaveTask() },
            onUpdateTask = onUpdateTask,
            onDiscard = onGoBack,
            onEditLabels = onEditLabels
        )
    }
}

@Composable
private fun getLabelState(
    modifyTaskStateProvider: () -> ModifyTaskState,
    availableLabelsProvider: () -> ImmutableList<TaskLabel>,
    onUpdateTask: (task: EditTask) -> Unit
) = remember {
    derivedStateOf {
        val tasksStats = modifyTaskStateProvider()
        val task = if (tasksStats is ModifyTaskState.Data) tasksStats.task else null
        CurrentTaskLabels(
            availableLabels = availableLabelsProvider(),
            selectedLabels = task?.taskLabels ?: persistentListOf(),
            onUpdateSelectedLabels = { labels ->
                task?.copy(taskLabels = labels)?.let(onUpdateTask)
            }
        )
    }
}

@Composable
private fun getTitles(
    modifyTaskStateProvider: () -> ModifyTaskState,
): State<EditTitles> = remember {
    derivedStateOf {
        when (val goalState = modifyTaskStateProvider()) {
            is ModifyTaskState.Data -> {
                if (goalState.isEdit) {
                    EditTitles(
                        appBarTitle = SharedRes.strings.edit_task_text.localized(),
                        dialogTitle = SharedRes.strings.discard_changes_text.localized()
                    )
                } else {
                    EditTitles(
                        appBarTitle = SharedRes.strings.add_task_text.localized(),
                        dialogTitle = SharedRes.strings.discard_task.localized()
                    )
                }
            }
            else -> EditTitles(
                appBarTitle = "• • •",
                dialogTitle = SharedRes.strings.discard_task.localized()
            )
        }
    }
}
