package co.jp.nej.earth.model;

import co.jp.nej.earth.model.enums.AccessRight;

public class UserAccessRight {
    private String userId;
    private AccessRight accessRight;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccessRight getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(AccessRight accessRight) {
        this.accessRight = accessRight;
    }
}
