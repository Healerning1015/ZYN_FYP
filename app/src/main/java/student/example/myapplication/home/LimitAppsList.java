package student.example.myapplication.home;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import student.example.myapplication.admin.set.applimits.AppInfo;

public class LimitAppsList {
    List<AppInfo> limitAppsList = new ArrayList<AppInfo>();

    // Add a new limit to the list
    public void addLimit(String appid, String appName, String pkgName, Drawable logo, int limitHour, int limitMinute, boolean alwaysAllowed) {
        AppInfo appInfo = new AppInfo(appName, pkgName, logo, limitHour, limitMinute, alwaysAllowed);
        limitAppsList.add(appInfo);
    }

    // Return all limit apps in array format
    public AppInfo[] getItems() {
        // View all jobs
        return limitAppsList.toArray(new AppInfo[limitAppsList.size()]);
    }

    // Empty list
    public void clear() {
        limitAppsList.clear();
    }
}
