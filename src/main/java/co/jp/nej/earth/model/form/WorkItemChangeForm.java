package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.WorkItem;

public class WorkItemChangeForm {
    private String workspaceId;
    private WorkItem workItem;

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId
     *            the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * @return the workItem
     */
    public WorkItem getWorkItem() {
        return workItem;
    }

    /**
     * @param workItem
     *            the workItem to set
     */
    public void setWorkItem(WorkItem workItem) {
        this.workItem = workItem;
    }

}
