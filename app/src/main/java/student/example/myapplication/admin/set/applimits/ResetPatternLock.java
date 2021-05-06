package student.example.myapplication.admin.set.applimits;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.shuhart.stepview.StepView;

import java.util.List;

import student.example.myapplication.R;
import student.example.myapplication.admin.services.BackgroundManager;

public class ResetPatternLock extends AppCompatActivity {

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

        BackgroundManager.getInstance().init(this).startService();

        initIconApp();
        initLayout();

        initPatternListener();

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
                    if(utilsPassword.isSecondStep()){
                        userPassword = pwd;
                        utilsPassword.setSecondStep(false);
                        status_password.setText(utilsPassword.STATUS_NEXT_STEP);
                        stepView.go(2, true);
                    }else{
                        if(userPassword.equals(pwd)){
                            utilsPassword.setPassword(userPassword);
                            Toast.makeText(ResetPatternLock.this, "set!", Toast.LENGTH_SHORT).show();
                            status_password.setText(utilsPassword.STATUS_PASSWORD_CORRECT);
                            stepView.done(true);

                            finish();
                        }else{
                            status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                        }
                    }
                }

                else{
                    if(utilsPassword.isCorrect(pwd) && utilsPassword.isFirstStep()){
                        utilsPassword.clearPassword();
                        utilsPassword.setFirstStep(false);
                        status_password.setText(utilsPassword.STATUS_NEW_FIRST_STEP);
                        stepView.go(1, true);
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

    private void initIconApp() {
        if(getIntent().getStringExtra("broadcast_receiver") != null){
            ImageView icon = findViewById(R.id.app_icon);
            String current_app = new Utils(this).getLastApp();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = getPackageManager().getApplicationInfo(current_app,0);
                icon.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initLayout() {
        stepView = findViewById(R.id.step_view);
        normalLayout = findViewById(R.id.normal_layout);
        relativeLayout = findViewById(R.id.root);
        status_password = findViewById(R.id.status_password);
        status_password.setTextColor(ResourcesCompat.getColor(getResources(),R.color.blue_4, null));
        utilsPassword = new PatternPassword(this);
        status_password.setText(utilsPassword.STATUS_CHANGE_PASSWORD_CONFIRM);

        normalLayout.setVisibility(View.GONE);
        utilsPassword.setFirstStep(true);
        stepView.setVisibility(View.VISIBLE);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
    }

    @Override
    public void onBackPressed() {
        if(utilsPassword.getPassword() == null && !utilsPassword.isFirstStep() && utilsPassword.isSecondStep()){
            Toast.makeText(this, "You cannot go back to the previous step, please set a new password.", Toast.LENGTH_SHORT).show();
        } else if(utilsPassword.getPassword() == null && !utilsPassword.isFirstStep() && !utilsPassword.isSecondStep()) {
            stepView.go(1, true);
            utilsPassword.setSecondStep(true);
            utilsPassword.clearPassword();
            Toast.makeText(this, "clear!", Toast.LENGTH_SHORT).show();
            status_password.setText(utilsPassword.STATUS_NEW_FIRST_STEP);
        } else {
            finish();
            super.onBackPressed();
        }
    }

}

