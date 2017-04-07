package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.sql.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.MenuLink;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;

/**
 * 
 * @author p-tvo-thuynd
 *
 */

@Repository
public class MenuAuthorityDaoImpl implements MenuAuthorityDao {

    public Map<String, MenuAccessRight> getMixAuthority(String userId) throws EarthException {
        
        Map<String, MenuAccessRight> menuAccessRightMap = new HashMap<String, MenuAccessRight>();
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            QMenu qMgrMenu = QMenu.newInstance();
            QMgrMenuCategory qMgrMenuCategory = QMgrMenuCategory.newInstance();
            
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                    .select(qMgrMenu.functionId, qMgrMenu.functionName, qMgrMenu.functionCategoryId,
                            qMgrMenu.functionCategoryName, qMgrMenu.functionInformation, qCtlMenu.accessAuthority)
                    .from(qMgrMenu).join(qMgrMenuCategory)
                    .on(qMgrMenu.functionCategoryId.eq(qMgrMenuCategory.functionCategoryId)).join(qCtlMenu)
                    .on(qMgrMenu.functionId.eq(qCtlMenu.functionId)).where(qCtlMenu.userId.eq(userId))
                    .orderBy(qMgrMenuCategory.functionCategoryId.asc(), qMgrMenu.functionCategoryId.asc()).getResults();
            MgrMenu mgrMenu = null;
            MenuAccessRight menuAccessRight = null;
            
            while (resultSet.next()) {
                mgrMenu = new MgrMenu();
                
                String functionId = resultSet.getString(ColumnNames.FUNCTION_ID.toString());
                mgrMenu.setFunctionId(functionId);
                mgrMenu.setFunctionName(resultSet.getString(ColumnNames.FUNCTION_NAME.toString()));
                mgrMenu.setFunctionCategoryId(resultSet.getString(ColumnNames.FUNCTION_CATEGORY_ID.toString()));
                mgrMenu.setFunctionCategoryName(resultSet.getString(ColumnNames.FUNCTION_CATEGORY_NAME.toString()));
                String functionInformation = resultSet.getString(ColumnNames.FUNCTION_INFORMATION.toString());
                if(!StringUtils.isEmpty(functionInformation)){
                    mgrMenu.setFunctionInformation(functionInformation);
                    Gson gson = new Gson();
                    MenuLink menuLink = gson.fromJson(functionInformation, MenuLink.class);
                    mgrMenu.setMenuInformation(menuLink);
                }
                menuAccessRight = new MenuAccessRight();
                menuAccessRight.setMgrMenu(mgrMenu);
                menuAccessRight.setAccessRight(AccessRight.values()[resultSet.getInt(ColumnNames.ACCESS_AUTHORITY.toString())]);
                
                menuAccessRightMap.put(functionId, menuAccessRight);
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return menuAccessRightMap;
    }

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            QMgrMenuU qMgrMenuU=QMgrMenuU.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qCtlMenu).where(qCtlMenu.userId.in(userIds)).execute();
            earthQueryFactory.delete(qMgrMenuU).where(qMgrMenuU.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean insertMixAuthority(String menuId, List<MenuAccessRight> menuAccessRights) throws EarthException {
        return false;
    }

    @Override
    public boolean deleteAllMixAuthority(String menuId) throws EarthException {
        return false;
    }
}
