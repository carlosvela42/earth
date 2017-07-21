package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.constant.Constant;
import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.entity.MgrProfile;

public class ProfileForm extends BaseModel<MgrProfile> {

    final String value = Constant.ErrorCode.E0001 + "," + Constant.ScreenItem.PROFILE_ID;
    @NotEmpty(message =value)
    private String profileId;

    @NotEmpty(message = "E0001,Description")
    private String description;

    private String availableLicenceCount;
    private String ldapIdentifier;
    private String userIds;

    public ProfileForm() {

    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAvailableLicenceCount() {
        return availableLicenceCount;
    }

    public void setAvailableLicenceCount(String availableLicenceCount) {
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

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
