package work.racka.reluct.android.screens.tasks.add_edit

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task.LazyColumnAddEditTaskFields
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.AddEditTasksState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddEditTaskUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: AddEditTasksState,
    onSaveTask: () -> Unit,
    onAddTaskClicked: () -> Unit,
    onUpdateTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit
) {

    val titleText = when (uiState) {
        is AddEditTasksState.Data -> {
            if (uiState.task == null) stringResource(R.string.add_task_text)
            else stringResource(R.string.edit_task_text)
        }
        else -> stringResource(R.string.add_task_text)
    }

    val taskSaved = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    BackPressHandler {
        if (taskSaved.value) onBackClicked()
        else openDialog.value = true
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = titleText,
                navigationIcon = {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
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
            // Loading
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is AddEditTasksState.Loading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Add or Edit Task
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is AddEditTasksState.Data,
                enter = fadeIn(),
                exit = scaleOut()
            ) {
                if (uiState is AddEditTasksState.Data) {
                    LazyColumnAddEditTaskFields(
                        editTask = uiState.task,
                        saveButtonText = stringResource(R.string.save_button_text),
                        discardButtonText = stringResource(R.string.discard_button_text),
                        onSave = { editTask -> onSaveTask(editTask) },
                        onDiscard = { openDialog.value = true }
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is AddEditTasksState.TaskSaved,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                taskSaved.value = true
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
                        buttonText = stringResource(R.string.go_back_button_text),
                        icon = Icons.Rounded.ArrowBack,
                        shape = Shapes.large,
                        borderColor = MaterialTheme.colorScheme.primary,
                        onButtonClicked = onBackClicked
                    )
                }
            }
        }

        // Discard Dialog
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(text = stringResource(R.string.discard_task))
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
                            openDialog.value = false
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
                        onButtonClicked = { openDialog.value = false }
                    )
                }
            )
        }
    }
}
