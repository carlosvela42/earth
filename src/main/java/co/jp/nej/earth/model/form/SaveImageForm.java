package co.jp.nej.earth.model.form;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.Document;

public class SaveImageForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @Valid
    private Document document;

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId
     *            the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

}
