package student.example.myapplication.admin.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import student.example.myapplication.admin.set.applimits.AppLimitsDB;
import student.example.myapplication.admin.set.applimits.Utils;
import student.example.myapplication.home.LimitTime;
import student.example.myapplication.home.LimitTimeList;

public class ReceiverAppLock extends BroadcastReceiver {

    private AppLimitsDB appLimitsDB;
    private LimitTimeList limitTimeList = new LimitTimeList();
    private long timeCountInMilliSeconds;
    private CountDownTimer countDownTimer;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Utils utils = new Utils(context);
        String appRunning = utils.getLauncherTopApp();

        appLimitsDB = new AppLimitsDB(context);
        limitTimeList = appLimitsDB.getAllLimitTime(limitTimeList);
        LimitTime[] limitTimes = limitTimeList.getItems();


        if(utils.isLock(appRunning) && !appRunning.equals("student.example.myapplication")){
            if(!appRunning.equals(utils.getLastApp())){
                utils.clearLastApp();
                utils.setLastApp(appRunning);


                for (int i = 0; i < limitTimes.length; i++) {
                    if (limitTimes[i].getPackageName().equals(appRunning)) {
                        Log.e("remain", limitTimes[i].getLimitTimeInMS() + "");
                        timeCountInMilliSeconds = limitTimes[i].getLimitTimeInMS();

                        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.e("onTick", hmsTimeFormatter(millisUntilFinished));
                            }

                            @Override
                            public void onFinish() {
                                Intent i = new Intent();
                                i.setComponent(new ComponentName("student.example.myapplication", "student.example.myapplication.admin.set.applimits.PatternLockPage"));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("broadcast_receiver", "broadcast_receiver");
                                //context.startActivity(i);

                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                }



                /*
                Intent i = new Intent();
                i.setComponent(new ComponentName("student.example.myapplication", "student.example.myapplication.admin.set.applimits.PatternLockPage"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("broadcast_receiver", "broadcast_receiver");
                //context.startActivity(i);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                 */

            }
        }

        appLimitsDB.close();
    }

    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }
}
