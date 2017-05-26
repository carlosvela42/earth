
package co.jp.nej.earth.dao;

import java.sql.Statement;
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
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.Type;
import co.jp.nej.earth.model.sql.QMgrTemplate;
import co.jp.nej.earth.util.EStringUtil;

@Repository
public class TemplateDaoImpl extends BaseDaoImpl<MgrTemplate> implements TemplateDao {

    public TemplateDaoImpl() throws Exception {
        super();
    }

    public MgrTemplate getTemplate(String workspaceId, String templateId) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
            MgrTemplate template = ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList)
                    .from(qMgrTemplate).where(qMgrTemplate.templateId.eq(templateId)).fetchOne();

            if (template != null && !EStringUtil.isEmpty(template.getTemplateField())) {
                template.addTemplateFields(template.getTemplateField());
            }

            return template;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    public List<MgrTemplate> getTemplates(String workspaceId, List<String> templateIds) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
            return ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate)
                    .where(qMgrTemplate.templateId.in(templateIds)).fetch();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public TemplateData getProcessTemplateData(String workspaceId, String processId, String workItemId,
            String templateId, int maxVersion) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
                    getTemplate(workspaceId, templateId), processId, workItemId, null, null, null, maxVersion);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId,
            int maxVersion) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
                    getTemplate(workspaceId, templateId), null, workItemId, null, null, null, maxVersion);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
            String templateId, int maxVersion) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
                    getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, null, null, maxVersion);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
            Integer docNo, String templateId, int maxVersion) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
                    getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, docNo, null, maxVersion);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
            Integer layerNo, String templateId, int maxVersion) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).getTemplateData(
                    getTemplate(workspaceId, templateId), null, workItemId, folderItemNo, docNo, layerNo, maxVersion);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long insertProcessTemplateData(String workspaceId, ProcessMap process, int historyNo) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(process.getMgrTemplate(),
                    process.getProcessData(), Integer.toString(process.getProcessId()), process.getWorkItemId(), null,
                    null, null);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long insertWorkItemTemplateData(String workspaceId, WorkItem workItem, int historyNo) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(workItem.getMgrTemplate(),
                    workItem.getWorkItemData(), null, workItem.getWorkitemId(), null, null, null);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long insertFolderItemTemplateData(String workspaceId, FolderItem folderItem, int historyNo)
            throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(folderItem.getMgrTemplate(),
                    folderItem.getFolderItemData(), null, folderItem.getWorkitemId(), folderItem.getFolderItemNo(),
                    null, null);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long insertDocumentTemplateData(String workspaceId, Document document, int historyNo) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(document.getMgrTemplate(),
                    document.getDocumentData(), null, document.getWorkitemId(), document.getFolderItemNo(),
                    document.getDocumentNo(), null);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long insertLayerTemplateData(String workspaceId, Layer layer, int historyNo) throws EarthException {
        try {
            return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(layer.getMgrTemplate(),
                    layer.getLayerData(), null, layer.getWorkitemId(), layer.getFolderItemNo(), layer.getDocumentNo(),
                    layer.getLayerNo());
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
        try {
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
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public MgrTemplate getById(TemplateKey templateKey) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
            return ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId()).select(selectList)
                    .from(qMgrTemplate).where(qMgrTemplate.templateId.eq(templateKey.getTemplateId())).fetchOne();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public List<MgrTemplate> getTemplateByType(String workspaceId, String templateType) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
            List<MgrTemplate> mgrTemplates = ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList)
                    .from(qMgrTemplate).where(qMgrTemplate.templateType.eq(templateType)).fetch();
            return mgrTemplates;
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public TemplateData getProcessTemplateData(String workspaceId, String processId, String templateId, int maxVersion)
            throws EarthException {
        return ConnectionManager.getEarthQueryFactory(workspaceId)
                .getProcessTemplateData(getTemplate(workspaceId, templateId), processId, maxVersion);
    }

    public long deleteTemplates(List<String> templateIds, String workspaceId) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
        for (String templateId : templateIds) {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrTemplate.templateId, templateId);
            conditions.add(condition);
        }
        return deleteList(workspaceId, conditions);
    }

    @Override
    public long insertOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException {
        try {
            return add(workspaceId, mgrTemplate);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public long updateOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            Map<Path<?>, Object> condition = new HashMap<>();
            Map<Path<?>, Object> value = new HashMap<>();
            condition.put(qMgrTemplate.templateId, mgrTemplate.getTemplateId());
            value.put(qMgrTemplate.templateName, mgrTemplate.getTemplateName());
            return update(workspaceId, condition, value);
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    public long createTemplateData(String workspaceId, MgrTemplate mgrTemplate) throws EarthException {
        try {
            String nameTable = "CREATE TABLE " + mgrTemplate.getTemplateTableName() + " ";
            List<Field> fields = mgrTemplate.getTemplateFields();
            String createField = "(ID INT GENERATED ALWAYS AS IDENTITY,";
            for (Field field : fields) {
                if (field.getType().equals(Constant.Template.FIELD_TYPTE_1)) {
                    createField += field.getName() + " " + Type.NUMBER + ",";
                } else if (field.getType().equals(Constant.Template.FIELD_TYPTE_3)) {
                    createField += field.getName() + " " + Type.LONG + ",";
                } else if (field.getType().equals(Constant.Template.FIELD_TYPTE_2)) {
                    createField += field.getName() + " " + Type.NVARCHAR2 + "(" + field.getSize() + ")" + ",";
                } else {
                    createField += field.getName() + " " + Type.NCHAR + "(" + field.getSize() + ")" + ",";
                }
            }
            createField += "PRIMARY KEY (ID))";
            String sql = nameTable + createField;
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            Statement stmt = earthQueryFactory.getConnection().createStatement();
            return stmt.executeUpdate(sql);

        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public long isExistsTableData(String workspaceId, String templateTableName, String dbUser) throws EarthException {
        try {
            String isExistsTable = "SELECT * FROM dba_tables where table_name = '" + templateTableName
                    + "' and owner = '" + dbUser + "'";
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            Statement stmt = earthQueryFactory.getConnection().createStatement();
            return stmt.executeUpdate(isExistsTable);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
