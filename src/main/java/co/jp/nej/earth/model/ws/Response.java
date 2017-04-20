package co.jp.nej.earth.model.ws;

public class Response {
    private boolean result;
    private String message;

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }
    /**
     * @param result the result to set
     */
    public void setResult(boolean result) {
        this.result = result;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
