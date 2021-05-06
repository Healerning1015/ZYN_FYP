package student.example.myapplication.usage.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;

/**
 * Created by Wingbu on 2017/9/13.
 */

public class BaseActivity extends AppCompatActivity {

    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar(){
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            mActionBar.setCustomView(R.layout.actionbar_custom);//设置自定义的布局：actionbar_custom
            TextView tv_action_title = mActionBar.getCustomView().findViewById(R.id.action_bar_title);
            tv_action_title.setText(R.string.action_bar_title_1);

            ImageView iv_setting = mActionBar.getCustomView().findViewById(R.id.iv_setting);
            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jumpToSystemPermissionActivity();
                }
            });

            ImageView iv_content = mActionBar.getCustomView().findViewById(R.id.iv_content);
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDetail("all");
                }
            });
        }
    }

    private void jumpToSystemPermissionActivity(){
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void showDetail(String pkg){
        Intent i = new Intent();
        i.setClassName(this,"student.example.myapplication.usage.ui.UseTimeDetailActivity");
        i.putExtra("type","times");
        i.putExtra("pkg",pkg);
        startActivity(i);
    }

    protected void setActionBarTitle(String title){
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            mActionBar.setCustomView(R.layout.actionbar_custom);//设置自定义的布局：actionbar_custom
            TextView tv_action_title = mActionBar.getCustomView().findViewById(R.id.action_bar_title);
            tv_action_title.setText(title);
        }
    }

    protected void setActionBarTitle(int stringId){
        setActionBarTitle(getString(stringId));
    }
}
