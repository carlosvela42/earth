
package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

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

	public long insertProcessTemplateData(String workspaceId, ProcessMap process, int historyNo) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(process.getMgrTemplate(),
				process.getProcessData(), process.getProcessId(), process.getWorkItemId(), null, null,
				null, historyNo);
	}

	public long insertWorkItemTemplateData(String workspaceId, WorkItem workItem, int historyNo) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(workItem.getMgrTemplate(),
				workItem.getWorkItemData(), null, workItem.getWorkItemId(), null, null,
				null, historyNo);
	}

	public long insertFolderItemTemplateData(String workspaceId, FolderItem folderItem, int historyNo) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(folderItem.getMgrTemplate(),
				folderItem.getFolderItemData(), null, folderItem.getWorkItemId(), folderItem.getFolderItemNo(), null,
				null, historyNo);
	}

	public long insertDocumentTemplateData(String workspaceId, Document document, int historyNo) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(document.getMgrTemplate(),
				document.getDocumentData(), null, document.getWorkItemId(), document.getFolderItemNo(), document.getDocumentNo(),
				null, historyNo);
	}

	public long insertLayerTemplateData(String workspaceId, Layer layer, int historyNo) throws EarthException {
		return ConnectionManager.getEarthQueryFactory(workspaceId).insertTemplateData(layer.getMgrTemplate(),
				layer.getLayerData(), null, layer.getWorkItemId(), layer.getFolderItemNo(), layer.getDocumentNo(),
				layer.getLayerNo(), historyNo);
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
    // public List<Template> getTemplates(List<String> templateIds) {
    // QTemplate qTemplate = QTemplate.newInstance("QTemplate",
    // "spring_boot_test", "TEMPLATE");
    // QField qField = QField.newInstance("QField", "spring_boot_test",
    // "FIELD");
    // QTemplateField qTemplateField =
    // QTemplateField.newInstance("QTemplateField", "spring_boot_test",
    // "TEMPLATEFIELD");
    // ResultSet resultSet = queryFactory
    // .select(
    // qTemplate.templateId
    // ,qTemplate.templateName
    // ,qTemplate.description.as("template_description")
    // ,qField.fieldId
    // ,qField.fieldName
    // ,qField.type
    // ,qField.description.as("field_description"))
    // .from(qField)
    // .innerJoin(qTemplateField).on(qField.fieldId.eq(qTemplateField.fieldId))
    // .innerJoin(qTemplate).on(qTemplateField.templateId.eq(qTemplate.templateId))
    // .where(qTemplate.templateId.in(templateIds)).getResults();
    // System.out.println(queryFactory
    // .select(
    // qTemplate.templateId
    // ,qTemplate.templateName
    // ,qTemplate.description.as("template_description")
    // ,qField.fieldId
    // ,qField.fieldName
    // ,qField.type
    // ,qField.description.as("field_description"))
    // .from(qField)
    // .innerJoin(qTemplateField).on(qField.fieldId.eq(qTemplateField.fieldId))
    // .innerJoin(qTemplate).on(qTemplateField.templateId.eq(qTemplate.templateId))
    // .where(qTemplate.templateId.in(templateIds)).getSQL());
    // List<Template> templates = new ArrayList<Template>();
    // Template template = new Template();
    // try {
    // while (resultSet.next()) {
    // String templateId =
    // resultSet.getString(TemplateColumnName.TEMPLATE_ID.columnName);
    // String templateName =
    // resultSet.getString(TemplateColumnName.TEMPLATE_NAME.columnName);
    // String templateDescription = resultSet.getString("template_description");
    //
    // if (resultSet.isFirst()) {
    // template = new Template(templateId, templateName, templateDescription);
    // } else {
    // if (template.getTemplateId() != null &&
    // !templateId.equals(template.getTemplateId())) {
    // templates.add(template);
    // template = new Template(templateId, templateName, templateDescription);
    // }
    // }
    //
    // // Set Field info
    // Long fieldId =
    // resultSet.getLong(QField.FieldColumnName.FIELD_ID.columnName);
    // String fieldName =
    // resultSet.getString(QField.FieldColumnName.FIELD_NAME.columnName);
    // int fieldType = resultSet.getInt(QField.FieldColumnName.TYPE.columnName);
    // String description = resultSet.getString("field_description");
    // Field field = new Field(fieldId, fieldName, fieldType, description);
    // template.addField(field);
    //
    // if (resultSet.isLast()) {
    // templates.add(template);
    // }
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    //
    // return templates;
    // }

    // public List<TemplateData> getTemplateData(Template template) {
    // // TODO Auto-generated method stub
    // return null;
    // }

////    public MgrTemplate getTemplate(String workspaceId, String templateId) throws EarthException {
//        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
//        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
//        return ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate)
//                .where(qMgrTemplate.templateId.eq(templateId)).fetchOne();
//    }

    public TemplateData getProcessTemplateData(String workspaceId, String processId, String templateId,
            int maxVersion) {
        // TODO Auto-generated method stub
        return null;
    }

//    public List<MgrTemplate> getTemplates(String workspaceId, List<String> templateIds) throws EarthException {
//        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
//        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
//        return ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate)
//                .where(qMgrTemplate.templateId.in(templateIds)).fetch();
//    }
//
//    public TemplateData getWorkItemTemplateData(String workspaceId, String workItemId, String templateId,
//            int maxVersion) throws EarthException {
//        MgrTemplate mgrTemplate = getTemplate(workspaceId, templateId);
//
//        return null;
//    }
//
//    public TemplateData getFolderItemTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
//            String templateId, int maxVersion) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    public TemplateData getDocumentTemplateData(String workspaceId, String workItemId, Integer folderItemNo,
//            Integer docNo, String templateId, int maxVersion) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    public TemplateData getLayerTemplateData(String workspaceId, String workItemId, Integer folderItemNo, Integer docNo,
//            Integer layerNo, String templateId, int maxVersion) {
//        // TODO Auto-generated method stub
//        return null;
//    }

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

////    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
//        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
//        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
//        return ConnectionManager.getEarthQueryFactory(workspaceId).select(selectList).from(qMgrTemplate).fetch();
//    }

    @Override
    public MgrTemplate getById(TemplateKey templateKey) throws EarthException {
        QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
        QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
        return ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId()).select(selectList)
                .from(qMgrTemplate).where(qMgrTemplate.templateId.eq(templateKey.getTemplateId())).fetchOne();
    }
}
