package co.jp.nej.earth.model.entity;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QStrDataFile;

public class StrDataFile extends BaseModel<StrDataFile> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String workitemId;
    private int folderItemNo;
    private int documentNo;
    private String documentDataPath;

    public StrDataFile() {
        this.setqObj(QStrDataFile.newInstance());
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
     * @return the documentDataPath
     */
    public String getDocumentDataPath() {
        return documentDataPath;
    }

    /**
     * @param documentDataPath
     *            the documentDataPath to set
     */
    public void setDocumentDataPath(String documentDataPath) {
        this.documentDataPath = documentDataPath;
    }

}
