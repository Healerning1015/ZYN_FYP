package student.example.myapplication.admin.set.applimits;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable {

    private String appid;
    String appName;
    String packageName;
    transient Drawable drawable;
    int hours;
    int mins;
    boolean alwaysAllowed;

    public AppInfo(){}

    public AppInfo(String appName){
        this.appName = appName;
    }

    public AppInfo(String appName, String packageName, Drawable drawable){
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
    }

    public AppInfo(String packageName, int hours, int mins) {
        this.packageName = packageName;
        this.hours = hours;
        this.mins = mins;
    }

    public AppInfo(String appName, String packageName, Drawable drawable, int hours, int mins, boolean alwaysAllowed){
        //this.appid = appid;
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
        this.hours = hours;
        this.mins = mins;
        this.alwaysAllowed = alwaysAllowed;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppName() {
        if(null == appName)
            return "";
        else
            return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        if(null == packageName)
            return "";
        else
            return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getHours(){
        return hours;
    }

    public void setHours(int hours){
        this.hours = hours;
    }

    public int getMins(){
        return mins;
    }

    public void setMins(int mins){
        this.mins = mins;
    }

    public boolean getAlwaysAllowed(){
        return alwaysAllowed;
    }

    public void setAlwaysAllowed(boolean alwaysAllowed){
        this.alwaysAllowed = alwaysAllowed;
    }
}


