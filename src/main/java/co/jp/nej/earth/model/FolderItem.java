/**
 *
 */
package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QFolderItem;

/**
 * @author p-tvo-khanhnv
 *
 */
public class FolderItem extends BaseModel<FolderItem> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String workitemId;
    @NotEmpty(message = "E0002,folderItemNo")
    private Integer folderItemNo;
    private MgrTemplate mgrTemplate;
    private Integer action;
    private TemplateData folderItemData;
    private String templateId;
    private List<Document> documents = new ArrayList<Document>();

    public FolderItem() {
        this.setqObj(QFolderItem.newInstance());
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
