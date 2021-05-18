package student.example.myapplication.usage;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.Utils;
import student.example.myapplication.usage.adapter.SelectDateAdapter;
import student.example.myapplication.usage.adapter.UseTimeAdapter;
import student.example.myapplication.usage.domain.UseTimeDataManager;
import student.example.myapplication.usage.utils.DateTransUtils;


public class UsageFragment extends Fragment {

    private UsageViewModel usageViewModel;

    private LinearLayout mLlSelectDate;
    private ToggleButton mBtnDate;
    private PopupWindow mPopupWindow;
    private RecyclerView mRvSelectDate;

    private RecyclerView mRecyclerView;
    private UseTimeAdapter mUseTimeAdapter;

    private ArrayList<String> mDateList;
    private UseTimeDataManager mUseTimeDataManager;

    private LinearLayout layout_permission;

    private int dayNum = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usageViewModel =
                ViewModelProviders.of(this).get(UsageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_usage, container, false);

        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        initData(dayNum);
        initView(root);

        layout_permission.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        });

        return root;
    }

    private void initView(View root){
        mLlSelectDate = (LinearLayout) root.findViewById(R.id.ll_select_date);
        mBtnDate = (ToggleButton) root.findViewById(R.id.tv_date);

        mBtnDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("onCheckedChanged", isChecked+"");
                if (isChecked) {
                    // The toggle is enabled
                    showPopWindow();
                } else {
                    // The toggle is disabled
                    mPopupWindow.dismiss();
                    mBtnDate.setTextOff(mDateList.get(dayNum));
                }
            }
        });

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_show_statistics);
        layout_permission = root.findViewById(R.id.layout_permission);
        showView(dayNum);
    }

    private void initData(int dayNum){
        mDateList = DateTransUtils.getSearchDays();
        mUseTimeDataManager = UseTimeDataManager.getInstance(getContext());
        mUseTimeDataManager.refreshData(dayNum);
    }

    private void showView(int dayNumber){

        dayNum = dayNumber;
        mBtnDate.setText(mDateList.get(dayNumber));

        mUseTimeAdapter = new UseTimeAdapter(getActivity(),mUseTimeDataManager.getmPackageInfoListOrderByTime());
        mUseTimeAdapter.setOnItemClickListener(new UseTimeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String pkg) {
                showDetail(pkg);
            }
        });
        mRecyclerView.setAdapter(mUseTimeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showDetail(String pkg){
        Intent i = new Intent();
        i.setClassName(getActivity(),"student.example.myapplication.usage.ui.UseTimeDetailActivity");
        i.putExtra("type","times");
        i.putExtra("pkg",pkg);
        startActivity(i);
    }

    private void showPopWindow(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popuplayout, null);
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mRvSelectDate = contentView.findViewById(R.id.rv_select_date);
        SelectDateAdapter adapter = new SelectDateAdapter(mDateList);
        adapter.setOnItemClickListener(new SelectDateAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mUseTimeDataManager.refreshData(position);
                showView(position);
                mPopupWindow.dismiss();
                mBtnDate.setChecked(false);

            }
        });
        mRvSelectDate.setAdapter(adapter);
        mRvSelectDate.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPopupWindow.showAsDropDown(mBtnDate);
    }

    @Override
    public void onResume() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(Utils.checkPermission(getActivity())){
                layout_permission.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                //initData(dayNum);
            } else{
                layout_permission.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
        super.onResume();
    }
}