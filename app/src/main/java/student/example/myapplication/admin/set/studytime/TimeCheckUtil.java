package student.example.myapplication.admin.set.studytime;

import android.util.Log;

import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;

public class TimeCheckUtil {

    //比较两个时间段数组是否有重合，有重合返回true
    public static boolean compare(ArrayList<Integer> timeArray1,
                                  ArrayList<Integer> timeArray2) {
        String logTimeArray1 = "";
        for (int i : timeArray1){
            logTimeArray1 += i+" ";
        }
        Log.w("timeArray1", logTimeArray1);

        String logTimeArray2 = "";
        for (int i : timeArray2){
            logTimeArray2 += i+" ";
        }
        Log.w("timeArray2", logTimeArray2);

        for (int i : timeArray1) {
            if (timeArray2.contains(i))
                return true;
        }
        return false;
    }

    //将时间段转换成数组
    public static ArrayList<Integer> checkList(Schedule schedule) {
        // 将时间段封装成一个数组
        ArrayList<Integer> timeArray = new ArrayList<Integer>();
        // 先将时间转换成分来计算
        int timeStart = (schedule.getStartTime().getHour() * 60) + schedule.getStartTime().getMinute();
        int timeEnd = (schedule.getEndTime().getHour() * 60) + schedule.getEndTime().getMinute();
        if (timeEnd > timeStart) {// 开始时间小于结束时间
            for (int i = timeStart; i <= timeEnd; i++) {
                timeArray.add(i);// 添加开始时间至结束时间为止的时间
            }
        } else {// 开始时间大于结束时间
            for (int i = timeStart; i < 24 * 60; i++) {
                timeArray.add(i);// 添加开始时间至当天0点以前的剩余时间
            }
            for (int i = 0; i <= timeEnd; i++) {
                timeArray.add(i);// 添加0点以后到结束时间为止的时间
            }
        }
        return timeArray;
    }

}
