package work.racka.reluct.android.screens.goals.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.bottom_sheet.TopSheetSection
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalEntry
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.goals.active.states.DefaultGoals

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NewGoalSheet(
    modifier: Modifier = Modifier,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onCloseSheet: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
        ) {
            stickyHeader {
                TopSheetSection(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    sheetTitle = stringResource(id = R.string.new_goal_text),
                    onCloseClicked = onCloseSheet
                )
            }

            item {
                ReluctButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = stringResource(R.string.new_goal_text),
                    icon = Icons.Rounded.Add,
                    onButtonClicked = { onAddGoal(null) }
                )
            }

            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.examples_text),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // NOTE: Using the LazyList itemsIndexed won't give you the correct index from the list
            DefaultGoals.predefined().forEachIndexed { index, goal ->
                item {
                    GoalEntry(goal = goal, onEntryClick = { onAddGoal(index) })
                }
            }
        }
    }
}