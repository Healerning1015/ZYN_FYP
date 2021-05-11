package student.example.myapplication.admin.set.applimits;

import android.content.Context;

import io.paperdb.Paper;

public class DailyLimitTime {
    private Context context;

    public DailyLimitTime(Context context){
        this.context = context;
        Paper.init(context);
    }

    public void setLimitTimeInMS(String pkgName, long LimitTimeInMS){
        Paper.book().write(pkgName, LimitTimeInMS);
    }

    public long getLimitTimeInMS(String pkgName){
        return Paper.book().read(pkgName);
    }

    public void deleteLimitTimeInMS(String pkgName){
        Paper.book().delete(pkgName);
    }
}
