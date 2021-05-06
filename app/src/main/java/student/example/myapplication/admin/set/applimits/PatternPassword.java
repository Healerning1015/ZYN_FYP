package student.example.myapplication.admin.set.applimits;

import android.content.Context;

import io.paperdb.Paper;

public class PatternPassword {
    private String PASSWORD_KEY = "PASSWORD_KEY";
    public String STATUS_FIRST_STEP = "Draw an unlock pattern";
    public String STATUS_NEXT_STEP = "Draw pattern again to confirm";
    public String STATUS_PASSWORD_CORRECT = "OK";
    public String STATUS_PASSWORD_INCORRECT = "Try again";
    public String SHEMA_FAILED = "Connect at least 4 dots";
    public String STATUS_CHANGE_PASSWORD_CONFIRM = "Draw old pattern to confirm";
    public String STATUS_NEW_FIRST_STEP = "Draw an new unlock pattern";

    private boolean isFirstStep = true;
    private boolean isSecondStep = true;

    public PatternPassword(Context ctx){
        Paper.init(ctx);
    }

    public void setPassword(String pwd){
        Paper.book().write(PASSWORD_KEY, pwd);
    }

    public String getPassword(){
        return Paper.book().read(PASSWORD_KEY);
    }

    public boolean isFirstStep(){
        return isFirstStep;
    }

    public void setFirstStep(boolean firstStep){
        isFirstStep = firstStep;
    }

    public boolean isSecondStep(){
        return isSecondStep;
    }

    public void setSecondStep(boolean secondStep){
        isSecondStep = secondStep;
    }

    public boolean isCorrect(String pwd){
        return pwd.equals(getPassword());
    }

    public void clearPassword(){
        Paper.book().delete(PASSWORD_KEY);
    }
}
