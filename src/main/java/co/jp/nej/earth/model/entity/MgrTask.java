package co.jp.nej.earth.model.entity;

/**
 *
 * @author p-tvo-sonta
 *
 */
public class MgrTask {
    private String taskId;
    private String taskName;
    private String className;
    private String processId;
    private String lastUpdateTime;

    public MgrTask() {
        super();
    }

    public MgrTask(String taskId, String taskName, String className, String processId, String lastUpdateTime) {
        super();
        this.taskId = taskId;
        this.taskName = taskName;
        this.className = className;
        this.processId = processId;
        this.lastUpdateTime = lastUpdateTime;
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
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     *            the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
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
