package student.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        this.getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                WelcomePage.this.startActivity(intent);
                WelcomePage.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}