package student.example.myapplication.admin.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import student.example.myapplication.admin.set.applimits.AppLimitsDB;
import student.example.myapplication.admin.set.applimits.DailyLimitTime;
import student.example.myapplication.home.LimitTime;
import student.example.myapplication.home.LimitTimeList;

public class ServiceWriteLimitTime extends JobIntentService {
    private static final int JOB_ID = 1000;
    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, ServiceWriteLimitTime.class, JOB_ID, work);
    }

    private DailyLimitTime dailyLimitTime;
    private AppLimitsDB appLimitsDB;
    private LimitTimeList limitTimeList = new LimitTimeList();

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        dailyLimitTime = new DailyLimitTime(this);
        appLimitsDB = new AppLimitsDB(this);
        limitTimeList = appLimitsDB.getAllLimitTime(limitTimeList);
        LimitTime[] limitTimes = limitTimeList.getItems();
        Log.e("ServiceWriteLimitTime","Here is ServiceWriteLimitTime");
        for (LimitTime limitTime:limitTimes) {
            Log.e("pkgName",limitTime.getPackageName());
            Log.e("limitTime",limitTime.getLimitTimeInMS()+"");
            dailyLimitTime.setLimitTimeInMS(limitTime.getPackageName(), limitTime.getLimitTimeInMS());
        }
        appLimitsDB.close();
    }

    @Override
    public void onDestroy() {
        appLimitsDB.close();
        super.onDestroy();
    }
}
