package co.jp.nej.earth.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.util.DateUtil;

public class CtlLogin extends BaseModel<CtlLogin> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CtlLogin.class);
    
    /**
     * Serial number 
     */
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String userId;
    private String loginTime;
    private String logoutTime;

    private Long testLong;
    
    private Date testDate;

    public CtlLogin() {

        LOG.debug("Call to blank constructor");
        this.setqObj(QCtlLogin.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public CtlLogin(String sessionId, String userId, String loginTime, String logoutTime) {

        this();

        LOG.debug("Call to (sessionId, userId, loginTime, logoutTime) constructor");

        this.sessionId = sessionId;
        this.userId = userId;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }
}