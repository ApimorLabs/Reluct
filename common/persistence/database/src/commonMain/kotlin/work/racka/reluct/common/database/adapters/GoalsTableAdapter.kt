package work.racka.reluct.common.database.adapters

import com.squareup.sqldelight.EnumColumnAdapter
import work.racka.reluct.common.database.tables.GoalsTable

internal val GoalsTableAdapter = GoalsTable.Adapter(
    relatedAppsAdapter = listOfStringAdapter,
    goalIntervalAdapter = EnumColumnAdapter(),
    timeIntervalAdapter = longRangeAdapter,
    daysOfWeekSelectedAdapter = listOfWeekAdapter,
    goalTypeAdapter = EnumColumnAdapter()
)
