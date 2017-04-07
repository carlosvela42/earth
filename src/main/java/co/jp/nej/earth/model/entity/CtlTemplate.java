package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class CtlTemplate implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String userId;
    private String templateId;
    private int accessAuthority;
    private String lastUpdateTime;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public int getAccessAuthority() {
        return accessAuthority;
    }
    public void setAccessAuthority(int accessAuthority) {
        this.accessAuthority = accessAuthority;
    }
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
