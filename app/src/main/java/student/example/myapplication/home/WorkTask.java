package student.example.myapplication.home;

public class WorkTask {
    private int taskID;
    private double studyTime;
    private double breakTime;
    private int switchCount;

    public WorkTask(int taskID, double studyTime, double breakTime, int switchCount) {
        this.taskID = taskID;
        this.studyTime = studyTime;
        this.breakTime = breakTime;
        this.switchCount = switchCount;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public double getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(double studyTime) {
        this.studyTime = studyTime;
    }

    public double getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(double breakTime) {
        this.breakTime = breakTime;
    }

    public int getSwitchCount() {
        return switchCount;
    }

    public void setSwitchCount(int switchCount) {
        this.switchCount = switchCount;
    }
}
