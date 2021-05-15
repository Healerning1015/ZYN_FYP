package student.example.myapplication.usage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import student.example.myapplication.admin.EmailAddress;
import student.example.myapplication.admin.set.SetEmail;

public class ServiceSendEmail extends JobIntentService {

    private static final int JOB_ID = 3999;
    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, ServiceSendEmail.class, JOB_ID, work);
    }

    private EmailAddress emailAddress;
    private String sEmail, sPassword;

    private String title, content;
    private Map<String, String> mapInlineImages;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("ServiceSendEmail","Here is ServiceSendEmail");

        emailAddress = new EmailAddress(this);

        //Sender
        sEmail = "putDownYourPhone2021@gmail.com";
        sPassword = "myFYP2021";

        title = "Put Down Your Phone 2021";
        content = "";
        if(intent.getStringExtra("email_title")!=null){
            title = intent.getStringExtra("email_title");
        }
        if(intent.getStringExtra("email_content")!=null){
            content = intent.getStringExtra("email_content");
        }
        if(intent.getSerializableExtra("image_path_map")!=null){
            SerializableMap serializableMap = (SerializableMap) intent.getSerializableExtra("image_path_map");
            mapInlineImages = serializableMap.getMap();
        }

        sendEmail();
    }

    private void sendEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail, sPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress.getEmail().trim()));
            message.setSubject(title);
            //message.setText(etMessage.getText().toString().trim());

            //
            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<h2>Hello!</h2>" +
                    "<p>Nice to meet you.</p>";

            htmlText += content;


            messageBodyPart.setContent(htmlText, "text/html;charset=UTF-8");

            multipart.addBodyPart(messageBodyPart);

            if (mapInlineImages != null && mapInlineImages.size() > 0) {
                Set<String> setImageID = mapInlineImages.keySet();
                for (String contentId : setImageID) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setHeader("Content-ID", contentId);
                    imagePart.setDisposition(MimeBodyPart.INLINE);

                    String imageFilePath = mapInlineImages.get(contentId);
                    Log.e(contentId, imageFilePath);
                    try {
                        imagePart.attachFile(imageFilePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    multipart.addBodyPart(imagePart);
                }
            }


            // put everything together
            message.setContent(multipart);

            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private class SendMail extends AsyncTask<Message,String,String> {

        //private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(ServiceSendEmail.this, "Please Wait", "Sending Mail...", true, false);
            Log.e("Service Send Email","Sending Mail...");
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();
            if(s.equals("Success")){
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceSendEmail.this);
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

                 */
                Log.e("Service Send Email","Successfully sent");
            } else {
                Log.e("Service Send Email","Send failure");
            }
        }
    }
}
