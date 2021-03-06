package co.jp.nej.earth.model.ws;

import org.hibernate.validator.constraints.NotEmpty;

public class SaveAndCloseImagesRequest extends Request {
    @NotEmpty(message = "E0001,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0001,workItemId")
    private String workItemId;
    @NotEmpty(message = "E0001,folderItemNo")
    private String folderItemNo;

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
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
     * @return the folderItemNo
     */
    public String getFolderItemNo() {
        return folderItemNo;
    }

    /**
     * @param folderItemNo the folderItemNo to set
     */
    public void setFolderItemNo(String folderItemNo) {
        this.folderItemNo = folderItemNo;
    }

}
