package co.jp.nej.earth.model;

import co.jp.nej.earth.model.entity.MgrMenu;

import java.util.List;

/**
 * Created Stucture MgrMenu
 *      MgrMenu Parent
 *          List MgrMenu
 */
public class MenuStructure {
    private String menuParentId;
    private String menuParentName;
    private List<MgrMenu> mgrMenus;

    public String getMenuParentId() {
        return menuParentId;
    }

    public void setMenuParentId(String menuParentId) {
        this.menuParentId = menuParentId;
    }

    public String getMenuParentName() {
        return menuParentName;
    }

    public void setMenuParentName(String menuParentName) {
        this.menuParentName = menuParentName;
    }

    public List<MgrMenu> getMgrMenus() {
        return mgrMenus;
    }

    public void setMgrMenus(List<MgrMenu> mgrMenus) {
        this.mgrMenus = mgrMenus;
    }
}
