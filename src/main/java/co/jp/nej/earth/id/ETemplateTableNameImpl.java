package co.jp.nej.earth.id;

import org.springframework.stereotype.Service;

import co.jp.nej.earth.exception.EarthException;

@Service
public class ETemplateTableNameImpl implements ETemplateTableName {

  @Override
  public String getTemplateTableName(String templateType, String templateId) throws EarthException {
    return "TMP_" + templateType.substring(0, 1) + templateId;
  }

}
