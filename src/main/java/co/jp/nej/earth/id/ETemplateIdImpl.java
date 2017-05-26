package co.jp.nej.earth.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.service.TemplateService;

@Service
public class ETemplateIdImpl implements ETemplateId {

  @Autowired
  private TemplateService templateService;

  @Override
  public String getAutoId(String workspaceId) throws EarthException {
    List<String> stringTemplateIds = new ArrayList<>();
    List<Integer> intTemplateIds = new ArrayList<>();
    List<MgrTemplate> mgrTemplates = templateService.getAllByWorkspace(workspaceId);
    if (mgrTemplates.size() > 0) {
      for (MgrTemplate temp : mgrTemplates) {
        stringTemplateIds.add(temp.getTemplateId());
      }
      for (String stringTemplateId : stringTemplateIds) {
        intTemplateIds.add(Integer.valueOf(stringTemplateId));
      }
      return String.valueOf(Collections.max(intTemplateIds) + 1);

    } else {
      return String.valueOf(1);
    }
  }

}
