package co.jp.nej.earth.model;

public class DocumentData {
    private Long documentId;
    private Integer seqNo;
    private Byte[] binaryFile;
    private String filePath;

    /**
     * @return the documentId
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId
     *            the documentId to set
     */
    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the seqNo
     */
    public Integer getSeqNo() {
        return seqNo;
    }

    /**
     * @param seqNo
     *            the seqNo to set
     */
    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    /**
     * @return the binaryFile
     */
    public Byte[] getBinaryFile() {
        return binaryFile;
    }

    /**
     * @param binaryFile
     *            the binaryFile to set
     */
    public void setBinaryFile(Byte[] binaryFile) {
        this.binaryFile = binaryFile;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
