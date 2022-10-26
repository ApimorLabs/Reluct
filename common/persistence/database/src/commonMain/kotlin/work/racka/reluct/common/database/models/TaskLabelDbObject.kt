package work.racka.reluct.common.database.models

data class TaskLabelDbObject(
    val id: String,
    val name: String,
    val description: String,
    val colorHexString: String
)
