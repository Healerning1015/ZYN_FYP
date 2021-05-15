package student.example.myapplication.admin.set.studytime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import student.example.myapplication.R;
import student.example.myapplication.home.LearningModule;

import static androidx.core.content.ContextCompat.getSystemService;

public class AlarmManagerUtils {

    private Context context;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;

    private Calendar calendar;

    private double studyTime = 25;
    private double breakTime = 5;
    private int studyCount;
    private int breakCount;


    private AlarmManagerUtils(Context aContext) {
        this.context = aContext;
    }

    private static AlarmManagerUtils instance = null;

    public static AlarmManagerUtils getInstance(Context aContext) {
        if (instance == null) {
            synchronized (AlarmManagerUtils.class) {
                if (instance == null) {
                    instance = new AlarmManagerUtils(aContext);
                }
            }
        }
        return instance;
    }

    public void createAlarmManager(int alarmId, int studyLong) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        studyTime = 25;
        breakTime = 5;
        calculateStudyTime(studyLong);
        Log.e("work task",alarmId+", "+studyTime+", "+breakTime+", "+(studyCount+breakCount));

        Intent intent = new Intent(context, LearningModule.class);
        //intent.setComponent(new ComponentName("student.example.myapplication", "student.example.myapplication.home.LearningModule"));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("alarmId",alarmId);
        intent.putExtra("workTime",studyTime);
        intent.putExtra("breakTime",breakTime);
        intent.putExtra("switchCount",studyCount+breakCount);

        pendingIntent = PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void calculateStudyTime(int studyLong) {
        Log.e("studyLong",studyLong+"");
        if(studyLong>=55){
            studyCount = (studyLong+5) / 30;
            breakCount = studyCount -1;
            int extraTime = studyLong - (25 * studyCount + 5 * breakCount);
            if(studyCount%2==0){ //如果studyCount是偶数
                studyTime = 25 + (double)extraTime / studyCount;
            } else {
                breakTime = 5 + (double)extraTime / breakCount;
            }

            //尽量调整休息时间和学习时间的比例
            while(breakTime>=5+studyCount){
                breakTime -= studyCount;
                studyTime += breakCount;
            }
        } else {
            studyTime = studyLong;
            studyCount = 1;
            breakCount = 0;
        }
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerStartWork(int dayOfWeek, int hour, int min) {


        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,00);

        //时间设置后本周周几的时间
        //Log.i("Set time:", calendar.getTime()+"");
        //判断刚才设置的时间在本周是否已经过去了
        Log.e("isToday()", isToday(calendar.getTime())+"");
        //如果已经过去了，那么推迟一周
        if(!isToday(calendar.getTime())){
            calendar.add(Calendar.DAY_OF_MONTH,7);
        }
        Log.i("Set time:", calendar.getTime()+"");

        //版本适配 System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            //am.setRepeating(AlarmManager.RTC_WAKEUP,
            //calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
        }
    }


    public static boolean isToday(Date day) {
        // 当前的时刻
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        // 设定的时刻
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);
            int diffHour = cal.get(Calendar.HOUR_OF_DAY) - pre.get(Calendar.HOUR_OF_DAY);
            int diffMin = cal.get(Calendar.MINUTE) - pre.get(Calendar.MINUTE);
            int diffSec = cal.get(Calendar.SECOND) - pre.get(Calendar.SECOND);
            if (diffDay == 0) {
                if (diffHour == 0) {
                    if (diffMin == 0) {
                        if (diffSec >= 0) {
                            return true;
                        }
                    } else if (diffMin > 0) {
                        return true;
                    }
                } else if (diffHour > 0) {
                    return true;
                }
            } else if (diffDay > 0) {
                return true;
            }
        } else if (cal.get(Calendar.YEAR) > (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }
}
