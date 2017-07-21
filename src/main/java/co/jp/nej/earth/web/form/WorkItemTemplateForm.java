package co.jp.nej.earth.web.form;

import co.jp.nej.earth.exception.EarthException;

/**
 * Form handle request param of Process
 *
 * @author DaoPQ
 * @version 1.0
 */
public class WorkItemTemplateForm extends BaseEditWorkItemTemplateForm {
    private String task;
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public void validateForm() throws EarthException {
        super.validateForm();
        validateEmpty(task, "Invalid parameter task = null");
    }
}