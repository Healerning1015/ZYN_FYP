package student.example.myapplication.home;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    public static ComponentName getComponentName(Context context){
        return new ComponentName(context, MyDeviceAdminReceiver.class);
    }

    public static String TAG = "MyDeviceAdminReceiver";

    @Override
    public void onLockTaskModeEntering(@NonNull Context context, @NonNull Intent intent, @NonNull String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d(TAG, "onLockTaskModeEntering");
    }

    @Override
    public void onLockTaskModeExiting(@NonNull Context context, @NonNull Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        Log.d(TAG, "onLockTaskModeExiting");
    }
}

