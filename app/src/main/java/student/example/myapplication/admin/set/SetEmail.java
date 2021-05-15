package student.example.myapplication.admin.set;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import student.example.myapplication.R;
import student.example.myapplication.admin.EmailAddress;
import student.example.myapplication.usage.ServiceSendEmail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SetEmail extends AppCompatActivity {

    private EditText et_email;
    private Button bt_email;
    private CheckBox cb_send;
    EmailAddress emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_set_email);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        et_email = findViewById(R.id.et_email);
        bt_email = findViewById(R.id.bt_email);
        cb_send = findViewById(R.id.cb_send);
        emailAddress = new EmailAddress(this);
        emailAddress.setWhetherSend(true);

        if(emailAddress.getEmail()!=null){
            et_email.setText(emailAddress.getEmail());
        }
        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email.getText() == null || et_email.getText().toString().equals("")){
                    Toast.makeText(SetEmail.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else if(!isEmail(et_email.getText().toString().trim())){
                    Toast.makeText(SetEmail.this, "Email format error", Toast.LENGTH_SHORT).show();
                } else {
                    emailAddress.setEmail(et_email.getText().toString().trim());
                    Toast.makeText(SetEmail.this, "Successful", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 隐藏软键盘
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    if(emailAddress.getWhetherSend()){
                        Log.e("Set Email","try to send email");
                        sendEmail();
                    }
                    //finish();
                }
            }
        });

        cb_send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    emailAddress.setWhetherSend(true);
                } else {
                    emailAddress.setWhetherSend(false);
                }
                //Log.e("checkBox", emailAddress.getWhetherSend()+"");
            }
        });
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
