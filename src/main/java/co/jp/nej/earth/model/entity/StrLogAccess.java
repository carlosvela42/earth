package co.jp.nej.earth.model.entity;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import co.jp.nej.earth.util.DateUtil;

public class StrLogAccess extends BaseModel<StrLogAccess> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String eventId;
    private String processTime;
    private String userId;
    private String workitemId;
    private int historyNo;
    private int processId;
    private long processVersion;
    private String taskId;

    public StrLogAccess() {
        this.setqObj(QStrLogAccess.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public StrLogAccess(String eventId, String processTime, String userId, String workitemId, int historyNo,
            int processId, long processVersion, String taskId) {
        this();
        this.eventId = eventId;
        this.processTime = processTime;
        this.userId = userId;
        this.workitemId = workitemId;
        this.historyNo = historyNo;
        this.processId = processId;
        this.processVersion = processVersion;
        this.taskId = taskId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
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

    public int getHistoryNo() {
        return historyNo;
    }

    public void setHistoryNo(int historyNo) {
        this.historyNo = historyNo;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public long getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(long processVersion) {
        this.processVersion = processVersion;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

}
