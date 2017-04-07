package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import co.jp.nej.earth.model.entity.MgrTemplate;

public class Document {

    private String workItemId;
    private String folderItemNo;
    private String documentNo;
    private MgrTemplate mgrTemplate;
    private int pageCount;
    private String displayInformation;
    private int documentType;
    private String lastUpdateTime;
    private Integer action;
    private TemplateData documentData;
    private String documentPath;
    private byte[] documentBinary;
    private List<Layer> layers = new ArrayList<Layer>();

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
    public String getFolderItemNo() {
        return folderItemNo;
    }

    /**
     * @param folderItemNo
     *            the folderItemNo to set
     */
    public void setFolderItemNo(String folderItemNo) {
        this.folderItemNo = folderItemNo;
    }

    /**
     * @return the documentNo
     */
    public String getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo
     *            the documentNo to set
     */
    public void setDocumentNo(String documentNo) {
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
    public int getPageCount() {
        return pageCount;
    }

    /**
     * @param pageCount
     *            the pageCount to set
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * @return the displayInformation
     */
    public String getDisplayInformation() {
        return displayInformation;
    }

    /**
     * @param displayInformation
     *            the displayInformation to set
     */
    public void setDisplayInformation(String displayInformation) {
        this.displayInformation = displayInformation;
    }

    /**
     * @return the documentType
     */
    public int getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType
     *            the documentType to set
     */
    public void setDocumentType(int documentType) {
        this.documentType = documentType;
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
