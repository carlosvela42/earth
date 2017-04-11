package co.jp.nej.earth.model.xml;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author p-tvo-sonta
 *
 */
public class Document {
    private long documentNo;
    private String dataFile;
    private String dataDb;
    private long workItemId;
    private String template;
    private int pageCount;
    private String displayInformation;
    private String documentType;

    private Map<String, String> attributes;

    /**
     * @return the documentNo
     */
    public long getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo
     *            the documentNo to set
     */
    public void setDocumentNo(long documentNo) {
        this.documentNo = documentNo;
    }

    /**
     * @return the dataFile
     */
    public String getDataFile() {
        return dataFile;
    }

    /**
     * @param dataFile
     *            the dataFile to set
     */
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * @return the dataDb
     */
    public String getDataDb() {
        return dataDb;
    }

    /**
     * @param dataDb
     *            the dataDb to set
     */
    public void setDataDb(String dataDb) {
        this.dataDb = dataDb;
    }

    /**
     * @return the workItemId
     */
    public long getWorkItemId() {
        return workItemId;
    }

    /**
     * @param workItemId
     *            the workItemId to set
     */
    public void setWorkItemId(long workItemId) {
        this.workItemId = workItemId;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
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
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    @XmlElement(name = "Attributes")
    @XmlJavaTypeAdapter(MapAdapter.class)
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
