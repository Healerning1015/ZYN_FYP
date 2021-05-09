package student.example.myapplication.home;


public class LimitTime {
    private String packageName;
    private long limitTimeInMS;


    public LimitTime(String packageName, long limitTimeInMS) {
        this.packageName = packageName;
        this.limitTimeInMS = limitTimeInMS;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getLimitTimeInMS() {
        return limitTimeInMS;
    }

    public void setLimitTimeInMS(long limitTimeInMS) {
        this.limitTimeInMS = limitTimeInMS;
    }
}
