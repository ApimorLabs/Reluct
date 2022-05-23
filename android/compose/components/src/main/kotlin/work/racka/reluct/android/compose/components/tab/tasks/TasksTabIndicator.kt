package work.racka.reluct.android.compose.components.tab.tasks

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import work.racka.reluct.android.compose.destinations.TasksDestinations

@Composable
internal fun TasksTabIndicator(
    modifier: Modifier = Modifier,
    tabPositions: List<TabPosition>,
    tabPage: TasksDestinations,
) {
    val transition = updateTransition(
        targetState = tabPage,
        label = "Tasks Tab Indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            when {
                TasksDestinations.Tasks
                        isTransitioningTo TasksDestinations.Done -> {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
                TasksDestinations.Done
                        isTransitioningTo TasksDestinations.Statistics -> {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
                TasksDestinations.Tasks
                        isTransitioningTo TasksDestinations.Statistics -> {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
                else -> {
                    spring(stiffness = Spring.StiffnessMedium)
                }
            }
        },
        label = "Indicator Left"
    ) { page ->
        tabPositions[page.ordinal].left
    }

    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (TasksDestinations.Statistics
                isTransitioningTo TasksDestinations.Tasks
            ) {
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }

    Box(
        modifier = modifier
            .zIndex(1f)
            .fillMaxSize()
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    )
}