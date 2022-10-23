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
import androidx.compose.runtime.*
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
import work.racka.reluct.common.model.states.tasks.ModifyTaskState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddEditTaskUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: ModifyTaskState,
    onSaveTask: () -> Unit,
    onAddTaskClicked: () -> Unit,
    onUpdateTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit
) {

    val titleText = when (uiState) {
        is ModifyTaskState.Data -> {
            if (uiState.isEdit) stringResource(R.string.edit_task_text)
            else stringResource(R.string.add_task_text)
        }
        else -> "• • •"
    }

    val canGoBack by remember(uiState) {
        derivedStateOf {
            uiState !is ModifyTaskState.Data
        }
    }
    val taskSaved = remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    fun goBackAttempt() {
        if (canGoBack) onBackClicked()
        else openDialog = true
    }

    BackPressHandler { goBackAttempt() }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
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
                visible = uiState is ModifyTaskState.Loading,
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
                visible = uiState is ModifyTaskState.Data,
                enter = fadeIn(),
                exit = scaleOut()
            ) {
                if (uiState is ModifyTaskState.Data) {
                    LazyColumnAddEditTaskFields(
                        task = uiState.task,
                        saveButtonText = stringResource(R.string.save_button_text),
                        discardButtonText = stringResource(R.string.discard_button_text),
                        onSave = { onSaveTask() },
                        onUpdateTask = onUpdateTask,
                        onDiscard = { goBackAttempt() }
                    )
                }
            }

            // Task Saved
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is ModifyTaskState.Saved,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
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

            // Task Not Found
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is ModifyTaskState.NotFound,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationWithDescription(
                        lottieResId = R.raw.no_data,
                        imageSize = 300.dp,
                        description = stringResource(R.string.task_not_found_text)
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
