package co.jp.nej.earth.model;

public class MgrSchedule {

    private String scheduleId;
    private String processId;
    private String taskId;
    private String hostName;
    private String processIseviceId;
    private String startTime;
    private String repeatOption;
    private String endTime;
    private String lastUpdateTime;

    public MgrSchedule() {
        super();
    }

    public MgrSchedule(String scheduleId, String processId, String taskId, String hostName, String processIseviceId,
            String startTime, String repeatOption, String endTime, String lastUpdateTime) {
        super();
        this.scheduleId = scheduleId;
        this.processId = processId;
        this.taskId = taskId;
        this.hostName = hostName;
        this.processIseviceId = processIseviceId;
        this.startTime = startTime;
        this.repeatOption = repeatOption;
        this.endTime = endTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the scheduleId
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * @param scheduleId
     *            the scheduleId to set
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * @return the processId
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * @param processId
     *            the processId to set
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     *            the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the processIseviceId
     */
    public String getProcessIseviceId() {
        return processIseviceId;
    }

    /**
     * @param processIseviceId
     *            the processIseviceId to set
     */
    public void setProcessIseviceId(String processIseviceId) {
        this.processIseviceId = processIseviceId;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the repeatOption
     */
    public String getRepeatOption() {
        return repeatOption;
    }

    /**
     * @param repeatOption
     *            the repeatOption to set
     */
    public void setRepeatOption(String repeatOption) {
        this.repeatOption = repeatOption;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *            the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
