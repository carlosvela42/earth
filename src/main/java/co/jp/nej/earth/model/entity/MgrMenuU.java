package co.jp.nej.earth.model.entity;

import java.io.Serializable;

/**
 * Created by minhtv on 3/29/2017.
 */
public class MgrMenuU implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String functionId;
    private String userId;
    private int accessAuthority;
    private String lastUpdateTime;

    public String getFunctionId() {
        return functionId;
    }
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
