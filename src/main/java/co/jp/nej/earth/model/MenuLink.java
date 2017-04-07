package co.jp.nej.earth.model;

import com.google.gson.annotations.SerializedName;

import co.jp.nej.earth.model.enums.LinkType;

public class MenuLink {
    @SerializedName("linkType")
    private LinkType linkType;
    private String url;
    
    public LinkType getLinkType() {
        return linkType;
    }
    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
