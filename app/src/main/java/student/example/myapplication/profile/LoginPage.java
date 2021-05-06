package student.example.myapplication.profile;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        this.getSupportActionBar().hide();

    }

    public void backToProfile(View view){
        finish();
    }
}