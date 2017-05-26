package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.MgrTemplate;

public interface TemplateDao extends BaseDao<MgrTemplate> {
    MgrTemplate getTemplate(String workspaceId, String templateId) throws EarthException;

    TemplateData getProcessTemplateData(String workspaceId, String processId, String workItemId, String templateId,
            int maxVersion) throws EarthException;

    List<MgrTemplate> getTemplates(String workspaceId, List<String> templateIds) throws EarthException;

    TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId, int maxVersion)
            throws EarthException;

    TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
            String templateId, int maxVersion) throws EarthException;

    TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
            String templateId, int maxVersion) throws EarthException;

    TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
            Integer layerNo, String templateId, int maxVersion) throws EarthException;

    long insertProcessTemplateData(String workspaceId, ProcessMap process, int historyNo) throws EarthException;

    long insertWorkItemTemplateData(String workspaceId, WorkItem workItem, int historyNo) throws EarthException;

    long insertFolderItemTemplateData(String workspaceId, FolderItem folderItem, int historyNo) throws EarthException;

    long insertDocumentTemplateData(String workspaceId, Document document, int historyNo) throws EarthException;

    long insertLayerTemplateData(String workspaceId, Layer layer, int historyNo) throws EarthException;

    List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException;

    MgrTemplate getById(TemplateKey templateKey) throws EarthException;

    List<MgrTemplate> getTemplateByType(String workspaceId, String templateType) throws EarthException;

    TemplateData getProcessTemplateData(String workspaceId, String processId, String templateId, int maxVersion)
            throws EarthException;

    long deleteTemplates(List<String> templateIds, String workspaceId) throws EarthException;

    long insertOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException;

    long createTemplateData(String workspaceId, MgrTemplate mgrTemplate) throws EarthException;

    long updateOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException;

    long isExistsTableData(String workspaceId, String templateTableName, String dbUser) throws EarthException;
}
