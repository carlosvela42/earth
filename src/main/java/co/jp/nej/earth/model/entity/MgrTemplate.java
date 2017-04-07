/**
 * 
 */
package co.jp.nej.earth.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.util.EStringUtil;

/**
 * @author p-tvo-khanhnv
 *
 */
public class MgrTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String workspaceId;
	private String templateId;
	private String templateName;
	private String templateTableName;
	private String templateType;
	private String lastUpdateTime;
	private String templateField;
	
	private List<Field> templateFields = new ArrayList<Field>();

	/**
	 * 
	 */
	public MgrTemplate() {
		super();
	}

	/**
	 * @return the workspaceId
	 */
	public String getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @param workspaceId
	 *            the workspaceId to set
	 */
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName
	 *            the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateTableName
	 */
	public String getTemplateTableName() {
		return templateTableName;
	}

	/**
	 * @param templateTableName
	 *            the templateTableName to set
	 */
	public void setTemplateTableName(String templateTableName) {
		this.templateTableName = templateTableName;
	}

	/**
	 * @return the templateFields
	 */
	public List<Field> getTemplateFields() {
		return templateFields;
	}

	/**
	 * @param templateFields
	 *            the templateFields to set
	 */
	public void setTemplateFields(List<Field> templateFields) {
		this.templateFields = templateFields;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType
	 *            the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void addField(Field field) {
		if (field != null && !templateFields.contains(field)) {
			templateFields.add(field);
		}
	}

	/**
	 * @return the templateField
	 */
	public String getTemplateField() {
		return templateField;
	}

	/**
	 * @param templateField the templateField to set
	 */
	public void setTemplateField(String templateField) {
		this.templateField = templateField;
	}
	
	public void addTemplateFields(String templateField) {
		if (!EStringUtil.isEmpty(templateField)) {
			List<Field> fields = new Gson().fromJson(templateField, new TypeToken<List<Field>>(){}.getType());
			setTemplateFields(fields);
		}
	}
}
