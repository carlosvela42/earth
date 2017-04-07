/**
 * 
 */
package co.jp.nej.earth.manager.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.sql.QTemplateData;
import co.jp.nej.earth.util.EStringUtil;

/**
 * @author p-tvo-khanhnv
 *
 */
public class EarthQueryFactory extends SQLQueryFactory {

	/**
	 * @param configuration
	 * @param dataSource
	 */
	public EarthQueryFactory(Configuration configuration, Provider<Connection> provider) {
		super(configuration, provider);
	}

	@Override
	public SQLQuery<Tuple> select(Expression<?>... exprs) {
		return super.select(exprs);
	}

	public SQLQuery<Tuple> selectAll(Expression<?>... exprs) {

		return super.select(exprs);
	}

	/**
	 * Get Template data information.
	 * 
	 * @param template
	 * @param processId
	 * @param workItemId
	 * @param folderItemNo
	 * @param documentNo
	 * @param layerNo
	 * @param maxVersion
	 * 
	 */
	public TemplateData getTemplateData(MgrTemplate template, String processId, String workItemId,
			Integer folderItemNo, Integer documentNo, Integer layerNo, int maxVersion) throws EarthException {
		if (template == null || EStringUtil.isEmpty(template.getTemplateId())) {
			return null;
		}

		QTemplateData qTemplateData = QTemplateData.newInstance(template.getTemplateName(),
				template.getTemplateFields());
		QBean<TemplateData> selectList = Projections.bean(TemplateData.class, qTemplateData.all());

		Predicate whereClauses = null;
		if (TemplateType.isProcess(template.getTemplateType())) {
			whereClauses = qTemplateData.processId.eq(processId)
					.and(qTemplateData.workItemId.eq(workItemId))
					.and(qTemplateData.historyNo.eq(maxVersion));
		}

		if (TemplateType.isWorkItem(template.getTemplateType())) {
			whereClauses = qTemplateData.workItemId.eq(workItemId)
					.and(qTemplateData.historyNo.eq(maxVersion));
		}

		if (TemplateType.isFolderItem(template.getTemplateType())) {
			whereClauses = qTemplateData.workItemId.eq(workItemId)
					.and(qTemplateData.folderItemNo.eq(folderItemNo))
					.and(qTemplateData.historyNo.eq(maxVersion));
		}

		if (TemplateType.isDocument(template.getTemplateType())) {
			whereClauses = qTemplateData.workItemId.eq(workItemId)
					.and(qTemplateData.folderItemNo.eq(folderItemNo))
					.and(qTemplateData.documentNo.eq(documentNo))
					.and(qTemplateData.historyNo.eq(maxVersion));
		}

		if (TemplateType.isLayer(template.getTemplateType())) {
			whereClauses = qTemplateData.workItemId.eq(workItemId)
					.and(qTemplateData.folderItemNo.eq(folderItemNo))
					.and(qTemplateData.documentNo.eq(documentNo))
					.and(qTemplateData.layerNo.eq(layerNo))
					.and(qTemplateData.historyNo.eq(maxVersion));
		}

		TemplateData templateData = null;
		try {
			ResultSet resultSet = select(selectList).from(qTemplateData).where(whereClauses).getResults();

			if (resultSet.next()) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				for (Field field : template.getTemplateFields()) {
					Object columnValue = resultSet.getObject(field.getName());
					dataMap.put(field.getName(), columnValue);
				}

				String lastUpdateTime = resultSet.getString(ColumnNames.LAST_UPDATE_TIME.toString());
				Integer historyNo = resultSet.getInt(ColumnNames.HISTORY_NO.toString());

				templateData = new TemplateData(historyNo, lastUpdateTime, dataMap);
			}
		} catch (SQLException e) {
			throw new EarthException(e.getErrorCode() + ":" + e.getMessage());
		}

		return templateData;
	}
	
	/**
	 * Insert Template data information.
	 * 
	 * @param template
	 * @param processId
	 * @param workItemId
	 * @param folderItemNo
	 * @param documentNo
	 * @param layerNo
	 * @param maxVersion
	 * 
	 */
	public int insertTemplateData(TemplateData templateData, String processId, String workItemId,
			Integer folderItemNo, Integer documentNo, Integer layerNo, int maxVersion) throws EarthException {
		if (templateData == null) {
			return 0;
		}
		
//		insert(path)
		return 0;
	}
	
}
