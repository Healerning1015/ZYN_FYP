package student.example.myapplication.admin.set.applimits;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

import io.paperdb.Paper;

import static android.app.AppOpsManager.MODE_ALLOWED;

public class Utils {
    private String EXTRA_LAST_APP = "EXTRA_LAST_APP";
    private String TIMER_IS_RUNNING = "TIMER_IS_RUNNING";
    private Context context;

    public Utils(Context context){
        this.context = context;
        Paper.init(context);
    }

    public boolean isTimerRunning(){
        if(Paper.book().read(TIMER_IS_RUNNING) == null){
            Paper.book().write(TIMER_IS_RUNNING, false);
        }
        return Paper.book().read(TIMER_IS_RUNNING);
    }

    public void setTimerState(boolean isRunning){
        Paper.book().write(TIMER_IS_RUNNING, isRunning);
    }

    public boolean isLock(String packageName){
        return Paper.book().read(packageName) != null;
    }

    public void lock(String pk){
        Paper.book().write(pk, pk);
    }

    public void unlock(String pk){
        Paper.book().delete(pk);
    }

    public void setLastApp(String pk){
        Paper.book().write(EXTRA_LAST_APP, pk);
    }

    public String getLastApp(){
        return Paper.book().read(EXTRA_LAST_APP);
    }

    public void clearLastApp(){
        Paper.book().delete(EXTRA_LAST_APP);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkPermission(Context ctx){
        /*
        try {
            PackageManager packageManager = ctx.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) ctx.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

         */

        AppOpsManager appOpsManager = (AppOpsManager)ctx.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.getPackageName());
        return mode == MODE_ALLOWED;


    }

    UsageStatsManager usageStatsManager;
    public String getLauncherTopApp(){
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            List<ActivityManager.RunningTaskInfo> taskInfoList = manager.getRunningTasks(1);
            if(null != taskInfoList && !taskInfoList.isEmpty()){
                return taskInfoList.get(0).topActivity.getPackageName();
            }
        }else{
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 1000*60;//1min

            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while(usageEvents.hasNextEvent()){
                usageEvents.getNextEvent(event);
                if(event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND){
                    result = event.getPackageName();
                }
            }

            if(!TextUtils.isEmpty(result)){
                Log.e("Utils", "LauncherTopApp:"+result);
                return result;
            }
        }
        return "null";
    }
}
