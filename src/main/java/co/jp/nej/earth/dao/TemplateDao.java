package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.MgrTemplate;

public interface TemplateDao {
	public MgrTemplate getTemplate(String workspaceId, String templateId) throws EarthException;

	public TemplateData getProcessTemplateData(String workspaceId, String processId, String workItemId,
			String templateId, int maxVersion) throws EarthException;

	public List<MgrTemplate> getTemplates(String workspaceId, List<String> templateIds) throws EarthException;

	public TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId,
			int maxVersion) throws EarthException;

	public TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
			String templateId, int maxVersion) throws EarthException;

	public TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
			Integer docNo, String templateId, int maxVersion) throws EarthException;

	public TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
			Integer layerNo, String templateId, int maxVersion) throws EarthException;

	public int insertProcessTemplateData(String workspaceId, ProcessMap process);

	public int insertWorkItemTemplateData(String workspaceId, WorkItem workItem);

	public int insertFolderItemTemplateData(String workspaceId, FolderItem folderItem);

	public int insertDocumentTemplateData(String workspaceId, Document document);

	public int insertLayerTemplateData(String workspaceId, Layer layer);

	public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException;
}
