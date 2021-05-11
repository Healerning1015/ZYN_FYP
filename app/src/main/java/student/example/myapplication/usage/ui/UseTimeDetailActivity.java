package student.example.myapplication.usage.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import student.example.myapplication.R;
import student.example.myapplication.usage.adapter.UseTimeDetailAdapter;
import student.example.myapplication.usage.domain.OneTimeDetails;
import student.example.myapplication.usage.domain.UseTimeDataManager;


public class UseTimeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private UseTimeDetailAdapter mUseTimeDetailAdapter;

    private UseTimeDataManager mUseTimeDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_time_detail);

        Log.i(UseTimeDataManager.TAG,"  UseTimeDetailActivity      ");
        mUseTimeDataManager = UseTimeDataManager.getInstance(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_use_time_detail);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        initView(type,intent);
    }

    private void initView(String type, Intent intent){
        if("times".equals(type)){
            //显示为次数统计信息
            showAppOpenTimes(intent.getStringExtra("pkg"));
            //getSupportActionBar().setTitle(R.string.action_bar_title_2);
            PackageInfo info = null;
            PackageManager pm = getPackageManager();
            try {
                info = pm.getPackageInfo(intent.getStringExtra("pkg"),PackageManager.GET_UNINSTALLED_PACKAGES);
                getSupportActionBar().setTitle(info.applicationInfo.loadLabel(pm).toString());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            Log.i(UseTimeDataManager.TAG,"   未知类型    ");
        }
    }


    private void showAppOpenTimes(String pkg){
        mUseTimeDetailAdapter = new UseTimeDetailAdapter(mUseTimeDataManager.getPkgOneTimeDetailList(pkg));
        mUseTimeDetailAdapter.setOnItemClickListener(new UseTimeDetailAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, OneTimeDetails details) {
                mUseTimeDataManager.setmOneTimeDetails(details);
            }
        });
        mRecyclerView.setAdapter(mUseTimeDetailAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
