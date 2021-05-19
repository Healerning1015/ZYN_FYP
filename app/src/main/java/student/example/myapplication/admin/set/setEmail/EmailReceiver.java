package student.example.myapplication.admin.set.setEmail;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import student.example.myapplication.usage.AppDir;
import student.example.myapplication.usage.SerializableMap;
import student.example.myapplication.usage.domain.PackageInfo;
import student.example.myapplication.usage.domain.UseTimeDataManager;

import student.example.myapplication.usage.BitmapUtil;
import student.example.myapplication.usage.utils.FileUtils;

public class EmailReceiver extends BroadcastReceiver {

    private UseTimeDataManager mUseTimeDataManager;
    private ArrayList<PackageInfo> mPackageInfoList;
    private PackageManager packageManager;
    private Map<String, String> map;
    private SerializableMap myMap;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("EmailReceiver","Here is EmailReceiver");
        FileUtils.deleteAllFile(AppDir.getInstance(context).IMAGE);

        mUseTimeDataManager = UseTimeDataManager.getInstance(context);
        mUseTimeDataManager.refreshData(1);

        mPackageInfoList = mUseTimeDataManager.getmPackageInfoListOrderByTime();

        packageManager = context.getPackageManager();

        Intent i = new Intent(context, ServiceSendEmail.class);
        i.putExtra("email_title", "Usage Feedback of " + getYesterday());
        i.putExtra("email_content", htmlContent(context));

        map = new HashMap<>();
        serializableMap(context);
        i.putExtra("image_path_map", myMap);

        ServiceSendEmail.enqueueWork(context, i);
    }

    public void serializableMap(Context context){
        int showNum;
        if(mPackageInfoList.size()>=10){
            showNum = 10;
        } else {
            showNum = mPackageInfoList.size();
        }
        for (int i = 0; i < showNum; i++){
            try {
                Bitmap bitmap = BitmapUtil.drawableToBitmap(packageManager.getApplicationIcon(mPackageInfoList.get(i).getmPackageName()));
                String pngFilePath = AppDir.getInstance(context).IMAGE + File.separator + System.currentTimeMillis() + ".png";
                BitmapUtil.saveBitmapToSDCard(bitmap,pngFilePath);
                map.put("logo_" + i, pngFilePath);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        myMap = new SerializableMap();
        //将map数据添加到封装的myMap中
        myMap.setMap(map);
    }

    public static String getYesterday() {
        int distanceDay = -1;
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public String htmlContent(Context context){
        String htmlText = "<p>The following table shows the usage of device yesterday. ("+ getYesterday() +")</p>" +
                "<p>Due to the mailbox limitation, only ten applications are displayed. If you need to check the detailed usage, please use the APP to check.</p>" +
                "<table cellspacing='10'>" +
                "<tr><th></th><th>App Name</th><th>Use Count</th><th>Use Time</th></tr>";
        int showNum;
        if(mPackageInfoList.size()>=10){
            showNum = 10;
        } else {
            showNum = mPackageInfoList.size();
        }

        for (int i = 0; i < showNum; i++){
            htmlText += "<tr>";
            htmlText += "<td><img style='display:block; width:40px;height:40px;' src='cid:logo_"+ i +"' /></td>";

            try {
                android.content.pm.PackageInfo info = null;
                info = packageManager.getPackageInfo(mPackageInfoList.get(i).getmPackageName(),PackageManager.GET_ACTIVITIES);
                htmlText += "<td>"+ info.applicationInfo.loadLabel(packageManager).toString() +"</td>";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                htmlText += "<td>Uninstalled application</td>";
            }
            if(mPackageInfoList.get(i).getmUsedCount() != 0){
                htmlText += "<td>"+ mPackageInfoList.get(i).getmUsedCount() +"</td>";
            } else {
                htmlText += "<td>x</td>";
            }
            htmlText += "<td>"+ hmsTimeFormatter(getTotalTimeFromUsage(mPackageInfoList.get(i).getmPackageName())) +"</td>" +
                    "</tr>";
        }
        htmlText += "</table>";
        return htmlText;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private long getTotalTimeFromUsage(String pkg){
        UsageStats stats = mUseTimeDataManager.getUsageStats(pkg);
        if(stats == null){
            return 0;
        }
        return stats.getTotalTimeInForeground();
    }

    private String hmsTimeFormatter(long milliSeconds) {
        String hms = String.format("%02dh %02dm %02ds",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

}
