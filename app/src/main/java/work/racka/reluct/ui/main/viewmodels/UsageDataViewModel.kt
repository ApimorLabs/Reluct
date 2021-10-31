package work.racka.reluct.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import work.racka.reluct.data.local.usagestats.UsageDataManager
import work.racka.reluct.data.local.usagestats.Week
import work.racka.reluct.di.IoDispatcher
import work.racka.reluct.model.UsageStats
import work.racka.reluct.ui.main.states.StatsState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UsageDataViewModel @Inject constructor(
    private val usageDataManager: UsageDataManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val usageStatsList = MutableStateFlow(listOf<UsageStats>())
    private val dayStats = MutableStateFlow(UsageStats(listOf(), Week.MONDAY, "", 0L))
    private val selectedWeekOffset = MutableStateFlow(-1)
    private val selectedDay = MutableStateFlow(0)

    val uIState: StateFlow<StatsState> = combine(
        usageStatsList,
        dayStats,
        selectedWeekOffset,
        selectedDay
    ) { usageStatsList, dayStats, selectedWeekOffset, selectedDay ->
        StatsState.Stats(
            usageStats = usageStatsList,
            dayStats = dayStats,
            selectedWeekOffset = selectedWeekOffset,
            selectedDay = selectedDay
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatsState.EmptyStats
    )

    init {
        getUsageStats()
    }

    private fun getUsageStats() {
        viewModelScope.launch(dispatcher) {
            val weeklyStats = usageDataManager.getWeeklyUsage(selectedWeekOffset.value)
            usageStatsList.emit(weeklyStats)
            dailyAppUsageInfo()
            dailyAppUsageInfo()
        }

    }

    /**
     * Gets Day stats for Display on Summary Chart & below UsageChart
     * Set day = 0 to get Today's Stats
     */
    private fun dailyAppUsageInfo() {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)
        var stats: UsageStats = dayStats.value
        if (selectedDay.value == 0) {
            selectedDay.value = today
        }
        usageStatsList.value.forEach {
            if (it.dayOfWeek.value == selectedDay.value &&
                selectedDay.value != 0
            ) {
                stats = it
            }
        }
        dayStats.value = stats
        Timber.d("Stats Found Active: ${dayStats.value.date} & sel day: ${selectedDay.value}")
    }

    fun updateSelectedWeek(weekOffsetBy: Int) {
        selectedWeekOffset.value = weekOffsetBy
        getUsageStats()
    }

    fun updateDailyAppUsageInfo(day: Int) {
        selectedDay.value = day
        dailyAppUsageInfo()
    }
}