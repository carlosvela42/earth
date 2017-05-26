package co.jp.nej.earth.web.form;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.Field;

public class TemplateForm {

    @NotEmpty(message = "E0001,templateId")
    private String templateId;

    @NotEmpty(message = "E0001,templateName")
    private String templateName;

    @NotEmpty(message = "E0001,templateTableName")
    private String templateTableName;

    @NotEmpty(message = "E0001,templateFields")
    private List<Field> templateFields;

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
