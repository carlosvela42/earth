package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrTemplate;

public interface TemplateService {
    public Map<String,Object> getTemplateListInfo(String workspaceId, List<String> templateIds) throws EarthException;
    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException;
}