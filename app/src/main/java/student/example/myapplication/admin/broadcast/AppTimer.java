package student.example.myapplication.admin.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import student.example.myapplication.admin.set.applimits.DailyLimitTime;
import student.example.myapplication.admin.set.applimits.Utils;

public class AppTimer extends BroadcastReceiver {

    private Utils utils;
    private DailyLimitTime dailyLimitTime;
    private CountDownTimer countDownTimer;
    private long timeCountInMilliSeconds;
    private String pkgName;
    private long remainTimeInMS;


    @Override
    public void onReceive(Context context, Intent intent) {
        utils = new Utils(context);
        dailyLimitTime = new DailyLimitTime(context);

        Log.w("AppTimer","Here is AppTimer");

        if(intent.getStringExtra("pkgName")!=null){
            timeCountInMilliSeconds = intent.getLongExtra("timeCountInMilliSeconds", 0);
            Log.e("timeCountInMilliSeconds", timeCountInMilliSeconds+"");
            pkgName = intent.getStringExtra("pkgName");
            Log.e("pkgName", pkgName);

            setLimitAppTimer(context, timeCountInMilliSeconds, pkgName);
        }
    }

    private void setLimitAppTimer(final Context context, long timeCountInMilliSeconds, final String pkgName){
        Log.e("timeCountInMilliSeconds", timeCountInMilliSeconds/1000+"");
        if(timeCountInMilliSeconds == 0){
            showLock(context);
        }else{
            countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    utils.setTimerState(true);
                    remainTimeInMS = millisUntilFinished;
                    Log.e("onTick", hmsTimeFormatter(millisUntilFinished));

                    if(!pkgName.equals(utils.getLauncherTopApp())){
                        Log.e("remainTimeInMS", remainTimeInMS/1000+"");
                        dailyLimitTime.setLimitTimeInMS(pkgName, remainTimeInMS);
                        utils.clearLastApp();

                        countDownTimer.cancel();
                        utils.setTimerState(false);
                    }
                }

                @Override
                public void onFinish() {
                    dailyLimitTime.setLimitTimeInMS(pkgName, 0);
                    utils.clearLastApp();
                    utils.setTimerState(false);

                    showLock(context);
                }
            }.start();
        }

    }

    private void showLock(Context context) {
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

    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }

}
