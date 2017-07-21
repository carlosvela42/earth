package co.jp.nej.earth.web.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;

/**
 * Form handle request param of Process
 *
 * @author DaoPQ
 * @version 1.0
 */
public class ProcessTemplateForm extends BaseEditWorkItemTemplateForm {

    @NotEmpty(message = "E0001,processId")
    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = StringUtils.trimWhitespace(processId);
    }
}