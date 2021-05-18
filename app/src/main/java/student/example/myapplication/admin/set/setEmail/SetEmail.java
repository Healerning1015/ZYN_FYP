package student.example.myapplication.admin.set.setEmail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import student.example.myapplication.R;

import static student.example.myapplication.MainActivity.emailAlarmSet;

public class SetEmail extends AppCompatActivity {

    private Context context;
    private EditText et_email;
    private Button bt_submit;
    private CheckBox cb_send;

    private LinearLayout layout_chooseTime;
    private TimePicker send_time_picker;
    Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_email);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        context = this;

        initView();

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email.getText() == null || et_email.getText().toString().equals("")){
                    Toast.makeText(SetEmail.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else if(!isEmail(et_email.getText().toString().trim())){
                    Toast.makeText(SetEmail.this, "Email format error", Toast.LENGTH_SHORT).show();
                } else {
                    email.setEmail(et_email.getText().toString().trim());
                    Toast.makeText(SetEmail.this, "Successful", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 隐藏软键盘
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    if(email.getWhetherSend()){
                        Log.e("Set Email","try to send email");
                        sendEmail();
                    }
                    //finish();
                }
                emailAlarmSet(context);
            }
        });

        cb_send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    email.setWhetherSend(true);
                    layout_chooseTime.setVisibility(View.VISIBLE);
                } else {
                    email.setWhetherSend(false);
                    layout_chooseTime.setVisibility(View.GONE);
                }
                //Log.e("checkBox", email.getWhetherSend()+"");
            }
        });

        send_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int[] time = {hourOfDay, minute};
                email.setSendTime(time);
            }
        });
    }

    private void initView() {
        email = new Email(this);
        email.setWhetherSend(true);

        et_email = findViewById(R.id.et_email);
        bt_submit = findViewById(R.id.bt_submit);
        cb_send = findViewById(R.id.cb_send);
        layout_chooseTime = findViewById(R.id.layout_chooseTime);
        send_time_picker = findViewById(R.id.send_time_picker);

        if(email.getEmail()!=null){
            et_email.setText(email.getEmail());
        }
        if(email.getSendTime()==null){
            int[] time = {8, 0};
            email.setSendTime(time);
        }

        send_time_picker.setIs24HourView(true);
        int[] time = email.getSendTime();
        send_time_picker.setHour(time[0]);
        send_time_picker.setMinute(time[1]);
    }

    public void sendEmail(){
        Intent intent = new Intent(SetEmail.this, ServiceSendEmail.class);
        ServiceSendEmail.enqueueWork(this, intent);
    }

    //检查email格式
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
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

/*
    public class SendMail extends AsyncTask<Message,String,String> {

        public ProgressDialog progressDialog;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SetEmail.this,
                    "Please Wait", "Sending Mail...", true, false);
        }

        @Override
        public String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(SetEmail.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#769fcd'>Success</font>"));
                builder.setMessage("Mail send successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                Log.e("Service Send Email","Successfully sent");
            } else {
                Log.e("Service Send Email","Send failure");
            }
        }
    }

 */
}
