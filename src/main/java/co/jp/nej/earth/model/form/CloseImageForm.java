package co.jp.nej.earth.model.form;

import org.hibernate.validator.constraints.NotEmpty;

public class CloseImageForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,workitemId")
    private String workitemId;

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

}
