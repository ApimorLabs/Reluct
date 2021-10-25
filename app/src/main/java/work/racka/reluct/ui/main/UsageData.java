//package work.racka.reluct.ui.main;
//
//import android.app.usage.UsageEvents;
//import android.app.usage.UsageStatsManager;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class UsageData(Context mContext) {
//    private class AppUsageInfo {
//        Drawable appIcon;
//        String appName, packageName;
//        long timeInForeground;
//        int launchCount;
//
//        AppUsageInfo(String pName) {
//            this.packageName=pName;
//        }
//    }
//
//    void getUsageStatistics() {
//
//        UsageEvents.Event currentEvent;
//        List<UsageEvents.Event> allEvents = new ArrayList<>();
//        HashMap<String, AppUsageInfo> map = new HashMap <String, AppUsageInfo> ();
//
//        long currTime = System.currentTimeMillis();
//        long startTime = currTime - 1000*3600*3; //querying past three hours
//
//        UsageStatsManager mUsageStatsManager =  (UsageStatsManager)
//                mContext.getSystemService(Context.USAGE_STATS_SERVICE);
//
//        assert mUsageStatsManager != null;
//        UsageEvents usageEvents = mUsageStatsManager.queryEvents(usageQueryTodayBeginTime, currTime);
//
////capturing all events in a array to compare with next element
//
//        while (usageEvents.hasNextEvent()) {
//            currentEvent = new UsageEvents.Event();
//            usageEvents.getNextEvent(currentEvent);
//            if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND ||
//                    currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
//                allEvents.add(currentEvent);
//                String key = currentEvent.getPackageName();
//// taking it into a collection to access by package name
//                if (map.get(key)==null)
//                    map.put(key,new AppUsageInfo(key));
//            }
//        }
//
////iterating through the arraylist
//        for (int i=0;i<allEvents.size()-1;i++){
//            UsageEvents.Event E0=allEvents.get(i);
//            UsageEvents.Event E1=allEvents.get(i+1);
//
////for launchCount of apps in time range
//            if (!E0.getPackageName().equals(E1.getPackageName()) && E1.getEventType()==1){
//// if true, E1 (launch event of an app) app launched
//                map.get(E1.getPackageName()).launchCount++;
//            }
//
////for UsageTime of apps in time range
//            if (E0.getEventType()==1 && E1.getEventType()==2
//                    && E0.getClassName().equals(E1.getClassName())){
//                long diff = E1.getTimeStamp()-E0.getTimeStamp();
//                phoneUsageToday+=diff; //gloabl Long var for total usagetime in the timerange
//                map.get(E0.getPackageName()).timeInForeground+= diff;
//            }
//        }
////transferred final data into modal class object
//        smallInfoList = new ArrayList<>(map.values());
//
//    }
//}
