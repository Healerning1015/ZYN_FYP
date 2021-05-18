package student.example.myapplication.admin.set.curfewtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin.AdminSet;

public class CurfewTime extends AppCompatActivity {

    private Spinner sleep_time_spinner;
    private TimePicker timePicker;
    private TextView bedtime_text;
    private String hour, min;
    private Curfew curfew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_curfew_time);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        curfew = new Curfew(this);

        initialView(0, 0, 8);

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
        timePicker.setHour(hour);
        timePicker.setMinute(min);

        bedtime_text = findViewById(R.id.bedtime_text);
        bedtime_text.setText(String.format("%02d", timePicker.getHour())+" : "+String.format("%02d", timePicker.getMinute()));
    }

    public void save(View button){
        String[] night = {hour, min}; 
        curfew.setNight(night);

        int get_up_hour = Integer.parseInt(hour) + Integer.parseInt(sleep_time_spinner.getSelectedItem().toString());
        if(get_up_hour >= 24){
            get_up_hour -= 24;
        }
        String[] morning = {get_up_hour+"", min};
        curfew.setMorning(morning);

        Toast.makeText(this, hour+":"+min+" - "+get_up_hour+":"+min, Toast.LENGTH_SHORT).show();

        finish();
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
