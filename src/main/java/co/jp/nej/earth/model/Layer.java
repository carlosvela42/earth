package co.jp.nej.earth.model;

import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QLayer;
import co.jp.nej.earth.util.DateUtil;

public class Layer extends BaseModel<Layer> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String workitemId;
    private Integer folderItemNo;
    private Integer documentNo;
    private Integer layerNo;
    private MgrTemplate mgrTemplate;
    private String ownerId;
    private Integer action;
    private TemplateData layerData;
    private String templateId;
    private String annotations;

    public Layer() {
        this.setqObj(QLayer.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
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
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the annotations
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

}
