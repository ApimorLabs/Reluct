//package work.racka.reluct.ui.main
//
//import android.app.usage.UsageStats
//import android.app.usage.UsageStatsManager
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.fragment.app.Fragment
//import com.example.reluct.R
//import CustomUsageStats
//import java.util.*
//
///**
// * Fragment that demonstrates how to use App Usage Statistics API.
// */
//class AppUsageStatisticsFragment : Fragment() {
//    //VisibleForTesting for variables below
//    var mUsageStatsManager: UsageStatsManager? = null
//    var mUsageListAdapter: UsageListAdapter? = null
//    var mRecyclerView: RecyclerView? = null
//    var mLayoutManager: RecyclerView.LayoutManager? = null
//    var mOpenUsageSettingButton: Button? = null
//    var mSpinner: Spinner? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mUsageStatsManager = activity
//            .getSystemService("usagestats") as UsageStatsManager //Context.USAGE_STATS_SERVICE
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_app_usage_statistics, container, false)
//    }
//
//    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(rootView, savedInstanceState)
//        mUsageListAdapter = UsageListAdapter()
//        mRecyclerView = rootView.findViewById<View>(R.id.recyclerview_app_usage) as RecyclerView
//        mLayoutManager = mRecyclerView.getLayoutManager()
//        mRecyclerView.scrollToPosition(0)
//        mRecyclerView.setAdapter(mUsageListAdapter)
//        mOpenUsageSettingButton =
//            rootView.findViewById<View>(R.id.button_open_usage_setting) as Button
//        mSpinner = rootView.findViewById<View>(R.id.spinner_time_span) as Spinner
//        val spinnerAdapter: SpinnerAdapter = ArrayAdapter.createFromResource(
//            activity!!,
//            R.array.action_list, android.R.layout.simple_spinner_dropdown_item
//        )
//        mSpinner!!.adapter = spinnerAdapter
//        mSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            var strings = resources.getStringArray(R.array.action_list)
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                val statsUsageInterval = StatsUsageInterval.getValue(strings[position])
//                if (statsUsageInterval != null) {
//                    val usageStatsList = getUsageStatistics(statsUsageInterval.mInterval)
//                    Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
//                    updateAppsList(usageStatsList)
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }
//
//    /**
//     * Returns the [.mRecyclerView] including the time span specified by the
//     * intervalType argument.
//     *
//     * @param intervalType The time interval by which the stats are aggregated.
//     * Corresponding to the value of [UsageStatsManager].
//     * E.g. [UsageStatsManager.INTERVAL_DAILY], [                     ][UsageStatsManager.INTERVAL_WEEKLY],
//     *
//     * @return A list of [android.app.usage.UsageStats].
//     */
//    fun getUsageStatistics(intervalType: Int): List<UsageStats> {
//        // Get the app statistics since one year ago from the current time.
//        val cal = Calendar.getInstance()
//        cal.add(Calendar.YEAR, -1)
//        val queryUsageStats = mUsageStatsManager
//            .queryUsageStats(
//                intervalType, cal.timeInMillis,
//                System.currentTimeMillis()
//            )
//        if (queryUsageStats.size == 0) {
//            Log.i(TAG, "The user may not allow the access to apps usage. ")
//            Toast.makeText(
//                activity,
//                getString(R.string.explanation_access_to_appusage_is_not_enabled),
//                Toast.LENGTH_LONG
//            ).show()
//            mOpenUsageSettingButton!!.visibility = View.VISIBLE
//            mOpenUsageSettingButton!!.setOnClickListener { startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }
//        }
//        return queryUsageStats
//    }
//
//    /**
//     * Updates the [.mRecyclerView] with the list of [UsageStats] passed as an argument.
//     *
//     * @param usageStatsList A list of [UsageStats] from which update the
//     * [.mRecyclerView].
//     */
//    //VisibleForTesting
//    fun updateAppsList(usageStatsList: List<UsageStats>) {
//        val customUsageStatsList: MutableList<CustomUsageStats> = ArrayList()
//        for (i in usageStatsList.indices) {
//            val customUsageStats = CustomUsageStats()
//            customUsageStats.usageStats = usageStatsList[i]
//            try {
//                val appIcon = activity!!.packageManager
//                    .getApplicationIcon(customUsageStats.usageStats!!.packageName)
//                customUsageStats.appIcon = appIcon
//            } catch (e: PackageManager.NameNotFoundException) {
//                Log.w(
//                    TAG, String.format(
//                        "App Icon is not found for %s",
//                        customUsageStats.usageStats!!.packageName
//                    )
//                )
//                customUsageStats.appIcon = activity
//                    .getDrawable(R.drawable.ic_default_app_launcher)
//            }
//            customUsageStatsList.add(customUsageStats)
//        }
//        mUsageListAdapter.setCustomUsageStatsList(customUsageStatsList)
//        mUsageListAdapter.notifyDataSetChanged()
//        mRecyclerView.scrollToPosition(0)
//    }
//
//    /**
//     * The [Comparator] to sort a collection of [UsageStats] sorted by the timestamp
//     * last time the app was used in the descendant order.
//     */
//    private class LastTimeLaunchedComparatorDesc :
//        Comparator<UsageStats?> {
//        override fun compare(left: UsageStats, right: UsageStats): Int {
//            return java.lang.Long.compare(right.lastTimeUsed, left.lastTimeUsed)
//        }
//    }
//
//    /**
//     * Enum represents the intervals for [android.app.usage.UsageStatsManager] so that
//     * values for intervals can be found by a String representation.
//     *
//     */
//    //VisibleForTesting
//    internal enum class StatsUsageInterval(
//        private val mStringRepresentation: String,
//        private val mInterval: Int
//    ) {
//        DAILY("Daily", UsageStatsManager.INTERVAL_DAILY), WEEKLY(
//            "Weekly",
//            UsageStatsManager.INTERVAL_WEEKLY
//        ),
//        MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY), YEARLY(
//            "Yearly",
//            UsageStatsManager.INTERVAL_YEARLY
//        );
//
//        companion object {
//            fun getValue(stringRepresentation: String): StatsUsageInterval? {
//                for (statsUsageInterval in values()) {
//                    if (statsUsageInterval.mStringRepresentation == stringRepresentation) {
//                        return statsUsageInterval
//                    }
//                }
//                return null
//            }
//        }
//    }
//
//    companion object {
//        private val TAG = AppUsageStatisticsFragment::class.java.simpleName
//
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @return A new instance of fragment [AppUsageStatisticsFragment].
//         */
//        fun newInstance(): AppUsageStatisticsFragment {
//            return AppUsageStatisticsFragment()
//        }
//    }
//}