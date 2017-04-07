package co.jp.nej.earth.dao;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;

public interface TemplateAuthorityDao {

    public Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId) throws EarthException;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;
}