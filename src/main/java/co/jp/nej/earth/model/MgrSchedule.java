package co.jp.nej.earth.model;

public class MgrSchedule {

    private String scheduleId;
    private String processId;
    private String taskId;
    private String processName;
    private String startTime;
    private String repeatOption;
    private String endTime;
    private String lastUpdateTime;

    public MgrSchedule() {
        super();
    }

    public MgrSchedule(String scheduleId, String processId, String taskId, String processName, String startTime,
            String repeatOption, String endTime, String lastUpdateTime) {
        super();
        this.scheduleId = scheduleId;
        this.processId = processId;
        this.taskId = taskId;
        this.processName = processName;
        this.startTime = startTime;
        this.repeatOption = repeatOption;
        this.endTime = endTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRepeatOption() {
        return repeatOption;
    }

    public void setRepeatOption(String repeatOption) {
        this.repeatOption = repeatOption;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getlastUpdateTime() {
        return lastUpdateTime;
    }

    public void setlastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
