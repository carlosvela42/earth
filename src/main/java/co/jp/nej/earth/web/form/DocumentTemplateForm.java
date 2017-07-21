package co.jp.nej.earth.web.form;

/**
 * Form handle request param of Process
 *
 * @author DaoPQ
 * @version 1.0
 */
public class DocumentTemplateForm extends FolderItemTemplateForm {
    private String documentNo;

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }
}