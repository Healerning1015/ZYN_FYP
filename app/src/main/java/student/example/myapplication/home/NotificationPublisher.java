package student.example.myapplication.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import student.example.myapplication.R;

import static student.example.myapplication.admin.set.studytime.AlarmManagerUtils.pendingIntent;


public class NotificationPublisher extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 26)
        {
            //当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("This is a content title")
                    .setContentText("This is a content text")
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(pendingIntent, true)
                    .build();
            manager.notify(1, notification);
        }
        else
        {
            //当sdk版本小于26
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(pendingIntent, true)
                    .build();
            manager.notify(1,notification);
        }

    }
}
