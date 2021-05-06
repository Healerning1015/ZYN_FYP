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
import student.example.myapplication.usage.adapter.UseTimeEveryDetailAdapter;
import student.example.myapplication.usage.domain.OneTimeDetails;
import student.example.myapplication.usage.domain.UseTimeDataManager;


public class UseTimeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private UseTimeDetailAdapter mUseTimeDetailAdapter;

    private UseTimeEveryDetailAdapter mUseTimeEveryDetailAdapter;

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
        }else if("details".equals(type)){
            //显示为activity统计信息
            showAppOpenDetails(intent.getStringExtra("pkg"));
            getSupportActionBar().setTitle(R.string.action_bar_title_3);
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
                showDetail(details.getPkgName());
            }
        });
        mRecyclerView.setAdapter(mUseTimeDetailAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void showAppOpenDetails(String pkg){
        if(!pkg.equals(mUseTimeDataManager.getmOneTimeDetails().getPkgName())){
            Log.i(UseTimeDataManager.TAG,"  showAppOpenDetails()    包名不一致 ");
        }
        mUseTimeEveryDetailAdapter = new UseTimeEveryDetailAdapter(mUseTimeDataManager.getmOneTimeDetails().getOneTimeDetailEventList());
        mRecyclerView.setAdapter(mUseTimeEveryDetailAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showDetail(String pkg){
        Intent i = new Intent();
        i.setClassName(this,"student.example.myapplication.usage.ui.UseTimeDetailActivity");
        i.putExtra("type","details");
        i.putExtra("pkg",pkg);
        startActivity(i);
    }
}
