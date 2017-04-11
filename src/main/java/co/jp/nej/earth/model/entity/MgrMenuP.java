package co.jp.nej.earth.model.entity;

import java.io.Serializable;

/**
 * Created by minhtv on 4/3/2017.
 */
public class MgrMenuP implements Serializable {

    private static final long serialVersionUID = 1L;
    private String functionId;
    private String profileId;
    private int accessAuthority;
    private String lastUpdateTime;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
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
