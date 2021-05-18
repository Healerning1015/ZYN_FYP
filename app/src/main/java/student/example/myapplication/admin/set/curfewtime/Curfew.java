package student.example.myapplication.admin.set.curfewtime;

import android.content.Context;

import io.paperdb.Paper;

public class Curfew {
    private String MORNING = "MORNING";
    private String NIGHT = "NIGHT";

    public Curfew(Context ctx){
        Paper.init(ctx);
    }

    public void setMorning(String[] time){
        Paper.book().write(MORNING, time);
    }

    public String[] getMorning(){
        return Paper.book().read(MORNING);
    }

    public void setNight(String[] time){
        Paper.book().write(NIGHT, time);
    }

    public String[] getNight(){
        return Paper.book().read(NIGHT);
    }

    public void clearEmail(){
        Paper.book().delete(MORNING);
    }
}
