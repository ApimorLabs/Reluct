package work.racka.reluct.android.screens.tasks.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
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
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TaskDetailsState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskDetailsUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: TaskDetailsState,
    onEditTask: (task: Task) -> Unit = { },
    onDeleteTask: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onBackClicked: () -> Unit = { },
) {

    val listState = rememberLazyListState()

    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
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
                DetailsBottomBar(
                    onEditTaskClicked = { onEditTask(it) },
                    onDeleteTaskClicked = { openDialog.value = true }
                )
            }
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
                if (uiState.task != null) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement
                            .spacedBy(Dimens.MediumPadding.size)
                    ) {
                        item {
                            TaskDetailsHeading(
                                modifier = Modifier.fillMaxWidth(),
                                text = uiState.task!!.title,
                                textStyle = MaterialTheme.typography.headlineMedium
                                    .copy(fontWeight = FontWeight.Medium),
                                isChecked = uiState.task!!.done,
                                onCheckedChange = { isDone ->
                                    onToggleTaskDone(isDone, uiState.task!!)
                                }
                            )
                        }

                        item {
                            Text(
                                text = uiState.task!!.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalContentColor.current
                                    .copy(alpha = .8f)
                            )
                        }

                        item {
                            TaskInfoCard(
                                task = uiState.task!!,
                                shape = Shapes.large
                            )
                        }

                        // Bottom Space
                        item {
                            Spacer(
                                modifier = Modifier
                                    .height(innerPadding.calculateBottomPadding())
                            )
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