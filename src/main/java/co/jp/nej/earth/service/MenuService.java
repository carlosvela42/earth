package co.jp.nej.earth.service;

import co.jp.nej.earth.model.MenuAccessRight;

import java.util.List;

public interface MenuService {
    List<MenuAccessRight> getAccessRightByUser(String userId);
}