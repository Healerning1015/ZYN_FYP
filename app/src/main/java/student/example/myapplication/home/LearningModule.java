package student.example.myapplication.home;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.UserManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

import student.example.myapplication.MainActivity;
import student.example.myapplication.R;
import student.example.myapplication.WelcomePage;
import student.example.myapplication.admin.set.applimits.AdminModePassword;

public class LearningModule extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private long timeCountInMilliSeconds = 1 * 60000; //一分钟

    boolean isAdmin;
    private ComponentName mAdminComponentName;
    private DevicePolicyManager mDevicePolicyManager;
    final static String LOCK_ACTIVITY_KEY = "student.example.myapplication.home.LearningModule";

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadSettings();
        setTimer();
    }

    private enum TimerStatus {
        STARTED,
        STOPPED,
    }

    int breakCount = 0;
    int switchCount = 0;
    boolean breakOrWork = false;
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime, showStudyTime, showBreakTime;
    private ImageView imageViewWork, imageViewBreak;
    private ImageView[] imageViewTomato;
    private LinearLayout tomatoBar;

    private CountDownTimer countDownTimer;
    private boolean  vibration;
    private SharedPreferences settings;
    private double workTime;
    private double breakTime;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_page);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setNotification();

        this.getSupportActionBar().hide();

        workTime = 0.1;
        breakTime = 0.05;
        switchCount = 7;

        //method call to initialize the views
        initViews();

        //Log.e("alarmId == 0",(getIntent().getIntExtra("alarmId", -1) == -1)+"");
        if(getIntent().getIntExtra("alarmId", -1)!=-1){
            int alarmId = getIntent().getIntExtra("alarmId", -1);
            //Log.e("alarmId",alarmId+"");
        }

        if(getIntent().getDoubleExtra("workTime", -1)!=-1){
            workTime = getIntent().getDoubleExtra("workTime",-1);
        }

        if(getIntent().getDoubleExtra("breakTime", -1)!=-1){
            breakTime = getIntent().getDoubleExtra("breakTime",-1);
        }

        if(getIntent().getIntExtra("switchCount",-1)!=-1){
            switchCount = getIntent().getIntExtra("switchCount",-1);
            //Log.e("switchCount",switchCount+"");
        }


        imageViewTomato = new ImageView[switchCount/2+1];
        for(int i = 0; i <= switchCount/2; i++){
            imageViewTomato[i] = new ImageView(this);
            imageViewTomato[i].setImageResource(R.mipmap.icon_tomato);
            imageViewTomato[i].setPadding(10,10,10,10);
            imageViewTomato[i].setAdjustViewBounds(true);
            imageViewTomato[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tomatoBar.addView(imageViewTomato[i]);
        }


        //Set vibrate feature
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Taking to feature from the setting menu
        loadSettings();

        //method call to initialize the settings menu item
        setTimer();

        //registerReceiver();

        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        mDevicePolicyManager.removeActiveAdmin(mAdminComponentName);

        isAdmin = isAdmin();
        setKioskPolicies(true, isAdmin);

        workstartStop();
    }

    private void setNotification() {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + 1000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

    }

    /**
     * method to initialize the settings menu item
     */
    private void setTimer() {
        Long work = Long.valueOf((long)(workTime * 60000));
        Long break_ = Long.valueOf((long)(breakTime * 60000));
        textViewTime.setText(hmsTimeFormatter(work));

        showStudyTime.setText(hmsTimeFormatter(work));
        showBreakTime.setText(hmsTimeFormatter(break_));
    }

    /**
     * Method to take settings from the setting menu
     */
    private void loadSettings() {
        vibration = settings.getBoolean("vibration", false);
        settings.registerOnSharedPreferenceChangeListener(LearningModule.this);
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        progressBarCircle = findViewById(R.id.progressBarCircle);
        textViewTime = findViewById(R.id.textViewTime);
        showStudyTime = findViewById(R.id.show_study_time);
        showBreakTime = findViewById(R.id.show_break_time);
        imageViewBreak = findViewById(R.id.imageViewBreak);
        imageViewWork = findViewById(R.id.imageViewWork);
        tomatoBar = findViewById(R.id.tomatoBar);
    }

    /**
     * method to reset count down timer
     */
    private void reset() {
        breakCount = 0;
        stopCountDownTimer();
        //startCountDownTimer();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        // call to initialize the progress bar values
        setProgressBarValues();
        //hiding break and work icon
        imageViewBreak.setVisibility(View.GONE);
        imageViewWork.setVisibility(View.GONE);
        // changing stop icon to start icon
        // changing the timer status to stopped
        timerStatus = TimerStatus.STOPPED;
    }


    /**
     * method to start and stop count down timer
     */
    private void workstartStop() {

        breakOrWork = true;
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            WorkSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the work icon
            imageViewWork.setVisibility(View.VISIBLE);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();


        } else {
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    /**
     * method to initialize the values for count down timer work
     */
    private void WorkSetTimerValues() {
        double time;
        time = workTime;
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = (long)(time * 60 * 1000);
    }

    /**
     * method to initialize the values for count down timer
     */
    private void BreakSetTimerValues() {
        double time;

        // fetching value from edit text and type cast to integer
        time = breakTime;

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = (long)(time * 60 * 1000);
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                //The count check end of the task
                breakCount++;
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the work icon
                imageViewWork.setVisibility(View.GONE);
                // hiding the break icon
                imageViewBreak.setVisibility(View.GONE);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                //Vibration
                vibration = settings.getBoolean("vibration", true);

                if (vibration) vibrator.vibrate(200);

                //checking work and break times
                checkBreakOrWork();
            }
        }.start();
    }

    /**
     * method check break and work duration
     */
    public void checkBreakOrWork() {
        if (breakCount != switchCount) {
            if (breakOrWork) {
                //breakAlert();
                Toast.makeText(this, "Good job! Now take a break", Toast.LENGTH_SHORT).show();
                breakStartStop();
            } else if (!breakOrWork) {
                //workAlert();
                Toast.makeText(this, "The break is over! Now working time", Toast.LENGTH_SHORT).show();
                workstartStop();
            } else {
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_LONG).show();
            }
        } else {
            imageViewTomato[0].setImageResource(R.mipmap.icon_tomato_gray);
            reset();
            textViewTime.setText("OVER");
            setKioskPolicies(false, isAdmin);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "Pomodoro is over", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LearningModule.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }, 1000);



        }

    }

    /**
     * method to show alert take a break
     */
    public void breakAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearningModule.this);
        alertDialogBuilder.setMessage("Good job! Would you like to take a break");

        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Take a break", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                breakStartStop();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to show alert take a break
     */
    public void workAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearningModule.this);
        alertDialogBuilder.setMessage("The break is over! Now working time");
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to start and stop count down timer break
     */
    private void breakStartStop() {
        breakOrWork = false;
        if (timerStatus == TimerStatus.STOPPED) {
            // call to initialize the timer values
            BreakSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the break icon
            imageViewBreak.setVisibility(View.VISIBLE);
            // making edit text not editable
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
        } else {
            breakCount = 0;
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }

        imageViewTomato[(switchCount-breakCount)/2].setImageResource(R.mipmap.icon_tomato_gray);
        //tomatoBar.removeView(imageViewTomato[(switchCount-breakCount)/2]);
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }

    @Override
    public void onBackPressed(){
        createDialog();
        //super.onBackPressed();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.fragment_admin, null);
        Button button = dialogView.findViewById(R.id.enter);
        button.setVisibility(View.GONE);
        builder.setView(dialogView);


        final EditText pwd = dialogView.findViewById(R.id.admin_pwd);
        final AdminModePassword adminModePassword = new AdminModePassword(this);

        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(pwd.getText().toString().equals(adminModePassword.getPassword())){
                    setKioskPolicies(false, isAdmin);
                    reset();
                    startActivity(new Intent(LearningModule.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
                //Toast.makeText(LearningModule.this, "你点击了是的", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(LearningModule.this, "你点击了不是", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e("TAG","对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("TAG","对话框消失了");
            }
        });
        //显示对话框
        dialog.show();
    }


    //Kiosk Mode
    private void setKioskPolicies(boolean enable, boolean isAdmin) {
        if (isAdmin) {
            setRestrictions(enable);
            enableStayOnWhilePluggedIn(enable);
            setUpdatePolicy(enable);
            setAsHomeApp(enable);
            setKeyGuardEnabled(enable);
        }
        setLockTask(enable, isAdmin);
        setImmersiveMode(enable);
    }

    private void setImmersiveMode(boolean enable) {
        if (enable) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void setKeyGuardEnabled(boolean enable) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable);
    }

    private void setAsHomeApp(boolean enable) {
        if (enable) {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
            intentFilter.addCategory(Intent.CATEGORY_HOME);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            mDevicePolicyManager.addPersistentPreferredActivity(
                    mAdminComponentName, intentFilter, new ComponentName("student.example.myapplication", "student.example.myapplication.home.LearningModule"));
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName, "student.example.myapplication");
        }
    }

    private void setUpdatePolicy(boolean enable) {
        if (enable) {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName, null);
        }
    }

    private void setLockTask(boolean start, boolean isAdmin) {
        if (isAdmin) {
            //mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, if (start) arrayOf(packageName) else arrayOf());
        }
        if (start) {
            startLockTask();
        } else {
            stopLockTask();
        }
    }

    private void enableStayOnWhilePluggedIn(boolean active) {
        if (active) {
            mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB | BatteryManager.BATTERY_PLUGGED_WIRELESS)+"");
        } else {
            mDevicePolicyManager.setGlobalSetting(mAdminComponentName, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, "0");
        }
    }

    private void setRestrictions(boolean disallow) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow);
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow);
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow);
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow);
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow);
        mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, disallow);
    }

    private void setUserRestriction(String restriction, boolean disallow) {
        if (disallow) {
            mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction);
        } else {
            mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction);
        }
    }

    private boolean isAdmin() {
        return mDevicePolicyManager.isDeviceOwnerApp("student.example.myapplication");
    }

}
