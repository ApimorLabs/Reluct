package work.racka.reluct.common.database.adapters

import com.squareup.sqldelight.ColumnAdapter
import work.racka.reluct.common.model.util.time.Week

internal val listOfStringAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> =
        if (databaseValue.isBlank()) {
            listOf()
        } else {
            databaseValue.split(";")
        }

    override fun encode(value: List<String>): String = value.joinToString(";")
}

internal val longRangeAdapter = object : ColumnAdapter<LongRange, String> {
    override fun encode(value: LongRange): String = "${value.first}-${value.last}"
    override fun decode(databaseValue: String): LongRange =
        databaseValue.split("-", limit = 2).let { split ->
            LongRange(split.first().toLong(), split.last().toLong())
        }
}

internal val listOfWeekAdapter = object : ColumnAdapter<List<Week>, String> {
    private val enumValues = Week.values()
    override fun encode(value: List<Week>): String = value.joinToString(":")
    override fun decode(databaseValue: String): List<Week> = if (databaseValue.isBlank()) {
        listOf()
    } else databaseValue.split(":")
        .map { enumName -> enumValues.first { it.name == enumName } }
}