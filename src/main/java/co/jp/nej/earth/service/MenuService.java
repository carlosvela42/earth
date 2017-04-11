package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.entity.MgrMenu;

import java.util.List;

public interface MenuService {
    List<MenuAccessRight> getAccessRightByUser(String userId);

    public List<MgrMenu> getMenuByProfileId(List<String> profileIds) throws EarthException;
}