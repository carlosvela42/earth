package co.jp.nej.earth.model;

import co.jp.nej.earth.model.enums.AccessRight;

public class TemplateAccessRight {
    
    private TemplateKey templateKey;
    private AccessRight accessRight;
    
    public TemplateKey getTemplateKey() {
        return templateKey;
    }
    public void setTemplateKey(TemplateKey templateKey) {
        this.templateKey = templateKey;
    }
    public AccessRight getAccessRight() {
        return accessRight;
    }
    public void setAccessRight(AccessRight accessRight) {
        this.accessRight = accessRight;
    }
}
