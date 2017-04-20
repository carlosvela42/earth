package co.jp.nej.earth.dao;

import co.jp.nej.earth.model.WorkItem;

/**
 * @author p-dcv-khanhnv
 *
 */
public interface WorkItemDao extends BaseDao<WorkItem> {
    WorkItem getWorkItemDataStructure(long workItemId);

    WorkItem getWorkItemStructure(long workItemId);
    //
    // public Folder getFolderByWorkItemId(long workItemId);
    // public long getFolderId(long workItemId);
    // public Folder getFolderById(long folderId);
    // public FolderItem getFolderItemIds(long folderId);
    // public List<String> getFolderItemNames(long folderId);
    // public List<FolderItem> getFolderItems(long folderId);
    // public List<WorkItem> getWorkItemsByColumns(List<String> columns, List<?>
    // values);
    //
    // public int updateWorkItem(WorkItem workItem);
    // public boolean deleteWorkItem(long workItemId);
    // public boolean deleteProcess(long workItemId);
    // public boolean deleteFolder(long folderId);
}