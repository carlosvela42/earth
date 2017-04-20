package co.jp.nej.earth.model;

import java.io.Serializable;

public class TemplateKey implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String workspaceId;
    private String templateId;

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
