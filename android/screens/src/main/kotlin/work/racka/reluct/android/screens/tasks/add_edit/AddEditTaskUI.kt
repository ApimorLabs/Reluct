package work.racka.reluct.android.screens.tasks.add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task.AddEditTaskFields
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.states.tasks.TasksState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddEditTaskUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: TasksState,
    onSaveTask: (task: EditTask) -> Unit,
    onBackClicked: () -> Unit = { },
) {

    val titleText = when (uiState) {
        is TasksState.AddEditTask -> {
            if (uiState.task == null) stringResource(R.string.add_task_text)
            else stringResource(R.string.edit_task_text)
        }
        else -> stringResource(R.string.add_task_text)
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
                    IconButton(onClick = onBackClicked) {
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
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {
            // Loading
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is TasksState.Loading,
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
            if (uiState is TasksState.AddEditTask) {
                AddEditTaskFields(
                    editTask = uiState.task,
                    saveButtonText = stringResource(R.string.save_button_text),
                    discardButtonText = stringResource(R.string.discard_button_text),
                    onSave = { editTask -> onSaveTask(editTask) },
                    onDiscard = { }
                )
            }
        }
    }

}
