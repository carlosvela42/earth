package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface ETemplateTableName {
    String getTemplateTableName(String templateType, String templateId) throws EarthException;
}
