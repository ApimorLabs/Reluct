package work.racka.reluct.android.screens.tasks.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.task_entry.TaskDetailsHeading
import work.racka.reluct.android.compose.components.cards.task_entry.TaskInfoCard
import work.racka.reluct.android.compose.components.cards.task_label_entry.TaskLabelPill
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TaskDetailsState

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsUI(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState,
    uiState: TaskDetailsState,
    onEditTask: (task: Task) -> Unit = { },
    onDeleteTask: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onBackClicked: () -> Unit = { },
) {

    val listState = rememberLazyListState()

    val openDialog = remember { mutableStateOf(false) }

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
            val task = if (uiState is TaskDetailsState.Data) uiState.task else null
            task?.let {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailsBottomBar(
                        onEditTaskClicked = { onEditTask(it) },
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
            // Loading
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is TaskDetailsState.Loading,
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
            // Task Details
            if (uiState is TaskDetailsState.Data) {
                uiState.task?.let { task ->
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement
                            .spacedBy(Dimens.MediumPadding.size)
                    ) {
                        item {
                            TaskDetailsHeading(
                                modifier = Modifier.fillMaxWidth(),
                                text = task.title,
                                textStyle = MaterialTheme.typography.headlineMedium
                                    .copy(fontWeight = FontWeight.Medium),
                                isChecked = task.done,
                                onCheckedChange = { isDone ->
                                    onToggleTaskDone(isDone, task)
                                }
                            )
                        }

                        item {
                            Text(
                                text = task.description
                                    .ifBlank { stringResource(R.string.no_description_text) },
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalContentColor.current
                                    .copy(alpha = .8f)
                            )
                        }

                        // Task Labels
                        if (task.taskLabels.isNotEmpty()) {
                            item {
                                LazyRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement
                                        .spacedBy(Dimens.SmallPadding.size),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(task.taskLabels, key = { it.id }) { item ->
                                        TaskLabelPill(
                                            modifier = Modifier.clickable {
                                                // TODO: Show Info
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
                                task = task,
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
                            val task = if (uiState is TaskDetailsState.Data) uiState.task else null
                            task?.let { onDeleteTask(it) }
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