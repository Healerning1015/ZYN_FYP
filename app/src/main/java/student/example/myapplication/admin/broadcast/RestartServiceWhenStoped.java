package student.example.myapplication.admin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import student.example.myapplication.admin.services.BackgroundManager;

public class RestartServiceWhenStoped extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("Broadcast Listened", "Service tried to stop");
            //Toast.makeText(context, “Service restarted”, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundManager.class));
            }
            else {
                context.startService(new Intent(context, BackgroundManager.class));
            }
            // context.startService(new Intent(context.getApplicationContext(), NotificationService.class));
        }
        Log.i("Broadcast Listened", "Service tried to stop");
        //Toast.makeText(context, “Service restarted”, Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, BackgroundManager.class));
        }
        else {
            context.startService(new Intent(context, BackgroundManager.class));
        }

         */
    }
}
