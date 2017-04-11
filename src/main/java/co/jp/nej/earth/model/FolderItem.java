/**
 * 
 */
package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import co.jp.nej.earth.model.entity.MgrTemplate;

/**
 * @author p-tvo-khanhnv
 *
 */
public class FolderItem {
    /**
     * 
     */
    private String workItemId;
    private Integer folderItemNo;
    private MgrTemplate mgrTemplate;
    private Integer action;
    private TemplateData folderItemData;
    private List<Document> documents = new ArrayList<Document>();

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

    public MgrTemplate getMgrTemplate() {
        return mgrTemplate;
    }

    public void setMgrTemplate(MgrTemplate mgrTemplate) {
        this.mgrTemplate = mgrTemplate;
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
     * @return the folderItemData
     */
    public TemplateData getFolderItemData() {
        return folderItemData;
    }

    /**
     * @param folderItemData
     *            the folderItemData to set
     */
    public void setFolderItemData(TemplateData folderItemData) {
        this.folderItemData = folderItemData;
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

    public void addDocument(Document document) {
        if (document != null && !documents.contains(document)) {
            documents.add(document);
        }
    }
}
