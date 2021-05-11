package student.example.myapplication.admin.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import student.example.myapplication.admin.set.applimits.DailyLimitTime;
import student.example.myapplication.admin.set.applimits.Utils;


public class ReceiverAppLock extends BroadcastReceiver {

    private Utils utils;
    private DailyLimitTime dailyLimitTime;
    private long timeCountInMilliSeconds;

    @Override
    public void onReceive(final Context context, Intent intent) {
        utils = new Utils(context);
        dailyLimitTime = new DailyLimitTime(context);
        String appRunning = utils.getLauncherTopApp();



        if(utils.isLock(appRunning) && !appRunning.equals("student.example.myapplication")){
            if(!appRunning.equals(utils.getLastApp())){
                utils.clearLastApp();
                utils.setLastApp(appRunning);

                timeCountInMilliSeconds = dailyLimitTime.getLimitTimeInMS(appRunning);

                Intent timer = new Intent(context, AppTimer.class);
                timer.putExtra("pkgName", appRunning);
                timer.putExtra("timeCountInMilliSeconds", timeCountInMilliSeconds);
                context.sendBroadcast(timer);
            }
        }
    }

}
