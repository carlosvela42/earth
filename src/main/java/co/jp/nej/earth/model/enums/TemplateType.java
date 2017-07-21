package co.jp.nej.earth.model.enums;

import java.util.ArrayList;
import java.util.List;

import co.jp.nej.earth.util.EStringUtil;

public enum TemplateType {
    PROCESS, WORKITEM, FOLDERITEM, DOCUMENT, LAYER;

    public static boolean isProcess(String type) {
        return TemplateType.PROCESS.toString().equals(type.toUpperCase());
    }

    public static boolean isWorkItem(String type) {
        return TemplateType.WORKITEM.toString().equals(type.toUpperCase());
    }

    public static boolean isFolderItem(String type) {
        return TemplateType.FOLDERITEM.toString().equals(type.toUpperCase());
    }

    public static boolean isDocument(String type) {
        return TemplateType.DOCUMENT.toString().equals(type.toUpperCase());
    }

    public static boolean isLayer(String type) {
        return TemplateType.LAYER.toString().equals(type.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toUpperCase();
    }

    public static String getTemplateTypeName(String type) {
        if (isProcess(type)) {
            return TemplateType.PROCESS.toString();
        }

        if (isFolderItem(type)) {
            return TemplateType.FOLDERITEM.toString();
        }

        if (isDocument(type)) {
            return TemplateType.DOCUMENT.toString();
        }

        if (isLayer(type)) {
            return TemplateType.LAYER.toString();
        }

        return TemplateType.WORKITEM.toString();
    }

    public static List<String> getTempateTypes() {
        List<String> templateTypes = new ArrayList<>();
        templateTypes.add(TemplateType.PROCESS.toString());
        templateTypes.add(TemplateType.WORKITEM.toString());
        templateTypes.add(TemplateType.FOLDERITEM.toString());
        templateTypes.add(TemplateType.DOCUMENT.toString());
        templateTypes.add(TemplateType.LAYER.toString());
        return templateTypes;
    }

    public boolean equal(TemplateType type) {
        return EStringUtil.equals(this.toString(), type.toString());
    }
}
