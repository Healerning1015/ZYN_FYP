package student.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.AppLimits;
import student.example.myapplication.admin.set.curfewtime.CurfewTime;
import student.example.myapplication.admin.set.studytime.StudyTime;
import student.example.myapplication.home.HomeFragment;

public class AdminSet extends AppCompatActivity {

    TableRow tablerow1 = null;
    TableRow tablerow2 = null;
    TableRow tablerow3 = null;
    //TextView textView_study = null;
    //TextView textView_curfew = null;
    Button save = null;

    /*String hour_curfew = "00";
    String min_curfew = "00";
    String sleep_time = "6";

    String hour_study = "00";
    String min_study = "00";
    String study_time = "2";

     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set);

        initialView();

        /*
        Intent intent1 = getIntent();
        Bundle bundle_curfew = intent1.getBundleExtra("bundle_curfew");
        if (bundle_curfew != null) {
            hour_curfew = bundle_curfew.getString("hour");
            min_curfew = bundle_curfew.getString("min");
            sleep_time = bundle_curfew.getString("sleep_time");

            //String get_up_time = String.format("%02d", Integer.parseInt(hour) + Integer.parseInt(sleep_time));
            int get_up_hour = Integer.parseInt(hour_curfew) + Integer.parseInt(sleep_time);
            if(get_up_hour >= 24){
                get_up_hour -= 24;
                textView_curfew.setText(hour_curfew + " : " + min_curfew + " - " + String.format("%02d", get_up_hour) + " : " + min_curfew + "  next day");
            }
            else{
                textView_curfew.setText(hour_curfew + " : " + min_curfew + " - " + String.format("%02d", get_up_hour) + " : " + min_curfew);
            }
        }

        Intent intent2 = getIntent();
        Bundle bundle_study = intent2.getBundleExtra("bundle_study");
        if (bundle_study != null) {
            hour_study = bundle_study.getString("hour");
            min_study = bundle_study.getString("min");
            study_time = bundle_study.getString("study_time");
            textView_study.setText(study_time + " h start at " + hour_study + " : " + min_study);
        }

         */







        tablerow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, AppLimits.class);
                startActivity(intent);
            }
        });

        tablerow2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*Bundle bundle2 = new Bundle();
                bundle2.putString("hour", hour_study);
                bundle2.putString("min", min_study);
                bundle2.putString("study_time", study_time);

                 */
                Intent intent = new Intent(AdminSet.this, StudyTime.class);
                //intent.putExtra("bundle", bundle2);
                startActivity(intent);
            }
        });

        tablerow3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Bundle bundle3 = new Bundle();
                bundle3.putString("hour", hour_curfew);
                bundle3.putString("min", min_curfew);
                bundle3.putString("sleep_time", sleep_time);

                 */
                Intent intent = new Intent(AdminSet.this, CurfewTime.class);
                //intent.putExtra("bundle", bundle3);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void initialView(){
        tablerow1 = findViewById(R.id.app_limits);
        tablerow2 = findViewById(R.id.study_time);
        tablerow3 = findViewById(R.id.curfew_time);
        //textView_study = findViewById(R.id.show_study_time);
        //textView_curfew = findViewById(R.id.sleep_time_period);

        //textView.setText("00 : 00 - 00 : 00");
        save = findViewById(R.id.save);

    }

}