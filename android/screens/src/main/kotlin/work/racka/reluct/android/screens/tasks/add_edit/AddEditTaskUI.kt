package work.racka.reluct.android.screens.tasks.add_edit

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task.LazyColumnAddEditTaskFields
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.CurrentTaskLabels
import work.racka.reluct.android.screens.tasks.components.ManageTaskLabelsSheet
import work.racka.reluct.android.screens.tasks.components.ModifyTaskLabel
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.common.model.states.tasks.AddEditTaskState
import work.racka.reluct.common.model.states.tasks.ModifyTaskState

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun AddEditTaskUI(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState,
    uiState: AddEditTaskState,
    onSaveTask: () -> Unit,
    onAddTaskClicked: () -> Unit,
    onUpdateTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit,
    onModifyTaskLabel: (ModifyTaskLabel) -> Unit
) {

    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val modifyTaskState = uiState.modifyTaskState

    val labelsState by getLabelState(modifyTaskState, uiState.availableTaskLabels, onUpdateTask)

    val (titleText, dialogTitle) = getTitles(modifyTaskState = modifyTaskState)

    val canGoBack by remember(modifyTaskState) {
        derivedStateOf {
            modifyTaskState !is ModifyTaskState.Data
        }
    }
    var openDialog by remember { mutableStateOf(false) }

    fun goBackAttempt() {
        if (canGoBack) onBackClicked()
        else openDialog = true
    }

    BackPressHandler { goBackAttempt() }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            ManageTaskLabelsSheet(
                modifier = Modifier.statusBarsPadding(),
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
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                ReluctSmallTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = titleText,
                    navigationIcon = {
                        IconButton(onClick = { goBackAttempt() }) {
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
                    targetState = modifyTaskState,
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
                                    lottieResId = R.raw.task_saved,
                                    imageSize = 300.dp,
                                    description = null
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
                                lottieResId = R.raw.no_data,
                                imageSize = 300.dp,
                                description = stringResource(R.string.task_not_found_text)
                            )
                        }
                        else -> {}
                    }
                }

                if (modifyTaskState is ModifyTaskState.Data) {
                    LazyColumnAddEditTaskFields(
                        task = modifyTaskState.task,
                        saveButtonText = stringResource(R.string.save_button_text),
                        discardButtonText = stringResource(R.string.discard_button_text),
                        onSave = { onSaveTask() },
                        onUpdateTask = onUpdateTask,
                        onDiscard = { goBackAttempt() },
                        onEditLabels = { scope.launch { modalSheetState.show() } }
                    )
                }
            }
        }
    }


    // Discard Dialog
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = stringResource(R.string.discard_task_message))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        openDialog = false
                        onBackClicked()
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
                    onButtonClicked = { openDialog = false }
                )
            }
        )
    }
}

@Composable
private fun getLabelState(
    modifyTaskState: ModifyTaskState,
    availableLabels: ImmutableList<TaskLabel>,
    onUpdateTask: (task: EditTask) -> Unit
) = remember(modifyTaskState, availableLabels) {
    derivedStateOf {
        val task = if (modifyTaskState is ModifyTaskState.Data) modifyTaskState.task else null
        CurrentTaskLabels(
            availableLabels = availableLabels,
            selectedLabels = task?.taskLabels ?: persistentListOf(),
            onUpdateSelectedLabels = { labels ->
                task?.copy(taskLabels = labels)?.let(onUpdateTask)
            }
        )
    }
}

typealias ScreenTitle = String
typealias DialogTitle = String

@Composable
private fun getTitles(
    modifyTaskState: ModifyTaskState
): Pair<ScreenTitle, DialogTitle> = when (modifyTaskState) {
    is ModifyTaskState.Data -> {
        if (modifyTaskState.isEdit) {
            stringResource(R.string.edit_task_text) to stringResource(R.string.discard_changes_text)
        } else stringResource(R.string.add_task_text) to stringResource(R.string.discard_task)
    }
    else -> "• • •" to stringResource(R.string.discard_task)
}

