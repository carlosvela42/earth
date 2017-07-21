package co.jp.nej.earth.model.ws;

import java.util.List;

import co.jp.nej.earth.model.Message;

public class LoginResponse extends Response {
    private String sessionId;

    public LoginResponse(boolean result, List<Message> messages) {
        super(result, messages);
    }

    public LoginResponse(boolean result, String sessionId) {
        super(result);
        this.sessionId = sessionId;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
