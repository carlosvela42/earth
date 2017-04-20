package co.jp.nej.earth.model;

public class MgrWorkspace {
    private String workspaceId;
    private String workspaceName;
    private String lastUpdateTime;
    private MgrWorkspaceConnect mgrWorkspaceConnect;

    public MgrWorkspaceConnect getMgrWorkspaceConnect() {
        return mgrWorkspaceConnect;
    }

    public void setMgrWorkspaceConnect(MgrWorkspaceConnect mgrWorkspaceConnect) {
        this.mgrWorkspaceConnect = mgrWorkspaceConnect;
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
     * @return the workspaceName
     */
    public String getWorkspaceName() {
        return workspaceName;
    }

    /**
     * @param workspaceName
     *            the workspaceName to set
     */
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
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
}
