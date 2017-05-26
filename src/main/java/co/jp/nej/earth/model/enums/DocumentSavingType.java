package co.jp.nej.earth.model.enums;

public enum DocumentSavingType {
    FILE_UNTIL_FULL("fileUntilFull"), FILE_ROUND_ROBIN("fileRoundRobin"), DATABASE("database");

    DocumentSavingType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
