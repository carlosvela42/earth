package co.jp.nej.earth.dao;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MenuAccessRight;

public interface MenuAuthorityDao {

    public Map<String, MenuAccessRight> getMixAuthority(String userId) throws EarthException;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    public boolean insertMixAuthority(String menuId,List<MenuAccessRight> menuAccessRights) throws EarthException;

    public boolean deleteAllMixAuthority(String menuId) throws EarthException;

}
