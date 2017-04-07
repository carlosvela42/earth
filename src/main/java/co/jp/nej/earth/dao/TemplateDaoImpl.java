package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplate;
import co.jp.nej.earth.util.EStringUtil;

@Repository
public class TemplateDaoImpl implements TemplateDao {

	public MgrTemplate getTemplate(String workspaceId, String templateId) throws EarthException {
		QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
		QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
		
		MgrTemplate template = ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate)
				.where(qMgrTemplate.templateId.eq(templateId)).fetchOne();
		
		template.addTemplateFields(template.getTemplateField());
		return template;
	}


	public List<MgrTemplate> getTemplates(String workspaceId, List<String> templateIds) throws EarthException {
		QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
		QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
		return ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate)
				.where(qMgrTemplate.templateId.in(templateIds)).fetch();
	}
	
	public TemplateData getProcessTemplateData(String workspaceId, String processId, String workItemId,
			String templateId, int maxVersion) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
				getTemplate(workspaceId, templateId), processId, workItemId, null, null, null, maxVersion);
	}

	public TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId,
			int maxVersion) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
				getTemplate(workspaceId, templateId), null, workItemId, null, null, null, maxVersion);
	}

	public TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
			String templateId, int maxVersion) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
				getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, null, null, maxVersion);
	}

	public TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
			Integer docNo, String templateId, int maxVersion) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
				getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, docNo, null, maxVersion);
	}

	public TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
			Integer layerNo, String templateId, int maxVersion) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
				getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, docNo, layerNo, maxVersion);
	}

	public int insertProcessTemplateData(String workspaceId, ProcessMap process) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertWorkItemTemplateData(String workspaceId, WorkItem workItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertFolderItemTemplateData(String workspaceId, FolderItem folderItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertDocumentTemplateData(String workspaceId, Document document) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertLayerTemplateData(String workspaceId, Layer layer) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
		QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
		QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
		List<MgrTemplate> mgrTemplates = ConnectionManager
				.getEarthQueryFactory(workspaceId)
				.select(selectList)
				.from(qMgrTemplate)
				.fetch();
		
		if (mgrTemplates != null && mgrTemplates.size() > 0) {
			for (MgrTemplate template : mgrTemplates) {
				template.addTemplateFields(template.getTemplateField());
			}
		}
		
		return mgrTemplates;
	}
}
