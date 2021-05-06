package student.example.myapplication.admin.set.studytime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import student.example.myapplication.R;

import static student.example.myapplication.home.HomeFragment.isJSONValid;

public class StudyTime extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    public int hours;
    public int mins;
    String studyTimePerWeek;

    private Button addBtn;
    private Button clearBtn;
    private TimetableView timetable;
    private TextView studyPerWeek;


    private AlarmManagerUtils alarmManagerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_study_time_table);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);

        timetable = findViewById(R.id.timetable);
        //timetable.setHeaderHighlight(2);

        studyPerWeek = findViewById(R.id.study_per_week);

        alarmManagerUtils = AlarmManagerUtils.getInstance(this);

        loadSavedData();
        loadSavedStudyTime();
        initView();
    }

    private void initView(){
        addBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, AddStudyTime.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });

        studyPerWeek.setText(studyTimePerWeek);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                Intent i = new Intent(this,AddStudyTime.class);
                i.putExtra("mode",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);
                break;
            case R.id.clear_btn:
                timetable.removeAll();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == AddStudyTime.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    studyTimePerWeek = data.getStringExtra("studyTimePerWeek");
                    timetable.add(item);
                    Log.e("item ", item.toString()+"");
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == AddStudyTime.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    studyTimePerWeek = data.getStringExtra("studyTimePerWeek");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == AddStudyTime.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    studyTimePerWeek = data.getStringExtra("studyTimePerWeek");
                    timetable.remove(idx);
                }
                break;
        }
        saveByPreference(timetable.createSaveData());
    }

    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable",data);
        editor.commit();
        //Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable","");
        Log.e("savedData: ", savedData);
        if(savedData == null && savedData.equals("")) return;
        if(isJSONValid(savedData)){
            timetable.load(savedData);
            /*
            try {
                //String转JSONObject
                JSONObject result = new JSONObject(savedData);
                JSONArray sticker = result.optJSONArray("sticker");
                for(int i=0; i < sticker.length(); i++){
                    JSONObject jsonObject1 = sticker.getJSONObject(i);
                    int idx = Integer.parseInt(jsonObject1.optString("idx"));
                    Log.e("idx", idx+"");

                    JSONArray schedule = jsonObject1.optJSONArray("schedule");
                    for(int j=0; j < schedule.length(); j++){
                        JSONObject jsonObject2 = schedule.getJSONObject(j);
                        int day = Integer.parseInt(jsonObject2.optString("day"));
                        JSONObject startTime = jsonObject2.optJSONObject("startTime");
                        int start_hour = Integer.parseInt(startTime.optString("hour"));
                        int start_min = Integer.parseInt(startTime.optString("minute"));
                        Log.e("day", day+"");
                        Log.e("start_hour", start_hour+"");
                        Log.e("start_min", start_min+"");


                        alarmManagerUtils.createGetUpAlarmManager(idx);
                        alarmManagerUtils.getUpAlarmManagerStartWork(day+2, start_hour, start_min);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

             */
        }


    }

    private void loadSavedStudyTime(){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        int saveStudyTime = mPref.getInt("studyTimePerWeek", 0);
        if(saveStudyTime == 0) return;
        studyTimePerWeek = saveStudyTime+"";
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
