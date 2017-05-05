package co.jp.nej.earth.model.ws;

public class ImageResponse extends Response {
    private String jsessionId;

    /**
     * @return the jsessionId
     */
    public String getJsessionId() {
        return jsessionId;
    }

    /**
     * @param jsessionId the jsessionId to set
     */
    public void setJsessionId(String jsessionId) {
        this.jsessionId = jsessionId;
    }
}
