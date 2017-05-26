package co.jp.nej.earth.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Type {
    NUMBER(1), NVARCHAR2(2), LONG(3), NCHAR(4);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    Type(int value) {
        this.value = value;
    }

    public static List<Type> getFieldTypes() {
        List<Type> templateTypes = new ArrayList<>();
        templateTypes.add(Type.NUMBER);
        templateTypes.add(Type.NVARCHAR2);
        templateTypes.add(Type.LONG);
        templateTypes.add(Type.NCHAR);
        return templateTypes;
    }

    @Override
    public String toString() {
        return super.toString().toUpperCase();
    }
}
