package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class MgrUserProfile implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String profileId;
    private String userId;
    private String lastUpdateTime;
    
    public String getProfileId() {
        return profileId;
    }
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
