package co.jp.nej.earth.model.entity;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.util.DateUtil;

import java.io.Serializable;

public class MgrUserProfile extends BaseModel<MgrUserProfile> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String profileId;
    private String userId;

    public MgrUserProfile(){
        this.setqObj(QMgrUserProfile.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public MgrUserProfile(String userId,String profileId){
        this();
        this.userId=userId;
        this.profileId=profileId;
    }

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
}
