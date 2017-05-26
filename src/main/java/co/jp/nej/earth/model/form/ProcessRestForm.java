package co.jp.nej.earth.model.form;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.ProcessMap;

public class ProcessRestForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @Valid
    private ProcessMap datProcess;

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
     * @return the datProcess
     */
    public ProcessMap getDatProcess() {
        return datProcess;
    }

    /**
     * @param datProcess
     *            the datProcess to set
     */
    public void setDatProcess(ProcessMap datProcess) {
        this.datProcess = datProcess;
    }

}
