package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.ProcessMap;

public class ProcessRestForm {
    private String workspaceId;
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
