package student.example.myapplication.admin.set.applimits;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Toast;

import com.andrognito.patternlockview.utils.PatternLockUtils;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.Utils;

public class AppLimits extends AppCompatActivity {

    ListView appInfoListView = null;
    List<AppInfo> appInfos = null;
    AppInfosAdapter infosAdapter = null;
    LinearLayout layout_permission;
    LinearLayout layout_set_lock;
    LinearLayout layout_app_limits_hint;
    public static final int REQUEST_DIALOG = 1;

    PatternPassword utilsPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_app_limits);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            //ColorDrawable newColor = new ColorDrawable(getResources().getColor(R.color.blue_3));
            //newColor.setAlpha(255);
            //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_3)));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        layout_permission = findViewById(R.id.layout_permission);
        layout_permission.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });

        layout_set_lock = findViewById(R.id.layout_set_lock);
        layout_app_limits_hint = findViewById(R.id.app_limits_hint);

        utilsPassword = new PatternPassword(this);

        appInfoListView = (ListView) this.findViewById(R.id.appinfo_list);
        appInfos = getAppInfos();
        updateUI(appInfos);

        appInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                createDialog(getAppInfos().get(position), position);
            }
        });
        infosAdapter.notifyDataSetChanged();

    }

    public void updateUI(List<AppInfo> appInfos) {
        if (null != appInfos) {
            infosAdapter = new AppInfosAdapter(getApplication(), appInfos);
            appInfoListView.setAdapter(infosAdapter);
        }
    }

    public List<AppInfo> getAppInfos() {
        PackageManager pm = getApplication().getPackageManager();
        List<PackageInfo> packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appInfos = new ArrayList<AppInfo>();
    	//获取应用程序的名称，不是包名，而是清单文件中的labelname
        //String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
        //appInfo.setAppName(str_name);
        for (PackageInfo packgeInfo : packgeInfos) {
            if ((packgeInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
                //String appCategory = packgeInfo.
                String packageName = packgeInfo.packageName;
                Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
                int hours = 0;
                int mins = 0;
                AppInfo appInfo = new AppInfo(appName, packageName, drawable);
                appInfos.add(appInfo);
            }

        }
        return appInfos;
    }

    public void createDialog(final AppInfo appInfo, int pos) {
        Intent intent = new Intent(AppLimits.this, AddLimitActivity.class);
        intent.putExtra("appInfo", appInfo);
        intent.putExtra("pos", pos);
        intent.putExtra("byteLogo", drawableToBytes(appInfo.getDrawable()));
        startActivityForResult(intent,REQUEST_DIALOG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_DIALOG:
                if (resultCode == RESULT_OK) {
                    if(intent.getStringExtra("add_limit_activity") != null){
                        LinearLayout view = (LinearLayout)infosAdapter.getView((int)intent.getIntExtra("pos", 0), null, null);
                        ImageView lock = (ImageView) view.findViewById(R.id.app_lock);
                        switch(intent.getStringExtra("add_limit_activity")) {
                            case "add_ok":
                                Toast.makeText(AppLimits.this, "Successful add limit.", Toast.LENGTH_LONG).show();
                                lock.setImageResource(R.drawable.ic_baseline_lock_24);
                                break;
                            case "change_ok":
                                Toast.makeText(AppLimits.this, "Successful change limit.", Toast.LENGTH_LONG).show();
                                break;
                            case "delete_ok":
                                Toast.makeText(AppLimits.this, "Successful delete limit.", Toast.LENGTH_LONG).show();
                                lock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                                break;
                        }
                        infosAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    public void setLock(View view) {
        Intent i;
        if(new PatternPassword(this).getPassword() == null){
            i = new Intent(AppLimits.this, PatternLockPage.class);
            i.putExtra("app_limits", "app_limits");
        } else {
            i = new Intent(AppLimits.this, ResetPatternLock.class);
        }
        startActivity(i);
    }

    public byte[] drawableToBytes(Drawable d) {
        Bitmap bitmap = this.drawableToBitmap(d);
        return this.bitmapToBytes(bitmap);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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

    @Override
    protected void onResume() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(Utils.checkPermission(this)){
                layout_permission.setVisibility(View.GONE);
                layout_app_limits_hint.setVisibility(View.GONE);
                if(utilsPassword.getPassword() != null){
                    appInfoListView.setVisibility(View.VISIBLE);
                }
            } else{
                layout_permission.setVisibility(View.VISIBLE);
                layout_app_limits_hint.setVisibility(View.VISIBLE);
                appInfoListView.setVisibility(View.GONE);
            }


        }
        super.onResume();
    }


}