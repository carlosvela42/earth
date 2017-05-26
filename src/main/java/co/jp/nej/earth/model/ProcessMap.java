package co.jp.nej.earth.model;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.entity.MgrTemplate;

public class ProcessMap {
    @NotEmpty(message = "E0002,processId")
    private Integer processId;
    @NotEmpty(message = "E0002,workItemId")
    private String workItemId;
    private MgrTemplate mgrTemplate;
    private TemplateData processData;
    private Float processVersion;
    private String lastUpdateTime;
    private String templateId;

    /**
     * @return the processId
     */
    public Integer getProcessId() {
        return processId;
    }

    /**
     * @param processId
     *            the processId to set
     */
    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return workItemId;
    }

    /**
     * @param workItemId
     *            the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }

    public MgrTemplate getMgrTemplate() {
        return mgrTemplate;
    }

    public void setMgrTemplate(MgrTemplate mgrTemplate) {
        this.mgrTemplate = mgrTemplate;
    }

    /**
     * @return the processData
     */
    public TemplateData getProcessData() {
        return processData;
    }

    /**
     * @param processData
     *            the processData to set
     */
    public void setProcessData(TemplateData processData) {
        this.processData = processData;
    }

    /**
     * @return the processVersion
     */
    public Float getProcessVersion() {
        return processVersion;
    }

    /**
     * @param processVersion
     *            the processVersion to set
     */
    public void setProcessVersion(Float processVersion) {
        this.processVersion = processVersion;
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

}
