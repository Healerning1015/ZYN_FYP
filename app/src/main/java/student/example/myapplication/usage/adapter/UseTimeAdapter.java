package student.example.myapplication.usage.adapter;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import student.example.myapplication.R;
import student.example.myapplication.usage.domain.OneTimeDetails;
import student.example.myapplication.usage.domain.PackageInfo;
import student.example.myapplication.usage.domain.UseTimeDataManager;


public class UseTimeAdapter extends RecyclerView.Adapter<UseTimeAdapter.UseTimeViewHolder>{

    private ArrayList<PackageInfo> mPackageInfoList;
    private PackageManager packageManager;
    private UseTimeDataManager mUseTimeDataManager;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public UseTimeAdapter(Context context, ArrayList<PackageInfo> PackageInfoList) {
        this.mPackageInfoList = PackageInfoList;
        mUseTimeDataManager = UseTimeDataManager.getInstance(context);
    }

    public void modifyData(ArrayList<PackageInfo> List){
        mPackageInfoList = List;
    }

    //define interface
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String pkg);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public UseTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        packageManager = parent.getContext().getPackageManager();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.used_time_item_layout, parent, false);
        UseTimeViewHolder holder = new UseTimeViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(UseTimeViewHolder holder, final int position) {
        holder.tv_index.setText("" + (position+1) );
        try {
            holder.iv_icon.setImageDrawable(packageManager.getApplicationIcon(mPackageInfoList.get(position).getmPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(mPackageInfoList.get(position).getmUsedCount() != 0){
            holder.tv_used_count.setText(" " + mPackageInfoList.get(position).getmUsedCount()+"");
        } else {
            holder.tv_used_count.setText(" x");
        }
        holder.tv_calculate_used_time.setText(" " + DateUtils.formatElapsedTime(getTotalTimeFromUsage(mPackageInfoList.get(position).getmPackageName())/1000));
        //DateTransUtils.formatElapsedTime(mPackageInfoList.get(position).getmUsedTime()/1000)
        try {
            android.content.pm.PackageInfo info = null;
            info = packageManager.getPackageInfo(mPackageInfoList.get(position).getmPackageName(),PackageManager.GET_ACTIVITIES);
            holder.tv_app_name.setText(info.applicationInfo.loadLabel(packageManager).toString());
            Log.i("APP Name: ",holder.tv_app_name.getText()+"");

            //holder.tv_app_name.setText(packageManager.getApplicationLabel(packageManager.getApplicationInfo(mPackageInfoList.get(position).getmPackageName(), 0)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,mPackageInfoList.get(position).getmPackageName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPackageInfoList.size();
    }

    public class UseTimeViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_index;
        public ImageView iv_icon;
        public TextView tv_used_count;
        public TextView tv_app_name;
        public TextView tv_calculate_used_time;


        public UseTimeViewHolder(View itemView) {
            super(itemView);
            tv_index = (TextView) itemView.findViewById(R.id.index);
            iv_icon = (ImageView) itemView.findViewById(R.id.app_icon);
            tv_used_count = (TextView) itemView.findViewById(R.id.use_count);
            tv_app_name = (TextView) itemView.findViewById(R.id.use_time_app_name);
            tv_calculate_used_time = (TextView) itemView.findViewById(R.id.calculate_use_time);
        }
    }

    private long calculateUseTime(ArrayList<OneTimeDetails> list, String pkg){
        long useTime = 0 ;
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getPkgName().equals(pkg)){
                useTime += list.get(i).getUseTime();
            }
        }

        return useTime;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private long getTotalTimeFromUsage(String pkg){
        UsageStats stats = mUseTimeDataManager.getUsageStats(pkg);
        if(stats == null){
            return 0;
        }
        return stats.getTotalTimeInForeground();
    }
}
