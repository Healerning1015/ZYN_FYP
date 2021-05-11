package student.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import student.example.myapplication.admin.services.ServiceApplockJobIntent;
import student.example.myapplication.admin.services.ServiceWriteLimitTime;

public class WelcomePage extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
    private String todayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        this.getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                WelcomePage.this.startActivity(intent);
                WelcomePage.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);


        isTodayFirstLogin();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveExitTime(todayTime);
    }

    /**
     * 保存每次退出的时间
     * @param extiLoginTime
     */
    private void saveExitTime(String extiLoginTime) {
        SharedPreferences.Editor editor = getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }

    private void isTodayFirstLogin() {
        //取
        SharedPreferences preferences = getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "1999-10-15");
        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        todayTime = df.format(new Date());// 获取当前的日期

        if (lastTime.equals(todayTime)) { //如果两个时间段相等
            Toast.makeText(this, "不是当日首次进入", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, ServiceWriteLimitTime.class);
            //ServiceWriteLimitTime.enqueueWork(this, intent);
            Log.e("Time", lastTime);
        } else {
            Toast.makeText(this, "当日首次进入", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ServiceWriteLimitTime.class);
            ServiceWriteLimitTime.enqueueWork(this, intent);

            Log.e("date", lastTime);
            Log.e("todayDate", todayTime);
        }
    }

}