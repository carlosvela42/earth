package co.jp.nej.earth.model.form;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.Layer;

public class LayerUpdateForm {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,workspaceId")
    private String workitemId;
    @NotEmpty(message = "E0002,folderItemNo")
    private Integer folderItemNo;
    @NotEmpty(message = "E0002,documentNo")
    private Integer documentNo;
    @Valid
    private Layer layer;

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
     * @return the layer
     */
    public Layer getLayer() {
        return layer;
    }

    /**
     * @param layer
     *            the layer to set
     */
    public void setLayer(Layer layer) {
        this.layer = layer;
    }

}
