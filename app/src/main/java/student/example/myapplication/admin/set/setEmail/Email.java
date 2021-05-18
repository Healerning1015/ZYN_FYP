package student.example.myapplication.admin.set.setEmail;

import android.content.Context;

import io.paperdb.Paper;

public class Email {
    private String EMAIL = "EMAIL";
    private String WHETHER_SEND = "WHETHER_SEND";
    private String SEND_TIME = "SEND_TIME";
    public Email(Context ctx){
        Paper.init(ctx);
    }

    public void setSendTime(int[] time){
        Paper.book().write(SEND_TIME, time);
    }

    public int[] getSendTime(){
        return Paper.book().read(SEND_TIME);
    }

    public void clearSendTime(){
        Paper.book().delete(SEND_TIME);
    }

    public void setEmail(String email){
        Paper.book().write(EMAIL, email);
    }

    public String getEmail(){
        return Paper.book().read(EMAIL);
    }

    public void clearEmail(){
        Paper.book().delete(EMAIL);
    }

    public void setWhetherSend(Boolean whetherSend){
        Paper.book().write(WHETHER_SEND, whetherSend);
    }

    public Boolean getWhetherSend(){
        return Paper.book().read(WHETHER_SEND);
    }
}
