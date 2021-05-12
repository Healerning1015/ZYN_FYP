package student.example.myapplication.home;import android.content.Intent;import android.content.SharedPreferences;import android.os.Bundle;import android.preference.PreferenceManager;import android.util.DisplayMetrics;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.AdapterView;import android.widget.GridView;import android.widget.LinearLayout;import android.widget.ProgressBar;import android.widget.TextView;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.appcompat.app.AlertDialog;import androidx.appcompat.app.AppCompatActivity;import androidx.fragment.app.Fragment;import androidx.lifecycle.ViewModelProviders;import com.github.tlaabs.timetableview.Schedule;import com.github.tlaabs.timetableview.TimetableView;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import org.w3c.dom.Text;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.concurrent.TimeUnit;import student.example.myapplication.R;import student.example.myapplication.admin.set.applimits.AppInfo;import student.example.myapplication.admin.set.applimits.AppLimitsDB;import student.example.myapplication.admin.set.applimits.DailyLimitTime;import student.example.myapplication.admin.set.studytime.AddStudyTime;import student.example.myapplication.admin.set.studytime.AlarmManagerUtils;public class HomeFragment extends Fragment {    private HomeViewModel homeViewModel;    private TimetableView timetable;    public static final int REQUEST_ADD = 1;    public static final int REQUEST_BORROW = 4;    private TextView textView_no_limits;    private GridView gridView;    private AppLimitsDB appLimitsDB;    public LimitAppsList limitAppsList = new LimitAppsList();    private LimitAppsAdapter adapter;    private AlarmManagerUtils alarmManagerUtils;    private DailyLimitTime dailyLimitTime;    public View onCreateView(@NonNull LayoutInflater inflater,                             ViewGroup container, Bundle savedInstanceState) {        homeViewModel =                ViewModelProviders.of(this).get(HomeViewModel.class);        View view = inflater.inflate(R.layout.fragment_home, container, false);        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();        alarmManagerUtils = AlarmManagerUtils.getInstance(getActivity());        dailyLimitTime = new DailyLimitTime(getActivity());        timetable = view.findViewById(R.id.timetable);        long time=System.currentTimeMillis();        Date date=new Date(time);        SimpleDateFormat format=new SimpleDateFormat("EEEE");        Log.e("time","time=" + format.format(date));        int todayIs = 0;        switch(format.format(date)){            case "Monday":                todayIs = 1;                break;            case "Tuesday":                todayIs = 2;                break;            case "Wednesday":                todayIs = 3;                break;            case "Thursday":                todayIs = 4;                break;            case "Friday":                todayIs = 5;                break;            case "Saturday":                todayIs = 6;                break;            case "Sunday":                todayIs = 7;                break;            case "星期一":                todayIs = 1;                break;            case "星期二":                todayIs = 2;                break;            case "星期三":                todayIs = 3;                break;            case "星期四":                todayIs = 4;                break;            case "星期五":                todayIs = 5;                break;            case "星期六":                todayIs = 6;                break;            case "星期日":                todayIs = 7;                break;        }        timetable.setHeaderHighlight(todayIs);        loadSavedData();        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {            @Override            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {                Intent i = new Intent(getActivity(), AddStudyTime.class);                i.putExtra("mode", REQUEST_BORROW);                i.putExtra("idx", idx);                i.putExtra("schedules", schedules);                startActivityForResult(i, REQUEST_BORROW);            }        });        //LimitApp GridView        appLimitsDB = new AppLimitsDB(getActivity());        limitAppsList = appLimitsDB.getAllLimitApps(limitAppsList);        textView_no_limits = view.findViewById(R.id.textView_no_limits);        gridView = view.findViewById(R.id.grid);        adapter = new LimitAppsAdapter(getActivity(), limitAppsList);        int size = adapter.getCount();        int length = 90;        DisplayMetrics dm = new DisplayMetrics();        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);        float density = dm.density;        int gridviewWidth = (int) (size * (length + 4) * density);        int itemWidth = (int) (length * density);        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键        gridView.setColumnWidth(itemWidth); // 设置列表项宽        gridView.setHorizontalSpacing(5); // 设置列表项水平间距        gridView.setStretchMode(GridView.NO_STRETCH);        gridView.setNumColumns(size); // 设置列数量=列表集合数        gridView.setAdapter(adapter);        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            @Override            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {                createDialog(limitAppsList.getItems()[position]);            }        });        appLimitsDB.close();        return view;    }    private void loadSavedData(){        timetable.removeAll();        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());        String savedData = mPref.getString("timetable","");        if(savedData == null && savedData.equals("")) return;        Log.e("is JSON Valid:  ", isJSONValid(savedData)+"");        if(isJSONValid(savedData)){            timetable.load(savedData);            Log.e("savedData:  ", savedData);            try {                //String转JSONObject                JSONObject result = new JSONObject(savedData);                JSONArray sticker = result.optJSONArray("sticker");                for(int i=0; i < sticker.length(); i++){                    JSONObject jsonObject1 = sticker.getJSONObject(i);                    int idx = Integer.parseInt(jsonObject1.optString("idx"));                    Log.e("idx", idx+"");                    JSONArray schedule = jsonObject1.optJSONArray("schedule");                    for(int j=0; j < schedule.length(); j++){                        JSONObject jsonObject2 = schedule.getJSONObject(j);                        int day = Integer.parseInt(jsonObject2.optString("day"));                        JSONObject startTime = jsonObject2.optJSONObject("startTime");                        int start_hour = Integer.parseInt(startTime.optString("hour"));                        int start_min = Integer.parseInt(startTime.optString("minute"));                        //Log.e("day", day+"");                        //Log.e("start_hour", start_hour+"");                        //Log.e("start_min", start_min+"");                        int end_hour = Integer.parseInt(jsonObject2.optJSONObject("endTime").optString("hour"));                        int end_min = Integer.parseInt(jsonObject2.optJSONObject("endTime").optString("minute"));                        String start = start_hour+":"+start_min;                        String end = end_hour+":"+end_min;                        int studyLong = 0;                        //计算总时长                        try {                            studyLong = (int) between(start, end);                        } catch (ParseException e) {                            e.printStackTrace();                        }                        alarmManagerUtils.createGetUpAlarmManager(idx, studyLong);                        alarmManagerUtils.getUpAlarmManagerStartWork(day+2, start_hour, start_min);                    }                }            } catch (JSONException e) {                e.printStackTrace();            }        }    }    public static boolean isJSONValid(String test) {        try {            new JSONObject(test);        } catch (JSONException ex) {            try {                new JSONArray(test);            } catch (JSONException ex1) {                return false;            }        }        return true;    }    private long between(String startTime, String endTime) throws ParseException {        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");        Date begin = dfs.parse(startTime); //00:00        Date end = dfs.parse(endTime); //00:00        long between = (end.getTime() - begin.getTime())/1000;        return between/60;    }    public void createDialog(final AppInfo appInfo){        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());        builder.setTitle(appInfo.getAppName());        builder.setIcon(appInfo.getDrawable());        long remainTime = dailyLimitTime.getLimitTimeInMS(appInfo.getPackageName());        long dailyLimitTime = (long)(appInfo.getHours() * 60 * 60000 + appInfo.getMins() * 60000);        View dialogView = View.inflate(getActivity(), R.layout.limit_app_dialog, null);        final ProgressBar processBar = dialogView.findViewById(R.id.progressBar);        processBar.setMax(100);        processBar.setProgress((int)(((double)(remainTime/dailyLimitTime))*100));        double d = (double)remainTime/dailyLimitTime;        double i = d*100;        final TextView textPercent = dialogView.findViewById(R.id.textPercent);        textPercent.setText((int)i+"%");        final TextView remaining_time = dialogView.findViewById(R.id.remaining_time);        remaining_time.setText(hmsTimeFormatter(remainTime));        final TextView daily_limit_time = dialogView.findViewById(R.id.daily_limit_time);        daily_limit_time.setText(hmsTimeFormatter(dailyLimitTime));        builder.setView(dialogView);        builder.setNeutralButton("Close", null);        builder.setCancelable(true);        AlertDialog alertDialog = builder.create();        alertDialog.show();    }    private String hmsTimeFormatter(long milliSeconds) {        String hms = String.format("%02dh %02dm %02ds",                TimeUnit.MILLISECONDS.toHours(milliSeconds),                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));        return hms;    }    @Override    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {        switch (requestCode){            case REQUEST_BORROW:                if(resultCode == AddStudyTime.RESULT_OK_BORROW){                    ArrayList<Schedule> item_1 = (ArrayList<Schedule>)data.getSerializableExtra("schedules_1");                    timetable.add(item_1);                    int idx = data.getIntExtra("idx",-1);                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");                    timetable.edit(idx,item);                }                if(resultCode == AddStudyTime.RESULT_OK_EDIT){                    int idx = data.getIntExtra("idx",-1);                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");                    timetable.edit(idx,item);                }                break;        }        saveByPreference(timetable.createSaveData());    }    private void saveByPreference(String data){        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());        SharedPreferences.Editor editor = mPref.edit();        editor.putString("timetable",data);        editor.commit();        //Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();    }    @Override    public void onResume() {        super.onResume();        appLimitsDB.getAllLimitApps(limitAppsList);        adapter.notifyDataSetChanged();        if(limitAppsList.getItems().length != 0){            textView_no_limits.setVisibility(View.GONE);        } else {            textView_no_limits.setVisibility(View.VISIBLE);        }    }    @Override    public void onDestroy() {        super.onDestroy();        if(appLimitsDB != null){            appLimitsDB.close();        }    }}