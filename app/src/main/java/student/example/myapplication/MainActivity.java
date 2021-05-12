package student.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.SimpleDateFormat;
import java.util.Date;

import student.example.myapplication.admin.services.BackgroundManager;


public class MainActivity extends AppCompatActivity{

    //private static final int PACKAGE_USAGE_STATS = 0;

    //private View mLayout;
    private final static String TAG="MainActivity";
    private View mLayout;
    private static final int PERMISSION_REQUEST_SYSTEM_ALERT_WINDOW = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.container);

        this.getSupportActionBar().hide();

        BackgroundManager.getInstance().init(this).startService();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_admin, R.id.navigation_usage, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        //Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        //startActivityForResult(intent,1);
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    R.string.package_usage_stats_permission_available,
                    Snackbar.LENGTH_SHORT).show();
        } else {
            // Permission is missing and must be requested.
            requestPermission();
        }

         */
    }

}