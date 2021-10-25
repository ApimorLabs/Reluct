package work.racka.reluct.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import work.racka.reluct.data.local.usagestats.UsageDataManager
import javax.inject.Inject

@HiltViewModel
class UsageDataViewModel @Inject constructor(
    private val usageDataManager: UsageDataManager
): ViewModel() {

}