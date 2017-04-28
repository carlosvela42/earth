package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QDocument;
import co.jp.nej.earth.util.DateUtil;

public class Document extends BaseModel<Document> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String workitemId;
    private Integer folderItemNo;
    private Integer documentNo;
    private MgrTemplate mgrTemplate;
    private Integer pageCount;
    private String viewInformation;
    private String documentType;
    private Integer action;
    private TemplateData documentData;
    private String documentPath;
    private byte[] documentBinary;
    private String templateId;
    private List<Layer> layers = new ArrayList<Layer>();

    public Document() {
        this.setqObj(QDocument.newInstance());
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

    public MgrTemplate getMgrTemplate() {
        return mgrTemplate;
    }

    public void setMgrTemplate(MgrTemplate mgrTemplate) {
        this.mgrTemplate = mgrTemplate;
    }

    /**
     * @return the pageCount
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * @param pageCount
     *            the pageCount to set
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * @return the viewInformation
     */
    public String getViewInformation() {
        return viewInformation;
    }

    /**
     * @param viewInformation
     *            the viewInformation to set
     */
    public void setViewInformation(String viewInformation) {
        this.viewInformation = viewInformation;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType
     *            the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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
     * @return the documentData
     */
    public TemplateData getDocumentData() {
        return documentData;
    }

    /**
     * @param documentData
     *            the documentData to set
     */
    public void setDocumentData(TemplateData documentData) {
        this.documentData = documentData;
    }

    /**
     * @return the documentPath
     */
    public String getDocumentPath() {
        return documentPath;
    }

    /**
     * @param documentPath
     *            the documentPath to set
     */
    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    /**
     * @return the documentBinary
     */
    public byte[] getDocumentBinary() {
        return documentBinary;
    }

    /**
     * @param documentBinary
     *            the documentBinary to set
     */
    public void setDocumentBinary(byte[] documentBinary) {
        this.documentBinary = documentBinary;
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
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers
     *            the layers to set
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

}
