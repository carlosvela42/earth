package co.jp.nej.earth.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.util.ObjectUtils;

import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.MenuStructure;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.enums.AccessRight;

public class MenuUtil {

    public List<MenuStructure> buildMenuTree(Map<String, MenuAccessRight> menuAccessRightMap) {
        List<MenuStructure> menuStructures = new ArrayList<MenuStructure>();
        MenuStructure menuStructure;
        Map<String, String> mapStructure = new HashMap<String, String>();

        for (Map.Entry<String, MenuAccessRight> entry : menuAccessRightMap.entrySet()) {
            MenuAccessRight menuAccessRight = entry.getValue();
            MgrMenu mgrMenu = menuAccessRight.getMgrMenu();
            if (!mapStructure.containsKey(mgrMenu.getFunctionCategoryId())) {
                mapStructure.put(mgrMenu.getFunctionCategoryId(), mgrMenu.getFunctionCategoryName());
            }
        }
        Map<String, String> sortedMapStructure = new TreeMap<String, String>(mapStructure);
        Map<String, MenuAccessRight> sortedMenuAccessRightMap = new TreeMap<String, MenuAccessRight>(
                menuAccessRightMap);
        for (Map.Entry<String, String> map : sortedMapStructure.entrySet()) {
            List<MgrMenu> mgrMenus = new ArrayList<MgrMenu>();
            menuStructure = new MenuStructure();
            menuStructure.setMenuParentId(map.getKey());
            menuStructure.setMenuParentName(map.getValue());
            for (Map.Entry<String, MenuAccessRight> entry : sortedMenuAccessRightMap.entrySet()) {
                MenuAccessRight menuAccessRight = entry.getValue();
                MgrMenu mgrMenu = menuAccessRight.getMgrMenu();
                String categoryId = mgrMenu.getFunctionCategoryId().trim();
                String key = map.getKey().trim();
                if (!categoryId.equals("") && !key.equals("") && categoryId.equals(key)) {
                    mgrMenus.add(mgrMenu);
                }
            }
            menuStructure.setMgrMenus(mgrMenus);
            menuStructures.add(menuStructure);
        }
        return menuStructures;
    }

    public static void saveToSession(HttpSession session, Map<String, MenuAccessRight> menuAccess) {
        session.setAttribute(Session.MENU_ACCESS_RIGHT_MAP, menuAccess);
    }

    @SuppressWarnings("unchecked")
    public static List<MgrMenu> getListMenu(HttpSession session) {
        List<MgrMenu> listMenu = new ArrayList<MgrMenu>();
        if (session.getAttribute(Session.MENU_ACCESS_RIGHT_MAP) != null) {
            Map<String, MenuAccessRight> mapMenuAccessRight = (Map<String, MenuAccessRight>) session
                    .getAttribute(Session.MENU_ACCESS_RIGHT_MAP);
            for (String key : mapMenuAccessRight.keySet()) {
                MenuAccessRight menuAccessRight = mapMenuAccessRight.get(key);
                if (!ObjectUtils.isEmpty(menuAccessRight)) {
                    listMenu.add(menuAccessRight.getMgrMenu());
                }
            }
        }
        return listMenu;
    }

    @SuppressWarnings("unchecked")
    public static AccessRight getAuthority(HttpSession session, String menuId) {
        if (session.getAttribute(Session.MENU_ACCESS_RIGHT_MAP) != null) {
            Map<String, MenuAccessRight> mapMenuAccessRight = (Map<String, MenuAccessRight>) session
                    .getAttribute(Session.MENU_ACCESS_RIGHT_MAP);
            for (String key : mapMenuAccessRight.keySet()) {
                if (menuId.equals(key)) {
                    MenuAccessRight menuAccessRight = mapMenuAccessRight.get(key);
                    if (!ObjectUtils.isEmpty(menuAccessRight)) {
                        return menuAccessRight.getAccessRight();
                    }
                }
            }
        }
        return null;
    }

}
