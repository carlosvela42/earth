package co.jp.nej.earth.model.ws;

public class RestResponse {
    private boolean result = true;
    private Object data;

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
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

}
