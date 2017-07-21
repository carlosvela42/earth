package co.jp.nej.earth.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Form handle request param of Process
 *
 * @author DaoPQ
 * @version 1.0
 */
public class LayerTemplateForm extends DocumentTemplateForm {
    @NotEmpty(message = "E0001,layerNo")
    private String layerNo;
    @NotEmpty(message = "E0001,ownerId")
    private String ownerId;

    public String getLayerNo() {
        return layerNo;
    }

    public void setLayerNo(String layerNo) {
        this.layerNo = layerNo;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}