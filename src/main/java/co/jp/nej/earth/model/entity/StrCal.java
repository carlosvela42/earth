package co.jp.nej.earth.model.entity;

import java.io.Serializable;

public class StrCal implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String processTime;
    private String profileId;
    private int availableLicenseCount;
    private int useLicenseCount;

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getAvailableLicenseCount() {
        return availableLicenseCount;
    }

    public void setAvailableLicenseCount(int availableLicenseCount) {
        this.availableLicenseCount = availableLicenseCount;
    }

    public int getUseLicenseCount() {
        return useLicenseCount;
    }

    public void setUseLicenseCount(int useLicenseCount) {
        this.useLicenseCount = useLicenseCount;
    }
}
