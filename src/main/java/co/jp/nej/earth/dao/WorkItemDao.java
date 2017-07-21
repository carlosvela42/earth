package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Column;
import co.jp.nej.earth.model.Row;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.WorkItemListDTO;
import co.jp.nej.earth.web.form.SearchForm;

import java.util.List;
import java.util.Map;

/**
 * @author p-dcv-khanhnv
 */
public interface WorkItemDao extends BaseDao<WorkItem> {
    Map<WorkItem, List<String>> getWorkItemStructure(String workspaceId, String workitemId) throws EarthException;
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

    List<WorkItemListDTO> getWorkItemsByWorkspace(String workspaceId) throws EarthException;

    List<WorkItemListDTO> getWorkItemsByWorkspace(String workspaceId,List<String> workItemIds) throws EarthException;

    long unlock(List<String> workItemId, String workspaceId) throws EarthException;

    List<Row> getRowValue(SearchForm searchForm, List<Column> columns, String workspaceId, String stringCondition)
            throws EarthException;

    Integer getProcessIdByWorkItem(String workspaceId, String workitemId) throws EarthException;
    Integer getMaxHistoryNo(String workspaceId, String workitemId) throws EarthException;
    long updateWorkItem(WorkItem workItem) throws EarthException;
}