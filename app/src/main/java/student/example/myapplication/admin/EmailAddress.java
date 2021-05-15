package student.example.myapplication.admin;

import android.content.Context;

import io.paperdb.Paper;

public class EmailAddress {
    private String EMAIL = "EMAIL";
    private String WHETHER_SEND = "WHETHER_SEND";

    public EmailAddress(Context ctx){
        Paper.init(ctx);
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
