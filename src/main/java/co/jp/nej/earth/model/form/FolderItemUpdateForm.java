package co.jp.nej.earth.model.form;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.FolderItem;

public class FolderItemUpdateForm {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,workspaceId")
    private String workitemId;
    @Valid
    private FolderItem folderItem;

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
     * @return the workitemId
     */
    public String getWorkitemId() {
        return workitemId;
    }

    /**
     * @param workitemId
     *            the workitemId to set
     */
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    /**
     * @return the folderItem
     */
    public FolderItem getFolderItem() {
        return folderItem;
    }

    /**
     * @param folderItem
     *            the folderItem to set
     */
    public void setFolderItem(FolderItem folderItem) {
        this.folderItem = folderItem;
    }

}
