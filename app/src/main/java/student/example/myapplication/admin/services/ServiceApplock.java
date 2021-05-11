package student.example.myapplication.admin.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import student.example.myapplication.admin.broadcast.ReceiverAppLock;

import static java.lang.Thread.sleep;

public class ServiceApplock extends IntentService {

    public ServiceApplock(){
        super("ServiceApplock");
    }

    private void runAppLock(){
        long endTime = System.currentTimeMillis()+210;
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

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        runAppLock();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        BackgroundManager.getInstance().init(this).startService();

        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        BackgroundManager.getInstance().init(this).startService();
        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onDestroy();
    }
}
