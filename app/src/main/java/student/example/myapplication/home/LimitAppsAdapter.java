 package student.example.myapplication.home;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.AppInfo;
import student.example.myapplication.admin.set.applimits.DailyLimitTime;

 public class
 LimitAppsAdapter extends BaseAdapter {
    private Context mContext;
    private LimitAppsList limitAppsList;
    private DailyLimitTime dailyLimitTime;
    //private List<AppInfo> list;

    public LimitAppsAdapter(Context context, LimitAppsList limitAppsList){
        this.mContext = context;
        this.limitAppsList = limitAppsList;
    }

    public int getCount(){
        return limitAppsList.getItems().length;
    }

    public Object getItem(int position){
        return limitAppsList.getItems()[position];
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        dailyLimitTime = new DailyLimitTime(mContext);
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.limit_app_list_item, null);

        ImageView app_icon = convertView.findViewById(R.id.app_icon);
        TextView app_name = convertView.findViewById(R.id.app_name);
        TextView limit_time = convertView.findViewById(R.id.limit_time);

        AppInfo appInfo = limitAppsList.getItems()[position];

        app_icon.setImageDrawable(appInfo.getDrawable());
        app_name.setText(appInfo.getAppName());
        long limitTime = dailyLimitTime.getLimitTimeInMS(appInfo.getPackageName());
        limit_time.setText(hmsTimeFormatter(limitTime));

        return convertView;
    }

     private String hmsTimeFormatter(long milliSeconds) {

         String hms = String.format("%02dh%02dm%02ds",
                 TimeUnit.MILLISECONDS.toHours(milliSeconds),
                 TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                 TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

         return hms;

     }
}
