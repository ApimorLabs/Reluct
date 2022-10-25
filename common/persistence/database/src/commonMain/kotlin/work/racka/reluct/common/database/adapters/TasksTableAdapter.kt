package work.racka.reluct.common.database.adapters

import work.racka.reluct.common.database.tables.TasksTable

internal val TasksTableAdapter = TasksTable.Adapter(
    taskLabelsIdAdapter = listOfStringAdapter
)