package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class CtlEvent implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String eventId;
    private String userId;
    private String workitemId;
    private String status;
    private String taskId;
    private String workitemData;
    private String lastUpdateTime;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkitemData() {
        return workitemData;
    }

    public void setWorkitemData(String workitemData) {
        this.workitemData = workitemData;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
