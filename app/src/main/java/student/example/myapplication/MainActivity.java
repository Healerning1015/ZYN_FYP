package student.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.Date;

import student.example.myapplication.admin.set.setEmail.Email;
import student.example.myapplication.admin.services.BackgroundManager;
import student.example.myapplication.admin.set.setEmail.EmailReceiver;


public class MainActivity extends AppCompatActivity{

    private final static String TAG="MainActivity";
    private View mLayout;
    private Email email;
    private static final int ALARM_ID = 199915;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.container);

        this.getSupportActionBar().hide();

        BackgroundManager.getInstance().init(this).startService();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_admin, R.id.navigation_usage, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        emailAlarmSet(this);
    }

    public static void emailAlarmSet(Context context){
        Email email = new Email(context);
        if(email.getEmail()!=null && email.getWhetherSend()!=null){
            if(email.getWhetherSend()){
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(context, EmailReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if(email.getSendTime()==null){
                    int[] time = {8, 0};
                    email.setSendTime(time);
                }

                Calendar calendar;
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, email.getSendTime()[0]);
                calendar.set(Calendar.MINUTE, email.getSendTime()[1]);
                calendar.set(Calendar.SECOND,0);

                if(!isToday(calendar.getTime())){
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                }
                Log.i("Sent email time:", calendar.getTime()+"");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0?????????
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4?????????
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pendingIntent);
                }
            }
        }
    }

    public static boolean isToday(Date day) {
        // ???????????????
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        // ???????????????
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