package student.example.myapplication.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import student.example.myapplication.admin.set.applimits.AppInfo;

public class LimitTimeList {
    List<LimitTime> limitTimeList = new ArrayList<LimitTime>();

    public void addTime(String pkgName, int limitHour, int limitMinute) {
        AppInfo appInfo = new AppInfo(pkgName, limitHour, limitMinute);
        long timeInMS = (long)(appInfo.getHours() * 60 * 60000 + appInfo.getMins() * 60000);
        LimitTime limitTime = new LimitTime(pkgName, timeInMS);
        limitTimeList.add(limitTime);
    }

    // Return all limit apps in array format
    public LimitTime[] getItems() {
        // View all jobs
        return limitTimeList.toArray(new LimitTime[limitTimeList.size()]);
    }

    // Empty list
    public void clear() {
        limitTimeList.clear();
    }
}
