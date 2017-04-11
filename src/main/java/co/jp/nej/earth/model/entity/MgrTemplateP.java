package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class MgrTemplateP implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String profileId;
    private String templateId;
    private int accessAuthority;
    private String lastUpdateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
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
