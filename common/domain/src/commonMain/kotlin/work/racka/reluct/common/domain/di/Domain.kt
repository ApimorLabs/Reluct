package work.racka.reluct.common.domain.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.domain.usecases.appUsage.GetAppUsageInfo
import work.racka.reluct.common.domain.usecases.appUsage.GetUsageStats
import work.racka.reluct.common.domain.usecases.appUsage.impl.GetAppUsageInfoImpl
import work.racka.reluct.common.domain.usecases.appUsage.impl.GetUsageStatsImpl
import work.racka.reluct.common.domain.usecases.authentication.LoginSignupUser
import work.racka.reluct.common.domain.usecases.authentication.UserAccountManagement
import work.racka.reluct.common.domain.usecases.authentication.impl.LoginSignupUserImpl
import work.racka.reluct.common.domain.usecases.authentication.impl.UserAccountManagementImpl
import work.racka.reluct.common.domain.usecases.billing.ManageCoffeeProducts
import work.racka.reluct.common.domain.usecases.billing.impl.ManageCoffeeProductsImpl
import work.racka.reluct.common.domain.usecases.goals.GetGoals
import work.racka.reluct.common.domain.usecases.goals.ModifyGoals
import work.racka.reluct.common.domain.usecases.goals.impl.GetGoalsImpl
import work.racka.reluct.common.domain.usecases.goals.impl.ModifyGoalsImpl
import work.racka.reluct.common.domain.usecases.limits.*
import work.racka.reluct.common.domain.usecases.limits.impl.*
import work.racka.reluct.common.domain.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ManageTaskLabels
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.domain.usecases.tasks.impl.GetGroupedTasksStatsImpl
import work.racka.reluct.common.domain.usecases.tasks.impl.GetTasksUseCaseImpl
import work.racka.reluct.common.domain.usecases.tasks.impl.ManageTaskLabelsImpl
import work.racka.reluct.common.domain.usecases.tasks.impl.ModifyTaskUseCaseImpl
import work.racka.reluct.common.domain.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.domain.usecases.time.impl.GetWeekRangeFromOffsetImpl

object Domain {

    fun KoinApplication.install() =
        this.apply {
            modules(
                commonModule(),
                Platform.installModule()
            )
        }

    private fun commonModule() = module {
        /** UseCases **/
        // Tasks
        factory<GetTasksUseCase> {
            GetTasksUseCaseImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ModifyTaskUseCase> {
            ModifyTaskUseCaseImpl(
                dao = get(),
                tasksUpload = get(),
                haptics = get(),
                manageTasksAlarms = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<GetGroupedTasksStats> {
            GetGroupedTasksStatsImpl(
                dao = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ManageTaskLabels> {
            ManageTaskLabelsImpl(
                dao = get(),
                haptics = get(),
                dispatcher = Dispatchers.IO
            )
        }

        // Time
        factory<GetWeekRangeFromOffset> {
            GetWeekRangeFromOffsetImpl(dispatcher = CoroutineDispatchers.backgroundDispatcher)
        }

        // App Usage Stats
        factory<GetAppUsageInfo> {
            GetAppUsageInfoImpl(
                usageManager = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher,
                getAppInfo = get()
            )
        }

        factory<GetUsageStats> {
            GetUsageStatsImpl(
                usageManager = get(),
                getAppInfo = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        // Limits
        factory<GetAppLimits> {
            GetAppLimitsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                dispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }
        factory<GetPausedApps> {
            GetPausedAppsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ModifyAppLimits> {
            ModifyAppLimitsImpl(
                limitsDao = get(),
                dispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<GetDistractingApps> {
            GetDistractingAppsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ManageDistractingApps> {
            ManageDistractingAppsImpl(
                getDistractingApps = get(),
                getInstalledApps = get(),
                modifyAppLimits = get(),
                haptics = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ManagePausedApps> {
            ManagePausedAppsImpl(
                getPausedApps = get(),
                getInstalledApps = get(),
                modifyAppLimits = get(),
                haptics = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ManageFocusMode> {
            ManageFocusModeImpl(
                settings = get(),
                haptics = get(),
                backgroundDispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        factory<ManageAppTimeLimit> {
            ManageAppTimeLimitImpl(
                limitsDao = get(),
                getAppInfo = get(),
                haptics = get(),
                dispatcher = CoroutineDispatchers.backgroundDispatcher
            )
        }

        /** Goals **/

        factory<GetGoals> {
            GetGoalsImpl(
                goalsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ModifyGoals> {
            ModifyGoalsImpl(
                goalsDao = get(),
                haptics = get(),
                usageDataManager = get(),
                getGroupedTasksStats = get(),
                dispatcher = Dispatchers.IO
            )
        }

        /** Billing **/
        factory<ManageCoffeeProducts> { ManageCoffeeProductsImpl(billingApi = get()) }

        /** Authentication **/
        factory<LoginSignupUser> {
            LoginSignupUserImpl(
                userAuth = get(),
                dispatcher = Dispatchers.IO
            )
        }

        factory<UserAccountManagement> {
            UserAccountManagementImpl(
                manageUser = get(),
                userAuth = get(),
                dispatcher = Dispatchers.IO
            )
        }
    }
}
