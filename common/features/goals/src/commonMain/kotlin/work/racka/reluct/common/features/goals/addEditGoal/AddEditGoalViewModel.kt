package work.racka.reluct.common.features.goals.addEditGoal

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.appInfo.GetInstalledApps
import work.racka.reluct.common.domain.usecases.goals.GetGoals
import work.racka.reluct.common.domain.usecases.goals.ModifyGoals
import work.racka.reluct.common.features.goals.active.states.DefaultGoals
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.addEditGoal.states.AddEditGoalState
import work.racka.reluct.common.features.goals.addEditGoal.states.GoalAppsState
import work.racka.reluct.common.features.goals.addEditGoal.states.ModifyGoalState
import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.util.list.filterPersistentNot

/**
 * [goalId] is passed when you want to edit a saved Goal and will be null for new goals
 * [defaultGoalIndex] is provided to get a predefined Goal from the list shown on the UI
 */
class AddEditGoalViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val getInstalledApps: GetInstalledApps,
    private val goalId: String?,
    private val defaultGoalIndex: Int?
) : CommonViewModel() {

    private val goalAppsState: MutableStateFlow<GoalAppsState> =
        MutableStateFlow(GoalAppsState.Nothing)
    private val modifyGoalState: MutableStateFlow<ModifyGoalState> =
        MutableStateFlow(ModifyGoalState.Loading)
    val uiState: StateFlow<AddEditGoalState> = combine(
        modifyGoalState,
        goalAppsState
    ) { modifyGoalState, goalAppsState ->
        AddEditGoalState(
            modifyGoalState = modifyGoalState,
            appsState = goalAppsState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AddEditGoalState()
    )

    private val eventsChannel = Channel<GoalsEvents>(Channel.UNLIMITED)
    val events: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var installedApps: ImmutableList<AppInfo> = persistentListOf()

    private var syncAppsJob: Job? = null

    init {
        getGoal(goalId)
    }

    fun saveCurrentGoal() {
        vmScope.launch {
            val goalState = modifyGoalState.value
            if (goalState is ModifyGoalState.Data) {
                modifyGoals.saveGoal(goalState.goal)
                val result =
                    eventsChannel.trySend(GoalsEvents.GoalSavedMessage(goalState.goal.name))
                result.onSuccess {
                    /**
                     * Go back after saving if you are just editing a Goal and the [goalId]
                     * is not null else just show the GoalSaved State for adding more tasks
                     */
                    goalId?.run { eventsChannel.send(GoalsEvents.Exit) }
                        ?: modifyGoalState.update { ModifyGoalState.Saved }
                }
            }
        }
    }

    fun newGoal() {
        modifyGoalState.update {
            ModifyGoalState.Data(
                isEdit = false,
                goal = DefaultGoals.emptyGoal()
            )
        }
    }

    fun syncRelatedApps() {
        syncAppsJob?.cancel()
        syncAppsJob = vmScope.launch {
            goalAppsState.update { GoalAppsState.Loading }
            if (installedApps.isEmpty()) {
                installedApps = getInstalledApps.invoke()
            }
            val goalState = modifyGoalState.value
            if (goalState is ModifyGoalState.Data) {
                val unselected = installedApps.filterPersistentNot { installed ->
                    goalState.goal.relatedApps.any { it.packageName == installed.packageName }
                }
                goalAppsState.update {
                    GoalAppsState.Data(
                        selected = goalState.goal.relatedApps,
                        unselected = unselected
                    )
                }
            } else {
                goalAppsState.update {
                    GoalAppsState.Data(
                        selected = persistentListOf(),
                        unselected = installedApps
                    )
                }
            }
        }
    }

    fun updateCurrentGoal(goal: Goal) {
        modifyGoalState.update { ModifyGoalState.Data(isEdit = goalId != null, goal = goal) }
    }

    fun modifyRelatedApps(appInfo: AppInfo, isAdd: Boolean) {
        val goalState = modifyGoalState.value
        if (goalState is ModifyGoalState.Data) {
            val relatedApps = goalState.goal.relatedApps.toPersistentList().builder()
                .apply { if (isAdd) add(appInfo) else remove(appInfo) }
                .build()
            val unSelectedApps = installedApps.filterPersistentNot { installed ->
                relatedApps.any { it.packageName == installed.packageName }
            }
            goalAppsState.update {
                GoalAppsState.Data(
                    selected = relatedApps,
                    unselected = unSelectedApps
                )
            }
            updateCurrentGoal(goalState.goal.copy(relatedApps = relatedApps))
        }
    }

    private fun getGoal(id: String?) {
        vmScope.launch {
            if (defaultGoalIndex != null) {
                getPredefinedGoal(defaultGoalIndex)
            } else {
                when (id) {
                    null -> modifyGoalState.update {
                        ModifyGoalState.Data(
                            isEdit = false,
                            goal = DefaultGoals.emptyGoal()
                        )
                    }
                    else -> {
                        when (val goal = getGoals.getGoal(id).firstOrNull()) {
                            null -> modifyGoalState.update { ModifyGoalState.NotFound }
                            else -> modifyGoalState.update {
                                ModifyGoalState.Data(
                                    goal,
                                    isEdit = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPredefinedGoal(index: Int) {
        val goal = DefaultGoals.predefined().getOrNull(index)
        modifyGoalState.update {
            ModifyGoalState.Data(
                isEdit = false,
                goal = goal ?: DefaultGoals.emptyGoal()
            )
        }
    }
}
