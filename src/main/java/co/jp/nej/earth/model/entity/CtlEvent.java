package co.jp.nej.earth.model.entity;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.util.DateUtil;

public class CtlEvent extends BaseModel<CtlEvent> {
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

    public CtlEvent() {
        this.setqObj(QCtlEvent.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

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

}
