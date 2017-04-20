
package co.jp.nej.earth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplate;

@Repository
public class TemplateDaoImpl extends BaseDaoImpl<MgrTemplate> implements TemplateDao {

    public TemplateDaoImpl() throws Exception {
        super();
    }

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
        return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(getTemplate(workspaceId, templateId),
                processId, workItemId, null, null, null, maxVersion);
    }

    public TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId,
            int maxVersion) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(getTemplate(workspaceId, templateId),
                null, workItemId, null, null, null, maxVersion);
    }

    public TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
            String templateId, int maxVersion) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(getTemplate(workspaceId, templateId),
                null, workItemId, folderItemNo, null, null, maxVersion);
    }

    public TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
            Integer docNo, String templateId, int maxVersion) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(getTemplate(workspaceId, templateId),
                null, workItemId, folderItemNo, docNo, null, maxVersion);
    }

    public TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
            Integer layerNo, String templateId, int maxVersion) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(getTemplate(workspaceId, templateId),
                null, workItemId, folderItemNo, docNo, layerNo, maxVersion);
    }

    public long insertProcessTemplateData(String workspaceId, ProcessMap process, int historyNo) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(process.getMgrTemplate(),
                process.getProcessData(), process.getProcessId(), process.getWorkItemId(), null, null, null);
    }

    public long insertWorkItemTemplateData(String workspaceId, WorkItem workItem, int historyNo) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(workItem.getMgrTemplate(),
                workItem.getWorkItemData(), null, workItem.getWorkitemId(), null, null, null);
    }

    public long insertFolderItemTemplateData(String workspaceId, FolderItem folderItem, int historyNo)
            throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(folderItem.getMgrTemplate(),
                folderItem.getFolderItemData(), null, folderItem.getWorkitemId(), folderItem.getFolderItemNo(), null,
                null);
    }

    public long insertDocumentTemplateData(String workspaceId, Document document, int historyNo) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(document.getMgrTemplate(),
                document.getDocumentData(), null, document.getWorkitemId(), document.getFolderItemNo(),
                document.getDocumentNo(), null);
    }

    public long insertLayerTemplateData(String workspaceId, Layer layer, int historyNo) throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(layer.getMgrTemplate(),
                layer.getLayerData(), null, layer.getWorkitemId(), layer.getFolderItemNo(), layer.getDocumentNo(),
                layer.getLayerNo());
    }

    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
        List<MgrTemplate> mgrTemplates = ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList)
                .from(qMgrTemplate).fetch();

        if (mgrTemplates != null && mgrTemplates.size() > 0) {
            for (MgrTemplate template : mgrTemplates) {
                template.addTemplateFields(template.getTemplateField());
            }
        }

        return mgrTemplates;
    }

    @Override
    public MgrTemplate getById(TemplateKey templateKey) throws EarthException {
        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
        return ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId()).select(selectList)
                .from(qMgrTemplate).where(qMgrTemplate.templateId.eq(templateKey.getTemplateId())).fetchOne();
    }

    @Override
    public List<MgrTemplate> getTemplateByType(String workspaceId, String templateType) throws EarthException {
        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
        List<MgrTemplate> mgrTemplates = ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList)
                .from(qMgrTemplate).where(qMgrTemplate.templateType.eq(templateType)).fetch();
        return mgrTemplates;
    }

    public boolean deleteTemplates(List<String> templateIds, String workspaceId) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            for (String templateId : templateIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrTemplate.templateId, templateId);
                conditions.add(condition);
            }
            return this.deleteList(workspaceId, conditions) > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
