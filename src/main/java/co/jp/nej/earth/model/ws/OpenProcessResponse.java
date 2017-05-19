package co.jp.nej.earth.model.ws;

public class OpenProcessResponse {
    private boolean result = true;
    private Object value;

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
