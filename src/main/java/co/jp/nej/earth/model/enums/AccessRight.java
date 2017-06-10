package co.jp.nej.earth.model.enums;

/**
 *  Access Right
 *  0: None
 *  1: can hide masking
 *  2: only write
 *  3: only read
 *  4: write and read
 *  5: Full
 */
public enum AccessRight {

    NONE(0)
    ,SO(1)
    ,RO(2)
    ,RW(3)
    ,FULL(4);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    AccessRight(int value) {
        this.value = value;
    }

}
