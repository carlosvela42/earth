package co.jp.nej.earth.model;

import co.jp.nej.earth.model.sql.QDatProcess;

public class DatProcess extends BaseModel<DatProcess> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer processId;
    private String workitemId;
    private String templateId;
    private TemplateData processData;

    public DatProcess() {
        super();
        this.setqObj(QDatProcess.newInstance());
    }

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
     * @return the workitemId
     */
    public String getWorkitemId() {
        return workitemId;
    }

    /**
     * @param workitemId
     *            the workitemId to set
     */
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
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
     * @return the processData
     */
    public TemplateData getProcessData() {
        return processData;
    }

    /**
     * @param processData the processData to set
     */
    public void setProcessData(TemplateData processData) {
        this.processData = processData;
    }

}
