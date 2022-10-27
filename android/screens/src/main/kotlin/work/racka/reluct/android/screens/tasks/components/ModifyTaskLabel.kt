package work.racka.reluct.android.screens.tasks.components

import work.racka.reluct.common.model.domain.tasks.TaskLabel

internal sealed class ModifyTaskLabel {
    class Delete(label: TaskLabel) : ModifyTaskLabel()
    class SaveLabel(label: TaskLabel) : ModifyTaskLabel()
}
