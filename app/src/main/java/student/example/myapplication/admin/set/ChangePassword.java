package student.example.myapplication.admin.set;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import student.example.myapplication.R;
import student.example.myapplication.admin.AdminModePassword;

public class ChangePassword extends AppCompatActivity {
    private EditText et_old_pwd, et_new_pwd;
    private Button bt_change_pwd;
    AdminModePassword adminModePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_password);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        et_old_pwd = findViewById(R.id.et_old_pwd);
        et_new_pwd = findViewById(R.id.et_new_pwd);
        bt_change_pwd = findViewById(R.id.bt_change_pwd);
        adminModePassword = new AdminModePassword(this);

        bt_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_old_pwd.getText() == null || et_old_pwd.getText().toString().trim().equals("")){
                    Toast.makeText(ChangePassword.this, "Please enter old password to confirm", Toast.LENGTH_SHORT).show();
                } else if (!et_old_pwd.getText().toString().trim().equals(adminModePassword.getPassword())){
                    Toast.makeText(ChangePassword.this, "Old password is not correct", Toast.LENGTH_SHORT).show();
                } else {
                    if(et_new_pwd.getText() == null || et_new_pwd.getText().toString().trim().equals("")){
                        Toast.makeText(ChangePassword.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                    } else {
                        adminModePassword.setPassword(et_new_pwd.getText().toString().trim());
                        Toast.makeText(ChangePassword.this, "Successful", Toast.LENGTH_SHORT).show();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        finish();
                    }
                }
            }
        });
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
