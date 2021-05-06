package student.example.myapplication.admin.set.curfewtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin.AdminSet;

public class CurfewTime extends AppCompatActivity {

    Spinner sleep_time_spinner = null;
    TimePicker timePicker = null;
    TextView bedtime_text = null;
    String hour;
    String min;
    String getSleepTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_curfew_time);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        if (bundle != null) {
            //getHour = bundle.getString("hour");
            //getMin = bundle.getString("min");
            hour = bundle.getString("hour");
            min = bundle.getString("min");
            getSleepTime = bundle.getString("sleep_time");
        }

        //initialView(Integer.parseInt(getHour), Integer.parseInt(getMin), Integer.parseInt(getSleepTime));
        initialView(0, 0, 6);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = String.format("%02d", hourOfDay);
                min = String.format("%02d", minute);
                bedtime_text.setText(hour + " : " + min);
            }
        });

    }

    public void initialView(int hour, int min, int sleep_time){
        sleep_time_spinner = findViewById(R.id.sleep_time_spinner);
        sleep_time_spinner.setSelection(sleep_time - 6);

        timePicker = findViewById(R.id.bedtime_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

        bedtime_text = findViewById(R.id.bedtime_text);
        bedtime_text.setText(String.format("%02d", timePicker.getCurrentHour())+" : "+String.format("%02d", timePicker.getCurrentMinute()));
    }

    public void save(View button){
        Bundle bundle = new Bundle();
        bundle.putString("hour", hour);
        bundle.putString("min", min);
        bundle.putString("sleep_time", sleep_time_spinner.getSelectedItem().toString());

        Intent intent = new Intent(CurfewTime.this, AdminSet.class);
        intent.putExtra("bundle_curfew", bundle);
        startActivity(intent);

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
