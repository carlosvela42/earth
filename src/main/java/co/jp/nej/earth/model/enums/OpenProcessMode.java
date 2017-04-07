package co.jp.nej.earth.model.enums;

public enum OpenProcessMode {
    READ_ONLY(0)
    ,EDIT(1);
    
    private Integer mode;

    /**
     * @param mode
     */
    private OpenProcessMode(Integer mode) {
        this.mode = mode;
    }
    
    public Integer getMode() {
        return mode;
    }
}
