package student.example.myapplication.usage;


import android.content.Intent;
import android.os.Bundle;
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

    private RecyclerView       mRecyclerView;
    private UseTimeAdapter mUseTimeAdapter;

    private ArrayList<String> mDateList;
    private UseTimeDataManager mUseTimeDataManager;

    private int dayNum = 0;
    private boolean isShowing = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usageViewModel =
                ViewModelProviders.of(this).get(UsageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_usage, container, false);

        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        initData(dayNum);
        initView(root);

        return root;
    }

    private void initView(View root){
        mLlSelectDate = (LinearLayout) root.findViewById(R.id.ll_select_date);
        mBtnDate = (ToggleButton) root.findViewById(R.id.tv_date);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_show_statistics);
        showView(dayNum);
    }

    private void initData(int dayNum){
        mDateList = DateTransUtils.getSearchDays();
        mUseTimeDataManager = UseTimeDataManager.getInstance(getContext());
        mUseTimeDataManager.refreshData(dayNum);
    }

    private void showView(int dayNumber){

        mBtnDate.setText(mDateList.get(dayNumber));

        mBtnDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    showPopWindow();
                } else {
                    // The toggle is disabled
                    mPopupWindow.dismiss();
                }
            }
        });

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
                isShowing = false;
            }
        });
        mRvSelectDate.setAdapter(adapter);
        mRvSelectDate.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPopupWindow.showAsDropDown(mBtnDate);
        isShowing = true;
    }
}