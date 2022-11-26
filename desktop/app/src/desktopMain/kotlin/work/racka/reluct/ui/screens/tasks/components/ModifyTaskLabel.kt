package work.racka.reluct.ui.screens.tasks.components

import work.racka.reluct.common.model.domain.tasks.TaskLabel

internal sealed class ModifyTaskLabel {
    class Delete(val label: TaskLabel) : ModifyTaskLabel()
    class SaveLabel(val label: TaskLabel) : ModifyTaskLabel()
}
