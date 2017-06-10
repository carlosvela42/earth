package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class ScheduleForm extends BaseModel<MgrSchedule> {

    @NotEmpty(message = "E0001,scheduleId")
    private String scheduleId;

    @NotEmpty(message = "E0001,hostName")
    private String hostName;

    @NotEmpty(message = "E0001,processId")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,processId")
    @Min(value = 1, message = "E0017,processId")
    private String processId;

    @NotEmpty(message = "E0001,taskId")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,taskId")
    @Min(value = 1, message = "E0017,processId")
    private String taskId;

    @NotEmpty(message = "E0001,processIServiceId")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,processIServiceId")
    @Min(value = 1, message = "E0017,processId")
    private String processIServiceId;

    @NotEmpty(message = "E0001,startTime")
    @Pattern(regexp = Constant.Regexp.DATETIME_VALIDATION, message = "E0017,startTime")
    private String startTime;

    @NotEmpty(message = "E0001,endTime")
    @Pattern(regexp = Constant.Regexp.DATETIME_VALIDATION, message = "E0017,endTime")
    private String endTime;

    private String enableDisable;

    @NotEmpty(message = "E0001,runIntervalDay")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,runIntervalSecond")
    private String runIntervalDay;

    @NotEmpty(message = "E0001,runIntervalHour")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,runIntervalSecond")
    private String runIntervalHour;

    @NotEmpty(message = "E0001,runIntervalMinute")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,runIntervalMinute")
    private String runIntervalMinute;

    @NotEmpty(message = "E0001,runIntervalSecond")
    @Pattern(regexp = Constant.Regexp.STRING_NUMBER, message = "E0017,runIntervalSecond")
    private String runIntervalSecond;
//    public ScheduleForm(MgrSchedule mgrSchedule){
//        super();
//        this.scheduleId = mgrSchedule.getScheduleId();
//        this.hostName = mgrSchedule.getHostName();
//        this.processId = mgrSchedule.getProcessId();
//        this.taskId = mgrSchedule.getTaskId();
//        this.processIServiceId = mgrSchedule.;
//        this.startTime = mgrSchedule.;
//        this.endTime = mgrSchedule.;
//        this.enableDisable = mgrSchedule.;
//        this.runIntervalDay = mgrSchedule.;
//        this.runIntervalHour = mgrSchedule.;
//        this.runIntervalMinute = mgrSchedule.;
//        this.runIntervalSecond = mgrSchedule.;
//    }
//
//    public ScheduleForm(String scheduleId, String hostName, String processId, String taskId, String processIServiceId,
//            String startTime, String endTime, String enableDisable, String runIntervalDay, String runIntervalHour,
//            String runIntervalMinute, String runIntervalSecond) {
//        super();
//        this.scheduleId = scheduleId;
//        this.hostName = hostName;
//        this.processId = processId;
//        this.taskId = taskId;
//        this.processIServiceId = processIServiceId;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.enableDisable = enableDisable;
//        this.runIntervalDay = runIntervalDay;
//        this.runIntervalHour = runIntervalHour;
//        this.runIntervalMinute = runIntervalMinute;
//        this.runIntervalSecond = runIntervalSecond;
//    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getProcessIServiceId() {
        return processIServiceId;
    }

    public void setProcessIServiceId(String processIServiceId) {
        this.processIServiceId = processIServiceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEnableDisable() {
        return enableDisable;
    }

    public void setEnableDisable(String enableDisable) {
        this.enableDisable = enableDisable;
    }

    public String getRunIntervalDay() {
        return runIntervalDay;
    }

    public void setRunIntervalDay(String runIntervalDay) {
        this.runIntervalDay = runIntervalDay;
    }

    public String getRunIntervalHour() {
        return runIntervalHour;
    }

    public void setRunIntervalHour(String runIntervalHour) {
        this.runIntervalHour = runIntervalHour;
    }

    public String getRunIntervalMinute() {
        return runIntervalMinute;
    }

    public void setRunIntervalMinute(String runIntervalMinute) {
        this.runIntervalMinute = runIntervalMinute;
    }

    public String getRunIntervalSecond() {
        return runIntervalSecond;
    }

    public void setRunIntervalSecond(String runIntervalSecond) {
        this.runIntervalSecond = runIntervalSecond;
    }
}
