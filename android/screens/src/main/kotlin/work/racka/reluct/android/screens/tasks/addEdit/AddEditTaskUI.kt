package work.racka.reluct.android.screens.tasks.addEdit

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.CurrentTaskLabels
import work.racka.reluct.android.screens.tasks.components.ManageTaskLabelsSheet
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.common.model.states.tasks.AddEditTaskState
import work.racka.reluct.common.model.states.tasks.ModifyTaskState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.bottomSheet.addEditTask.LazyColumnAddEditTaskFields
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.dialogs.DiscardPromptDialog
import work.racka.reluct.compose.common.components.images.LottieAnimationWithDescription
import work.racka.reluct.compose.common.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.compose.common.components.util.EditTitles
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

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

    val context = LocalContext.current
    val titles = getTitles(modifyTaskStateProvider = { modifyTaskState.value }, context = context)

    val canGoBack by remember { derivedStateOf { modifyTaskState.value !is ModifyTaskState.Data } }
    val openDialog = remember { mutableStateOf(false) }

    // Call this when you trying to Go Back safely!
    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onBackClicked() else openDialog.value = true
    }
    BackPressHandler { goBackAttempt(canGoBack) }
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            ManageTaskLabelsSheet(
                modifier = Modifier.statusBarsPadding(),
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
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                ReluctSmallTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
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
                                    buttonText = stringResource(R.string.add_task_button_text),
                                    icon = Icons.Rounded.Add,
                                    shape = Shapes.large,
                                    buttonColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    onButtonClicked = onAddTaskClicked
                                )
                                OutlinedReluctButton(
                                    buttonText = stringResource(R.string.exit_text),
                                    icon = Icons.Rounded.ArrowBack,
                                    shape = Shapes.large,
                                    borderColor = MaterialTheme.colorScheme.primary,
                                    onButtonClicked = onBackClicked
                                )
                            }
                        }
                        is ModifyTaskState.NotFound -> {
                            LottieAnimationWithDescription(
                                lottieResource = SharedRes.files.no_data,
                                imageSize = 300.dp,
                                description = stringResource(R.string.task_not_found_text),
                                descriptionTextStyle = MaterialTheme.typography.bodyLarge
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
        dialogTextProvider = { context.getString(SharedRes.strings.discard_task_message.resourceId) },
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
            saveButtonText = stringResource(R.string.save_button_text),
            discardButtonText = stringResource(R.string.discard_button_text),
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
    context: Context
): State<EditTitles> = remember(context) {
    derivedStateOf {
        when (val goalState = modifyTaskStateProvider()) {
            is ModifyTaskState.Data -> {
                if (goalState.isEdit) {
                    EditTitles(
                        appBarTitle = context.getString(R.string.edit_task_text),
                        dialogTitle = context.getString(R.string.discard_changes_text)
                    )
                } else {
                    EditTitles(
                        appBarTitle = context.getString(R.string.add_task_text),
                        dialogTitle = context.getString(R.string.discard_task)
                    )
                }
            }
            else -> EditTitles(
                appBarTitle = "• • •",
                dialogTitle = context.getString(R.string.discard_task)
            )
        }
    }
}
