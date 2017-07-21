package co.jp.nej.earth.model.ws;

import org.hibernate.validator.constraints.NotEmpty;

public class Request {

    @NotEmpty(message = "E0001,sessionId")
    private String sessionId;

    /**
     * @return the sesssionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sesssionId the sesssionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
