/**
 * 
 */
package co.jp.nej.earth.model;

import java.util.ArrayList;
import java.util.List;

import co.jp.nej.earth.model.entity.MgrTemplate;

/**
 * @author p-dcv-khanhnv
 *
 */
public class WorkItem {
    /**
     * 
     */
    private String workspaceId;
    private String workItemId;
    private Integer eventStatus;
    private String taskId;
    private MgrTemplate mgrTemplate;
    private TemplateData workItemData;
    private Long templateId;
    private List<FolderItem> folderItems = new ArrayList<FolderItem>();
    
    public WorkItem() {
    }

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return workItemId;
    }

    /**
     * @param workItemId the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }

    /**
     * @return the eventStatus
     */
    public Integer getEventStatus() {
        return eventStatus;
    }

    /**
     * @param eventStatus the eventStatus to set
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
     * @param taskId the taskId to set
     */
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
     * @param workItemData the workItemData to set
     */
    public void setWorkItemData(TemplateData workItemData) {
        this.workItemData = workItemData;
    }

    /**
     * @return the templateId
     */
    public Long getTemplateId() {
        return templateId;
    }


    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the folderItems
     */
    public List<FolderItem> getFolderItems() {
        return folderItems;
    }

    /**
     * @param folderItems the folderItems to set
     */
    public void setFolderItems(List<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    public void addFolderItem(FolderItem folderItem) {
        if (!folderItems.contains(folderItem)) {
            folderItems.add(folderItem);
        }
    }
}