package co.jp.nej.earth.model;

import co.jp.nej.earth.dto.DbConnection;

public class MgrWorkspaceConnect extends DbConnection{
    private String workspaceId;
    private String lastUpdateTime;
    
    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }
    /**
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
