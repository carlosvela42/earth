package co.jp.nej.earth.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.nej.earth.model.sql.QMgrSchedule;

public class MgrSchedule extends BaseModel<MgrSchedule> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MgrSchedule.class);

    private String scheduleId;
    private int processId;
    private String processName;
    private String taskId;
    private String taskName;
    private String hostName;
    private String processIServiceId;
    private String startTime;
    private String endTime;
    private String enableDisable;
    private String nextRunDate;
    private String runIntervalDay;
    private String runIntervalHour;
    private String runIntervalMinute;
    private String runIntervalSecond;


    public MgrSchedule() {
        LOG.debug("Call to blank contructor");
        this.setqObj(QMgrSchedule.newInstance());
    }

    public MgrSchedule(String scheduleId, int processId, String processName, String taskId, String taskName,
                       String hostName, String processIServiceId, String startTime, String endTime) {
        this();
        this.scheduleId = scheduleId;
        this.processId = processId;
        this.processName = processName;
        this.taskId = taskId;
        this.taskName = taskName;
        this.hostName = hostName;
        this.processIServiceId = processIServiceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MgrSchedule(String scheduleId, int processId, String taskId, String hostName, String processIServiceId,
                       String enableDisable, String startTime, String endTime, String nextRunDate,
                       String runIntervalDay, String runIntervalHour, String runIntervalMinute,
                       String runIntervalSecond) {
        this();
        this.scheduleId = scheduleId;
        this.processId = processId;
        this.taskId = taskId;
        this.hostName = hostName;
        this.processIServiceId = processIServiceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enableDisable=enableDisable;
        this.nextRunDate=nextRunDate;
        this.runIntervalDay=runIntervalDay;
        this.runIntervalHour=runIntervalHour;
        this.runIntervalMinute=runIntervalMinute;
        this.runIntervalSecond=runIntervalSecond;
    }

    /**
     * @return the scheduleId
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * @param scheduleId the scheduleId to set
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * @return the processId
     */
    public int getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(int processId) {
        this.processId = processId;
    }

    /**
     * @return the processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * @param processName the processName to set
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the processIServiceId
     */
    public String getProcessIServiceId() {
        return processIServiceId;
    }

    /**
     * @param processIServiceId the processIseviceId to set
     */
    public void setProcessIServiceId(String processIServiceId) {
        this.processIServiceId = processIServiceId;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEnableDisable() {
        return enableDisable;
    }

    public void setEnableDisable(String enableDisable) {
        this.enableDisable = enableDisable;
    }

    public String getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(String nextRunDate) {
        this.nextRunDate = nextRunDate;
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
