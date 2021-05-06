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

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.AppInfo;

public class LimitAppsAdapter extends BaseAdapter {
    private Context mContext;
    private LimitAppsList limitAppsList;
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
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.limit_app_list_item, null);

        ImageView app_icon = convertView.findViewById(R.id.app_icon);
        TextView app_name = convertView.findViewById(R.id.app_name);
        TextView limit_time = convertView.findViewById(R.id.limit_time);

        AppInfo appInfo = limitAppsList.getItems()[position];

        app_icon.setImageDrawable(appInfo.getDrawable());
        app_name.setText(appInfo.getAppName());
        limit_time.setText(appInfo.getHours() + "h " + appInfo.getMins() + "min");

        return convertView;
    }
}
