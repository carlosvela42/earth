/**
 *
 */
package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QWorkItem;

/**
 * @author p-dcv-khanhnv
 *
 */
@XmlRootElement
public class WorkItem extends BaseModel<WorkItem> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String workspaceId;
    @NotEmpty(message = "E0002,workitemId")
    private String workitemId;
    private Integer eventStatus;
    private String taskId;
    private MgrTemplate mgrTemplate;
    private TemplateData workItemData;
    private String templateId;
    private List<FolderItem> folderItems = new ArrayList<FolderItem>();
    private int lastHistoryNo;

    private ProcessMap processMap;

    public WorkItem() {
        this.setqObj(QWorkItem.newInstance());
    }

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId
     *            the workspaceId to set
     */
    @XmlElement
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
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
    @XmlElement
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    /**
     * @return the eventStatus
     */
    public Integer getEventStatus() {
        return eventStatus;
    }

    /**
     * @param eventStatus
     *            the eventStatus to set
     */
    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     *            the taskId to set
     */
    @XmlElement
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public MgrTemplate getMgrTemplate() {
        return mgrTemplate;
    }

    public void setMgrTemplate(MgrTemplate mgrTemplate) {
        this.mgrTemplate = mgrTemplate;
    }

    /**
     * @return the workItemData
     */
    public TemplateData getWorkItemData() {
        return workItemData;
    }

    /**
     * @param workItemData
     *            the workItemData to set
     */
    public void setWorkItemData(TemplateData workItemData) {
        this.workItemData = workItemData;
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
    @XmlElement
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the folderItems
     */
    public List<FolderItem> getFolderItems() {
        return folderItems;
    }

    /**
     * @param folderItems
     *            the folderItems to set
     */
    @XmlElement
    public void setFolderItems(List<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    public void addFolderItem(FolderItem folderItem) {
        if (!folderItems.contains(folderItem)) {
            folderItems.add(folderItem);
        }
    }

    /**
     * @return the lastHistoryNo
     */
    public int getLastHistoryNo() {
        return lastHistoryNo;
    }

    /**
     * @param lastHistoryNo
     *            the lastHistoryNo to set
     */
    public void setLastHistoryNo(int lastHistoryNo) {
        this.lastHistoryNo = lastHistoryNo;
    }

    /**
     * @return the processMap
     */
    public ProcessMap getProcessMap() {
        return processMap;
    }

    /**
     * @param processMap the processMap to set
     */
    public void setProcessMap(ProcessMap processMap) {
        this.processMap = processMap;
    }
}