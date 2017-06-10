package co.jp.nej.earth.web.form;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.entity.MgrTemplate;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class TemplateForm extends BaseModel<MgrTemplate> {

  @NotEmpty(message = "E0001,templateId")
  private String templateId;

  @NotEmpty(message = "E0001,templateName")
  private String templateName;

  public String getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(String workspaceId) {
    this.workspaceId = workspaceId;
  }

  @NotEmpty(message = "E0001,templateTableName")
  private String templateTableName;

  @NotEmpty(message = "E0001,templateFields")
  private List<Field> templateFields;

  private String workspaceId;

  public List<Field> getTemplateFields() {
    return templateFields;
  }

  public void setTemplateFields(List<Field> templateFields) {
    this.templateFields = templateFields;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateTableName() {
    return templateTableName;
  }

  public void setTemplateTableName(String templateTableName) {
    this.templateTableName = templateTableName;
  }

}
