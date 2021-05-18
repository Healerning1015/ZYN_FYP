

package student.example.myapplication.admin.set.applimits;

import java.util.List;

import student.example.myapplication.R;
import student.example.myapplication.admin._interface.AppOnClickListener;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

public class AppInfosAdapter extends BaseAdapter {


    Context context;
    List<AppInfo> appInfos;
    private Utils utils;

    public AppInfosAdapter(){}

    public AppInfosAdapter(Context context , List<AppInfo> infos ){
        this.context = context;
        this.appInfos = infos;
        this.utils = new Utils(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(null != appInfos){
            return appInfos.size();
        };
        return count;
    }

    @Override
    public Object getItem(int index) {
        return appInfos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        final AppInfo appInfo = appInfos.get(position);
        ViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.app_info_item, null);
            viewHolder.appIconImg = (ImageView)convertView.findViewById(R.id.app_icon);
            viewHolder.appNameText = (TextView)convertView.findViewById(R.id.app_info_name);
            //viewHolder.appPackageText = (TextView)convertView.findViewById(R.id.app_info_package_name);
            viewHolder.itemLayout = (LinearLayout)convertView.findViewById(R.id.item_layout);
            //viewHolder.appLimitTime = (TextView)convertView.findViewById(R.id.app_limit_time);
            viewHolder.appLock = (ImageView) convertView.findViewById(R.id.app_lock);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(null != appInfos){
            viewHolder.appIconImg.setImageDrawable(appInfo.getDrawable());
            viewHolder.appNameText.setText(appInfo.getAppName());
            //viewHolder.appPackageText.setText(appInfo.getPackageName());
            //viewHolder.appLimitTime.setText(appInfo.getHours() + " h " + appInfo.getMins() + " min");
            if(utils.isLock(appInfo.getPackageName())){
                viewHolder.appLock.setImageResource(R.drawable.ic_baseline_lock_24);
            } else {
                viewHolder.appLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
            }

            /*
            if(!appInfo.getAlwaysAllowed()){
                viewHolder.appLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                //Log.i("is Lock? ",utils.isLock(pk)+"");
                //Toast.makeText(mContext, "is Lock? "+utils.isLock(pk), Toast.LENGTH_SHORT).show();
            } else {
                viewHolder.appLock.setImageResource(R.drawable.ic_baseline_lock_24);
                //Log.i("is Lock? ",utils.isLock(pk)+"");
                //Toast.makeText(mContext, "is Lock? "+utils.isLock(pk), Toast.LENGTH_SHORT).show();
            }

             */
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView appIconImg;
        TextView  appNameText;
        TextView  appPackageText;
        TextView  appLimitTime;
        ImageView appLock;
        LinearLayout itemLayout;

    }


}

