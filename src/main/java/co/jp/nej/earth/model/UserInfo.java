package co.jp.nej.earth.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
private static final long serialVersionUID = 1L;
private String userId;
private String userName;
private String loginToken;

public String getUserId() {
   return userId;
}

public void setUserId(String userId) {
   this.userId = userId;
}

public String getUserName() {
   return userName;
}

public void setUserName(String userName) {
   this.userName = userName;
}

public String getLoginToken() {
  return loginToken;
}

public void setLoginToken(String loginToken) {
  this.loginToken = loginToken;
}
}
