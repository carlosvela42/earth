package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;

import java.util.List;

public interface StrLogAccessDao extends BaseDao<StrLogAccess> {
    List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException;

    boolean deleteListByUserIds(List<String> userIds) throws EarthException;
    Integer getMaxHistoryNo(String workspaceId, String eventId) throws EarthException;
    String getMaxId(String workspaceId) throws EarthException;
}
