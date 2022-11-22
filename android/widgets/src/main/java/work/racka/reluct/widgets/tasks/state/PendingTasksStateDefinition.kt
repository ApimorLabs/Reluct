package work.racka.reluct.widgets.tasks.state

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal object PendingTasksStateDefinition : GlanceStateDefinition<PendingTasksInfo> {

    private const val DATA_STORE_FILE = "pending_tasks_info"

    private val Context.dataStore by dataStore(DATA_STORE_FILE, PendingTasksInfoSerializer)

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<PendingTasksInfo> {
        return context.dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILE)
    }

    object PendingTasksInfoSerializer : Serializer<PendingTasksInfo> {
        override val defaultValue: PendingTasksInfo = PendingTasksInfo.Nothing

        override suspend fun readFrom(input: InputStream): PendingTasksInfo = try {
            Json.decodeFromString(
                PendingTasksInfo.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Could not read PendingTaskInfo: ${e.message}")
        }

        override suspend fun writeTo(t: PendingTasksInfo, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(PendingTasksInfo.serializer(), t).encodeToByteArray()
                )
            }
        }
    }
}
