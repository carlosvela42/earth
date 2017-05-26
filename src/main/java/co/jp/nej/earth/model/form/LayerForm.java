package co.jp.nej.earth.model.form;

import org.hibernate.validator.constraints.NotEmpty;

public class LayerForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,workspaceId")
    private String workitemId;
    @NotEmpty(message = "E0002,folderItemNo")
    private Integer folderItemNo;
    @NotEmpty(message = "E0002,documentNo")
    private Integer documentNo;
    @NotEmpty(message = "E0002,layerNo")
    private Integer layerNo;

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
     * @return the folderItemNo
     */
    public Integer getFolderItemNo() {
        return folderItemNo;
    }

    /**
     * @param folderItemNo
     *            the folderItemNo to set
     */
    public void setFolderItemNo(Integer folderItemNo) {
        this.folderItemNo = folderItemNo;
    }

    /**
     * @return the documentNo
     */
    public Integer getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo
     *            the documentNo to set
     */
    public void setDocumentNo(Integer documentNo) {
        this.documentNo = documentNo;
    }

    /**
     * @return the layerNo
     */
    public Integer getLayerNo() {
        return layerNo;
    }

    /**
     * @param layerNo
     *            the layerNo to set
     */
    public void setLayerNo(Integer layerNo) {
        this.layerNo = layerNo;
    }

}
