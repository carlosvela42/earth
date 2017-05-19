package co.jp.nej.earth.model.enums;

public enum OpenProcessMode {
    READ_ONLY(0), EDIT(1);

    private Integer mode;

    /**
     * @param mode
     */
    OpenProcessMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getMode() {
        return mode;
    }

    public static OpenProcessMode findOpenModeByMode(Integer mode) {
        for (OpenProcessMode openProcessMode : OpenProcessMode.values()) {
            if (mode.equals(openProcessMode.getMode())) {
                return openProcessMode;
            }
        }
        return null;
    }
}
