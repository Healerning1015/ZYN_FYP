package student.example.myapplication.admin.set.applimits;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.shuhart.stepview.StepView;

import java.util.List;

import student.example.myapplication.MainActivity;
import student.example.myapplication.R;
import student.example.myapplication.admin.services.BackgroundManager;
import student.example.myapplication.home.LearningModule;

public class PatternLockPage extends AppCompatActivity {

    StepView stepView;
    LinearLayout normalLayout;
    TextView status_password;
    RelativeLayout relativeLayout;

    PatternPassword utilsPassword;
    String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_limit_pattern_lock);

        this.getSupportActionBar().hide();

        //Log.e("Current Class","PatternLockAct");
        BackgroundManager.getInstance().init(this).startService();
        initIconApp();
        initLayout();

        initPatternListener();

        registerReceiver();

    }

    private void initPatternListener() {
        final PatternLockView patternLockView = findViewById(R.id.pattern_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                String pwd = PatternLockUtils.patternToString(patternLockView, pattern);
                if(pwd.length() < 4){
                    status_password.setText(utilsPassword.SHEMA_FAILED);
                    patternLockView.clearPattern();
                    return;
                }

                if(utilsPassword.getPassword() == null){
                    if(utilsPassword.isFirstStep()){
                        userPassword = pwd;
                        utilsPassword.setFirstStep(false);
                        status_password.setText(utilsPassword.STATUS_NEXT_STEP);
                        stepView.go(1, true);
                    }else{
                        if(userPassword.equals(pwd)){
                            utilsPassword.setPassword(userPassword);
                            Toast.makeText(PatternLockPage.this, "set!", Toast.LENGTH_SHORT).show();
                            status_password.setText(utilsPassword.STATUS_PASSWORD_CORRECT);
                            stepView.done(true);

                            startAct();
                        }else{
                            status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                        }
                    }
                }
                else{
                    if(utilsPassword.isCorrect(pwd)){
                        status_password.setText(utilsPassword.STATUS_PASSWORD_CORRECT);
                        startAct();
                    } else {
                        status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                    }
                }

                patternLockView.clearPattern();

            }

            @Override
            public void onCleared() {

            }
        });
    }

    private void initLayout() {

        stepView = findViewById(R.id.step_view);
        normalLayout = findViewById(R.id.normal_layout);
        relativeLayout = findViewById(R.id.root);
        status_password = findViewById(R.id.status_password);
        status_password.setTextColor(ResourcesCompat.getColor(getResources(),R.color.blue_4, null));
        utilsPassword = new PatternPassword(this);
        status_password.setText(utilsPassword.STATUS_FIRST_STEP);

        if(utilsPassword.getPassword() == null){
            normalLayout.setVisibility(View.GONE);
            stepView.setVisibility(View.VISIBLE);
            stepView.setStepsNumber(2);
            stepView.go(0, true);
        } else{
            normalLayout.setVisibility(View.VISIBLE);
            stepView.setVisibility(View.GONE);
            int backColor = ResourcesCompat.getColor(getResources(),R.color.blue_2, null);
            relativeLayout.setBackgroundColor(backColor);
        }
    }

    //这里貌似会出现bug
    private void initIconApp() {
        if(getIntent().getStringExtra("broadcast_receiver") != null){
            Log.e("PatternLockPage", "Go Here");
            ImageView icon = findViewById(R.id.app_icon);
            String current_app = new Utils(this).getLastApp();
            ApplicationInfo applicationInfo = null;
            try {
                Log.e("current_app", current_app);
                applicationInfo = getPackageManager().getApplicationInfo(current_app,0);
                icon.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void forgetPwd(View view) {
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.fragment_admin, null);
        Button button = dialogView.findViewById(R.id.enter);
        button.setVisibility(View.GONE);
        builder.setView(dialogView);

        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PatternLockPage.this, "你点击了是的", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PatternLockPage.this, "你点击了不是", Toast.LENGTH_SHORT).show();
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

    private void startAct(){
        //setKioskPolicies(false, isAdmin);

        if(getIntent().getStringExtra("broadcast_receiver") == null){
            startActivity(new Intent(this, AppLimits.class));
        }
        if(getIntent().getStringExtra("learning") != null){
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        //Log.e("PatternLockAct", "onBackPressed()");
        if(utilsPassword.getPassword() == null && !utilsPassword.isFirstStep()){
            //Log.e("PatternLockAct", "onBackPressed() if");
            stepView.go(0, true);
            utilsPassword.setFirstStep(true);
            status_password.setText(utilsPassword.STATUS_FIRST_STEP);
        } else {

            if(getIntent().getStringExtra("app_limits") != null){
                finish();
            } else {
                startCurrentHomePackage();
                finish();
                super.onBackPressed();
            }
        }
    }

    private void startCurrentHomePackage() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);

        new Utils(this).clearLastApp();
    }









    private RecentAppsReceiver recentAppsReceiver;

    //注册广播
    public void registerReceiver() {
        recentAppsReceiver = new RecentAppsReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(recentAppsReceiver, homeFilter);
    }

    //用于监听最近任务按钮 防止杀死进程丢失数据
    private class RecentAppsReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (reason == null) {
                    return;
                }
                // Home键
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    Log.i("","按了Home键");
                    if(getIntent().getStringExtra("app_limits") == null){
                        new Utils(context).clearLastApp();
                    }
                }
                // 最近任务列表键
                if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    Log.i("","按了最近任务列表");
                    if(getIntent().getStringExtra("app_limits") == null){
                        new Utils(context).clearLastApp();
                    }
                }
            }
        }
    }


}
