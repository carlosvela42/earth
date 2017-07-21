package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;
import org.springframework.stereotype.Service;

@Service
public class ETemplateTableNameImpl implements ETemplateTableName {

    @Override
    public String getTemplateTableName(String templateType, String templateId) throws EarthException {
        return "TMP_" + templateType.substring(0, 1) + templateId;
    }

}
