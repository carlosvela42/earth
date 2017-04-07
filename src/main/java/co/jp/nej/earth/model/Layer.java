package co.jp.nej.earth.model;

import java.util.List;

import co.jp.nej.earth.model.entity.MgrTemplate;

public class Layer {

    private String workItemId;
    private Integer folderItemNo;
    private Integer documentNo;
    private Integer layerNo;
    private MgrTemplate mgrTemplate;
    private String ownerId;
    private Integer action;
    private TemplateData layerData;
    private List<Annotation> annotations;

    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return workItemId;
    }

    /**
     * @param workItemId
     *            the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
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

    public MgrTemplate getMgrTemplate() {
        return mgrTemplate;
    }

    public void setMgrTemplate(MgrTemplate mgrTemplate) {
        this.mgrTemplate = mgrTemplate;
    }

    /**
     * @return the ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId
     *            the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return the action
     */
    public Integer getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(Integer action) {
        this.action = action;
    }

    /**
     * @return the layerData
     */
    public TemplateData getLayerData() {
        return layerData;
    }

    /**
     * @param layerData
     *            the layerData to set
     */
    public void setLayerData(TemplateData layerData) {
        this.layerData = layerData;
    }

    /**
     * @return the annotations
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

}
