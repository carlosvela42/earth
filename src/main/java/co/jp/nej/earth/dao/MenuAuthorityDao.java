package co.jp.nej.earth.dao;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.UserAccessRight;

public interface MenuAuthorityDao {

    public Map<String, MenuAccessRight> getMixAuthority(String userId) throws EarthException;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;

    public boolean insertMixAuthority(String menuId, List<UserAccessRight> userAccessRights) throws EarthException;

    public boolean deleteAllMixAuthority(String menuId) throws EarthException;

    public List<UserAccessRight> getUserAuthority(String menuId) throws EarthException;

    public List<UserAccessRight> getUserAuthorityByProfiles(String menuId) throws EarthException;

}
