package co.jp.nej.earth.model.xml;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author p-tvo-sonta
 *
 */
public class FolderItem {
    private long folderItemNo;
    private long workItemId;
    private String template;
    private List<Document> documents;
    private Map<String, String> attributes;

    /**
     * @return the folderItemNo
     */
    public long getFolderItemNo() {
        return folderItemNo;
    }

    /**
     * @param folderItemNo
     *            the folderItemNo to set
     */
    public void setFolderItemNo(long folderItemNo) {
        this.folderItemNo = folderItemNo;
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
     * @return the documents
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents
     *            the documents to set
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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
