package student.example.myapplication.admin.services;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import student.example.myapplication.admin.broadcast.ReceiverAppLock;

import static java.lang.Thread.sleep;

public class ServiceApplockJobIntent extends JobIntentService {
    private static final int JOB_ID = 15462;
    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, ServiceApplockJobIntent.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        runAppLock();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        BackgroundManager.getInstance().init(this).startService();

        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        BackgroundManager.getInstance().init(this).startService();

        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onDestroy();
    }

    private void runAppLock(){
        long endTime = System.currentTimeMillis()+1500;
        while(System.currentTimeMillis() < endTime){
            synchronized (this){
                try {
                    Intent intent = new Intent(this, ReceiverAppLock.class);
                    sendBroadcast(intent);
                    sleep(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
