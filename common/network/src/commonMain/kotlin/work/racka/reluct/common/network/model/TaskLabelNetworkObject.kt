package work.racka.reluct.common.network.model

import work.racka.reluct.common.database.models.TaskLabelDbObject

data class TaskLabelNetworkObject(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val colorHexString: String? = null
) {
    fun toDbObject(): TaskLabelDbObject? =
        if (id == null || colorHexString == null || name == null) {
            null
        } else {
            TaskLabelDbObject(
                id = id,
                name = name,
                description = description ?: "",
                colorHexString = colorHexString
            )
        }
}

fun TaskLabelDbObject.toNetworkObject() = TaskLabelNetworkObject(
    id = id,
    name = name,
    description = description,
    colorHexString = colorHexString
)
