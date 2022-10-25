package work.racka.reluct.common.domain.mappers.tasks

import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.model.domain.tasks.TaskLabel

fun TaskLabelDbObject.asTaskLabel() = TaskLabel(
    id = id,
    name = name,
    description = description,
    colorHexString = colorHexString
)

fun TaskLabel.asTaskLabelDbObject() = TaskLabelDbObject(
    id = id,
    name = name,
    description = description,
    colorHexString = colorHexString
)