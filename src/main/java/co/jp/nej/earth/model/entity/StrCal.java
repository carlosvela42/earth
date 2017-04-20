package co.jp.nej.earth.model.entity;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QStrCal;
import co.jp.nej.earth.util.DateUtil;

public class StrCal extends BaseModel<StrCal> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CtlLogin.class);
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;
    private String division;
    private String processTime;
    private String profileId;
    private int availableLicenseCount;
    private int useLicenseCount;

    public StrCal() {
        LOG.debug("Call to blank constructor");
        this.setqObj(QStrCal.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public StrCal(String division, String processTime, String profileId, int availableLicenseCount,
            int useLicenseCount) {
        this();
        LOG.debug("Call to (division, processTime, profileId, availableLicenseCount,"
                    +"useLicenseCount) constructor");
        this.division = division;
        this.processTime = processTime;
        this.profileId = profileId;
        this.availableLicenseCount = availableLicenseCount;
        this.useLicenseCount = useLicenseCount;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

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
