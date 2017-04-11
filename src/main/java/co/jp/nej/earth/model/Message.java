package co.jp.nej.earth.model;

import java.io.Serializable;

public class Message implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String content;

    public Message(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
