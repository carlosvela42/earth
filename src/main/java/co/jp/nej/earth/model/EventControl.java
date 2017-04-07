package co.jp.nej.earth.model;

public class EventControl {
    private String eventId;
    private String workItemId;
    private String userId;
    private int status;
    private String workItemData;
    private String taskId;
    /**
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }
    /**
     * @param eventId the eventId to set
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return workItemId;
    }
    /**
     * @param workItemId the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }
    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * @return the workItemData
     */
    public String getWorkItemData() {
        return workItemData;
    }
    /**
     * @param workItemData the workItemData to set
     */
    public void setWorkItemData(String workItemData) {
        this.workItemData = workItemData;
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
}
