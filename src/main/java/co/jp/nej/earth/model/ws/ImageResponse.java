package co.jp.nej.earth.model.ws;

import java.util.List;

import co.jp.nej.earth.model.Document;

public class ImageResponse extends Response {
    private String jsessionId;
    private List<Document> result;

    public List<Document> getResult() {
        return result;
    }

    public void setResult(List<Document> result) {
        this.result = result;
    }

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
