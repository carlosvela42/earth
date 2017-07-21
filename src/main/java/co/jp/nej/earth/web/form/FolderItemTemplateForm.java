package co.jp.nej.earth.web.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Form handle request param of Process
 *
 * @author DaoPQ
 * @version 1.0
 */
public class FolderItemTemplateForm extends BaseEditWorkItemTemplateForm {
    @NotEmpty(message = "E0001,folderItemNo")
    @Size(message = "E0026,FolderItemNo,20")
    private String folderItemNo;

    public String getFolderItemNo() {
        return folderItemNo;
    }

    public void setFolderItemNo(String folderItemNo) {
        this.folderItemNo = folderItemNo;
    }

}