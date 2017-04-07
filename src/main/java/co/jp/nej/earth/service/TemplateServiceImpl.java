package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrTemplate;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService{
    
    @Autowired
    TemplateDao templateDao;

    public Map<String, Object> getTemplateListInfo(String workspaceId, List<String> templateIds) throws EarthException {
        return null;
    }

    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
        return templateDao.getAllByWorkspace(workspaceId);
    }
}
