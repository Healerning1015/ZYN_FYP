package student.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.ChangePassword;
import student.example.myapplication.admin.set.setEmail.SetEmail;
import student.example.myapplication.admin.set.applimits.AppLimits;
import student.example.myapplication.admin.set.curfewtime.CurfewTime;
import student.example.myapplication.admin.set.studytime.StudyTime;

public class AdminSet extends AppCompatActivity {

    private TableRow tablerow1;
    private TableRow tablerow2;
    private TableRow tablerow3;
    private TableRow tablerow4;
    private TableRow tablerow5;
    private Button done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set);

        initialView();

        tablerow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, AppLimits.class);
                startActivity(intent);
            }
        });

        tablerow2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, StudyTime.class);
                startActivity(intent);
            }
        });

        tablerow3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, CurfewTime.class);
                startActivity(intent);
            }
        });

        tablerow4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, SetEmail.class);
                startActivity(intent);
            }
        });

        tablerow5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminSet.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(AdminSet.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    public void initialView(){
        tablerow1 = findViewById(R.id.app_limits);
        tablerow2 = findViewById(R.id.study_time);
        tablerow3 = findViewById(R.id.curfew_time);
        tablerow4 = findViewById(R.id.set_email);
        tablerow5 = findViewById(R.id.change_pwd);
        done = findViewById(R.id.done);
    }
}