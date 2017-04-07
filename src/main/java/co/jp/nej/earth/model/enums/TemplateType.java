package co.jp.nej.earth.model.enums;

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
}
