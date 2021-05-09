package student.example.myapplication.admin.set.applimits;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin._interface.AppOnClickListener;
import student.example.myapplication.home.LimitAppsList;

public class AddLimitActivity extends AppCompatActivity {

    private Utils utils;
    private AppLimitsDB appLimitsDB;
    private LimitAppsList limitAppsList = new LimitAppsList();
    private boolean flag = true;
    private boolean edit;
    private AppInfo editAppInfo = new AppInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_limits_dialog);

        utils = new Utils(getApplicationContext());

        appLimitsDB = new AppLimitsDB(this);
        limitAppsList = appLimitsDB.getAllLimitApps(limitAppsList);
        final AppInfo[] appInfos = limitAppsList.getItems();

        Intent intent = getIntent();
        final AppInfo appInfo = (AppInfo) intent.getSerializableExtra("appInfo");
        final int pos = (int) intent.getIntExtra("pos", 0);

        final byte[] byteLogo = intent.getByteArrayExtra("byteLogo");


        final TextView app_name = findViewById(R.id.app_name);
        final Spinner hours = findViewById(R.id.spinner_hour);
        final Spinner mins = findViewById(R.id.spinner_min);
        final Switch limit_use = findViewById(R.id.switch_limit_use);
        final Switch always = findViewById(R.id.switch_always);
        final Button btn_cancel = findViewById(R.id.cancel);
        final Button btn_save = findViewById(R.id.save);

        app_name.setText(appInfo.appName);

        always.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    limit_use.setChecked(false);
                    //limit_use.set
                    hours.setEnabled(false);
                    mins.setEnabled(false);
                    flag = false;
                }
            }
        });

        limit_use.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //limit_use.setChecked(true);
                if(isChecked) {
                    always.setChecked(false);
                    hours.setEnabled(true);
                    //hours.setClickable(false);
                    mins.setEnabled(true);
                    flag = true;
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < appInfos.length; i++){
                    edit = false;
                    if(appInfo.getPackageName().equals(appInfos[i].getPackageName())){
                        edit = true;
                        //Log.e("edit", "true");
                        editAppInfo = appInfos[i];
                        //Log.e("appInfos[i]", "appInfos[i].getPackageName() = " + appInfos[i].getPackageName());
                        break;
                    }
                }

                Intent i = new Intent(AddLimitActivity.this, AppLimits.class);
                i.putExtra("pos", pos);
                if(flag == true && (Integer.parseInt(hours.getSelectedItem().toString()) != 0 || Integer.parseInt(mins.getSelectedItem().toString()) != 0)){
                    appInfo.setHours(Integer.parseInt(hours.getSelectedItem().toString()));
                    appInfo.setMins(Integer.parseInt(mins.getSelectedItem().toString()));
                    if(edit == false){
                        appLimitsDB.addLimit(appInfo.getAppName(), appInfo.getPackageName(), byteLogo, appInfo.getHours(), appInfo.getMins(), appInfo.getAlwaysAllowed());
                        utils.lock(appInfo.getPackageName());
                        i.putExtra("add_limit_activity", "add_ok");
                        setResult(RESULT_OK, i);
                        finish();
                        //Toast.makeText(AddLimitActivity.this, "Successful add limit.", Toast.LENGTH_LONG).show();
                    } else{
                        appLimitsDB.updateLimit(editAppInfo, appInfo.getHours(), appInfo.getMins(), appInfo.getAlwaysAllowed());
                        utils.lock(appInfo.getPackageName());
                        i.putExtra("add_limit_activity", "change_ok");
                        setResult(RESULT_OK, i);
                        finish();
                        //Toast.makeText(AddLimitActivity.this, "Successful change limit.", Toast.LENGTH_LONG).show();
                    }
                } else if(flag == false && edit == true){
                    appLimitsDB.deleteLimit(editAppInfo);
                    utils.unlock(appInfo.getPackageName());
                    i.putExtra("add_limit_activity", "delete_ok");
                    setResult(RESULT_OK, i);
                    finish();
                    //Toast.makeText(AddLimitActivity.this, "Successful delete limit.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AddLimitActivity.this, "Please set limit time.", Toast.LENGTH_LONG).show();
                }


            }
        });

        appLimitsDB.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(appLimitsDB != null){
            appLimitsDB.close();
        }
    }

}
