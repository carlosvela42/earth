package co.jp.nej.earth.model.entity;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QStrDataDb;
import co.jp.nej.earth.util.DateUtil;

public class StrDataDb extends BaseModel<StrDataDb> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String workitemId;
    private int folderItemNo;
    private int documentNo;
    private String documentData;

    public StrDataDb() {
        this.setqObj(QStrDataDb.newInstance());
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
    public int getFolderItemNo() {
        return folderItemNo;
    }

    /**
     * @param folderItemNo
     *            the folderItemNo to set
     */
    public void setFolderItemNo(int folderItemNo) {
        this.folderItemNo = folderItemNo;
    }

    /**
     * @return the documentNo
     */
    public int getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo
     *            the documentNo to set
     */
    public void setDocumentNo(int documentNo) {
        this.documentNo = documentNo;
    }

    /**
     * @return the documentData
     */
    public String getDocumentData() {
        return documentData;
    }

    /**
     * @param documentData
     *            the documentData to set
     */
    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }

}
