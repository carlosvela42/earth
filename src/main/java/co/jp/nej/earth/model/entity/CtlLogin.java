package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class CtlLogin implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String sessionId;
    private String userId;
    private String loginTime;
    private String logoutTime;
    private String lastUpdateTime;
    
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
    public String getLogoutTime() {
        return logoutTime;
    }
    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
