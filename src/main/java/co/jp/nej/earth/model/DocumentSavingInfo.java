package co.jp.nej.earth.model;

import java.util.List;

import co.jp.nej.earth.model.enums.DocumentSavingType;

public class DocumentSavingInfo {

    private DocumentSavingType savingType;

    private List<Directory> dataDef;

    /**
     * @return the savingType
     */
    public DocumentSavingType getSavingType() {
        return savingType;
    }

    /**
     * @param savingType
     *            the savingType to set
     */
    public void setSavingType(DocumentSavingType savingType) {
        this.savingType = savingType;
    }

    /**
     * @return the dataDef
     */
    public List<Directory> getDataDef() {
        return dataDef;
    }

    /**
     * @param dataDef
     *            the dataDef to set
     */
    public void setDataDef(List<Directory> dataDef) {
        this.dataDef = dataDef;
    }

}
