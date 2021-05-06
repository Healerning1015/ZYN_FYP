package student.example.myapplication.admin.set.studytime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import student.example.myapplication.R;
import student.example.myapplication.home.HomeFragment;

public class AddStudyTime extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;
    public static final int RESULT_OK_BORROW = 4;

    private Button deleteBtn;
    private Button breakUpBtn;
    private Button submitBtn;


    private Spinner daySpinner;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private TextView startText;
    private TextView endText;

    private LinearLayout container;

    private View view;


    private String hour;
    private String min;
    private int timeSlotNo;

    //request mode
    private int mode;

    private Schedule schedule;
    private int editIdx;

    private int studyPerWeek = 0;
    private int newStudyTime;
    private int oldStudyTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_study_time);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init(){
        this.context = this;

        deleteBtn = findViewById(R.id.delete_btn);
        breakUpBtn = findViewById(R.id.break_up_btn);
        submitBtn = findViewById(R.id.submit_btn);

        //timeSlotTitle = findViewById(R.id.time_slot_title);
        //timeSlotNum = findViewById(R.id.time_slot_num);
        daySpinner = findViewById(R.id.study_day_spinner);
        startText = findViewById(R.id.start_text);
        startTimePicker = findViewById(R.id.start_time_picker);
        endText = findViewById(R.id.end_text);
        endTimePicker = findViewById(R.id.end_time_picker);

        container = findViewById(R.id.container);

        //set the default time
        schedule = new Schedule();
        schedule.setStartTime(new Time(15,00));
        schedule.setEndTime(new Time(17,00));

        loadSavedData();
        checkMode();
        initView();
    }

    /** check whether the mode is ADD or EDIT */
    private void checkMode(){
        Intent i = getIntent();
        mode = i.getIntExtra("mode", StudyTime.REQUEST_ADD);

        if(mode == StudyTime.REQUEST_EDIT){
            getSupportActionBar().setTitle("Change Time");
            loadScheduleData();
            deleteBtn.setVisibility(View.VISIBLE);
        } else if(mode == HomeFragment.REQUEST_BORROW){
            getSupportActionBar().setTitle("Change Time");
            loadScheduleData();
            breakUpBtn.setVisibility(View.VISIBLE);
        }
    }

    public void initView(){
        submitBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        breakUpBtn.setOnClickListener(this);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schedule.setDay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int startHour = schedule.getStartTime().getHour();
        int startMin = schedule.getStartTime().getMinute();
        int endHour = schedule.getEndTime().getHour();
        int endMin = schedule.getEndTime().getMinute();

        String startTime = startHour+":"+startMin;
        String endTime = endHour+":"+endMin;
        int studyLong = 0;

        //计算总时长
        try {
            studyLong = (int) between(startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startText.setText(String.format("%02d", startHour)+" : "+String.format("%02d", startMin));
        endText.setText(String.format("%02d", endHour)+" : "+String.format("%02d", endMin));

        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour(startHour);
        startTimePicker.setCurrentMinute(startMin);
        final int finalStudyLong = studyLong;
        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = String.format("%02d", hourOfDay);
                min = String.format("%02d", minute);
                startText.setText(hour + " : " + min);
                schedule.getStartTime().setHour(hourOfDay);
                schedule.getStartTime().setMinute(minute);
                if(mode == HomeFragment.REQUEST_BORROW){
                    String newEndHour = String.format("%02d", hourOfDay+(minute+finalStudyLong)/60);
                    String newEndMin = String.format("%02d", (minute+finalStudyLong)%60);
                    endText.setText(newEndHour + " : " + newEndMin);
                    endTimePicker.setCurrentHour(hourOfDay+(minute+finalStudyLong)/60);
                    endTimePicker.setCurrentMinute((minute+finalStudyLong)%60);
                    schedule.getEndTime().setHour(hourOfDay+(minute+finalStudyLong)/60);
                    schedule.getEndTime().setMinute((minute+finalStudyLong)%60);
                }
            }
        });

        endTimePicker.setEnabled(false);
        endTimePicker.setIs24HourView(true);
        endTimePicker.setCurrentHour(endHour);
        endTimePicker.setCurrentMinute(endMin);
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = String.format("%02d", hourOfDay);
                min = String.format("%02d", minute);
                endText.setText(hour + " : " + min);
                schedule.getEndTime().setHour(hourOfDay);
                schedule.getEndTime().setMinute(minute);
                //如果结束时间可以调节，就会陷入一个开始结束一直互相调节的死循环，因此unable
                /*
                if(mode == HomeFragment.REQUEST_BORROW){
                    int studyLongH = finalStudyLong/60;
                    int studyLongM = finalStudyLong%60;
                    int newStartH;
                    int newStartM;
                    if(minute >= studyLongM){
                        newStartH = hourOfDay - studyLongH;
                        newStartM = minute - studyLongM;
                    } else {
                        newStartH = hourOfDay - 1 - studyLongH;
                        newStartM = minute + 60 - studyLongM;
                    }
                    String newStartHour = String.format("%02d", newStartH);
                    String newStartMin = String.format("%02d", newStartM);
                    startText.setText(newStartHour + " : " + newStartMin);
                    startTimePicker.setCurrentHour(newStartH);
                    startTimePicker.setCurrentMinute(newStartM);
                    schedule.getStartTime().setHour(newStartH);
                    schedule.getStartTime().setMinute(newStartM);
                }
                 */
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:
                String startTime = schedule.getStartTime().getHour()+":"+schedule.getStartTime().getMinute();
                String endTime = schedule.getEndTime().getHour()+":"+schedule.getEndTime().getMinute();
                try {
                    newStudyTime = (int) between(startTime, endTime);
                    studyPerWeek += newStudyTime;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(mode == StudyTime.REQUEST_ADD){
                    schedule.setClassTitle("Study");
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    //you can add more schedules to ArrayList
                    schedules.add(schedule);
                    i.putExtra("schedules",schedules);
                    //i.putExtra("studyTimePerWeek", studyPerWeek);
                    setResult(RESULT_OK_ADD,i);
                    finish();
                }
                else if(mode == StudyTime.REQUEST_EDIT || mode == HomeFragment.REQUEST_BORROW){
                    schedule.setClassTitle("Study");
                    Log.e("schedule start time:  ", schedule.getStartTime().getHour()+":"+schedule.getStartTime().getMinute());
                    Log.e("schedule end time:  ", schedule.getEndTime().getHour()+":"+schedule.getEndTime().getMinute());
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    schedules.add(schedule);
                    i.putExtra("idx",editIdx);
                    i.putExtra("schedules",schedules);
                    //i.putExtra("studyTimePerWeek", studyPerWeek);
                    setResult(RESULT_OK_EDIT,i);
                    finish();
                }
                break;
            case R.id.delete_btn:
                Intent i = new Intent();
                i.putExtra("idx",editIdx);
                //i.putExtra("studyTimePerWeek", studyPerWeek);
                setResult(RESULT_OK_DELETE, i);
                finish();
                break;
            case R.id.break_up_btn:
                Schedule breakUpSchedule = new Schedule();
                breakUpTime(breakUpSchedule);
                Log.e("breakUp start time:  ", breakUpSchedule.getStartTime().getHour()+":"+breakUpSchedule.getStartTime().getMinute());
                Log.e("breakUp end time:  ", breakUpSchedule.getEndTime().getHour()+":"+breakUpSchedule.getEndTime().getMinute());

                Intent j = new Intent();
                ArrayList<Schedule> schedules_1 = new ArrayList<Schedule>();
                //you can add more schedules to ArrayList
                schedules_1.add(breakUpSchedule);

                j.putExtra("schedules_1",schedules_1);

                ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                schedules.add(schedule);

                j.putExtra("idx",editIdx);
                j.putExtra("schedules",schedules);
                setResult(RESULT_OK_BORROW,j);

                finish();

                break;
        }
        saveByPreference(studyPerWeek);
    }

    private void breakUpTime(Schedule breakUpSchedule){
        int startHour = schedule.getStartTime().getHour();
        int startMin = schedule.getStartTime().getMinute();
        int endHour = schedule.getEndTime().getHour();
        int endMin = schedule.getEndTime().getMinute();

        String startTime = startHour+":"+startMin;
        String endTime = endHour+":"+endMin;
        int studyLong = 0;

        //计算总时长
        try {
            studyLong = (int) between(startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //将原来的时间段缩为前1/2
        int endMin1 = startMin + studyLong/2;
        int endHour1 = startHour + endMin1/60;
        schedule.getEndTime().setHour(endHour1);
        schedule.getEndTime().setMinute(endMin1%60);

        //分割出来的时间段设置
        breakUpSchedule.setDay(schedule.getDay());
        breakUpSchedule.getStartTime().setHour(endHour1);
        breakUpSchedule.getStartTime().setMinute(endMin1%60);
        breakUpSchedule.getEndTime().setHour(endHour);
        breakUpSchedule.getEndTime().setMinute(endMin);
        breakUpSchedule.setClassTitle("Study");
    }

    private void loadScheduleData(){
        Intent i = getIntent();
        editIdx = i.getIntExtra("idx",-1);
        ArrayList<Schedule> schedules = (ArrayList<Schedule>)i.getSerializableExtra("schedules");
        schedule = schedules.get(0);
        daySpinner.setSelection(schedule.getDay());

        String startTime = schedule.getStartTime().getHour()+":"+schedule.getStartTime().getMinute();
        String endTime = schedule.getEndTime().getHour()+":"+schedule.getEndTime().getMinute();
        try {
            oldStudyTime = (int) between(startTime, endTime);
            studyPerWeek -= oldStudyTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private long between(String startTime, String endTime) throws ParseException {
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
        Date begin = dfs.parse(startTime); //00:00
        Date end = dfs.parse(endTime); //00:00
        long between = (end.getTime() - begin.getTime())/1000;
        return between/60;
    }

    private void saveByPreference(int num){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("studyTimePerWeek", num);
        editor.commit();
    }

    private void loadSavedData(){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        int savedData = mPref.getInt("studyTimePerWeek",0);
        if(savedData == 0) return;
        studyPerWeek = savedData;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
