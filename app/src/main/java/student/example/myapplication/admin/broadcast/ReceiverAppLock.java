package student.example.myapplication.admin.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import student.example.myapplication.admin.set.applimits.Utils;

public class ReceiverAppLock extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = new Utils(context);
        String appRunning = utils.getLauncherTopApp();

        //Log.e("Current Class","ReceiverAppLock");
        /*
        Log.i("appRunning",appRunning);
        if(utils.getLastApp()!=null){
            Log.i("LastApp",utils.getLastApp());
        }else{
            Log.i("LastApp","null");
        }

         */


        if(utils.isLock(appRunning) && !appRunning.equals("student.example.myapplication")){
            if(!appRunning.equals(utils.getLastApp())){
                utils.clearLastApp();
                utils.setLastApp(appRunning);

                Intent i = new Intent();
                i.setComponent(new ComponentName("student.example.myapplication", "student.example.myapplication.admin.set.applimits.PatternLockPage"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("broadcast_receiver","broadcast_receiver");
                //context.startActivity(i);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
