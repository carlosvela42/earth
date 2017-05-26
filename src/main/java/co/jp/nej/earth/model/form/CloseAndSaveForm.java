package co.jp.nej.earth.model.form;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.Document;

public class CloseAndSaveForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,workitemId")
    private String workitemId;
    @Valid
    private List<Document> docImages;

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
     * @return the workitemId
     */
    public String getWorkitemId() {
        return workitemId;
    }

    /**
     * @param workitemId
     *            the workitemId to set
     */
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    /**
     * @return the docImages
     */
    public List<Document> getDocImages() {
        return docImages;
    }

    /**
     * @param docImages
     *            the docImages to set
     */
    public void setDocImages(List<Document> docImages) {
        this.docImages = docImages;
    }

}
