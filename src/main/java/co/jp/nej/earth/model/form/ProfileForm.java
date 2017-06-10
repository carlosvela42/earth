package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProfile;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ProfileForm extends BaseModel<MgrProfile> {

    @NotEmpty(message = "E0001,ProfileId")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,UserId")
    private String profileId;

    private String availableLicenceCount;

    @NotEmpty(message = "E0001,Description")
    private String description;

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
