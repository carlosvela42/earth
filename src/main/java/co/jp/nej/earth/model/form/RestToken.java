package co.jp.nej.earth.model.form;

import org.hibernate.validator.constraints.NotEmpty;

public class RestToken {

    @NotEmpty(message = "E0002,sessionId")
    private String sessionId;

    /**
     * @return the sesssionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sesssionId
     *            the sesssionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
