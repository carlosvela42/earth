package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class MgrProfile implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String profileId;
    private int availableLicenceCount;
    private String description;
    private String ldapIdentifier;
    private String lastUpdateTime;


    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getAvailableLicenceCount() {
        return availableLicenceCount;
    }

    public void setAvailableLicenceCount(int availableLicenceCount) {
        this.availableLicenceCount = availableLicenceCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLdapIdentifier() {
        return ldapIdentifier;
    }

    public void setLdapIdentifier(String ldapIdentifier) {
        this.ldapIdentifier = ldapIdentifier;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
