package student.example.myapplication.admin;

import android.content.Context;

import io.paperdb.Paper;

public class AdminModePassword {
    private String ADMIN_PWD_KEY = "ADMIN_PWD_KEY";

    public AdminModePassword(Context ctx){
        Paper.init(ctx);
    }

    public void setPassword(String pwd){
        Paper.book().write(ADMIN_PWD_KEY, pwd);
    }

    public String getPassword(){
        return Paper.book().read(ADMIN_PWD_KEY);
    }

    public void clearPassword(){
        Paper.book().delete(ADMIN_PWD_KEY);
    }
}
