package co.jp.nej.earth.dao;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.UserAccessRight;

public interface MenuAuthorityDao {

    Map<String, MenuAccessRight> getMixAuthority(String userId) throws EarthException;

    boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;

    boolean insertMixAuthority(String menuId, List<UserAccessRight> userAccessRights) throws EarthException;

    boolean deleteAllMixAuthority(String menuId) throws EarthException;

    List<UserAccessRight> getUserAuthority(String menuId) throws EarthException;

    List<UserAccessRight> getUserAuthorityByProfiles(String menuId) throws EarthException;

}
