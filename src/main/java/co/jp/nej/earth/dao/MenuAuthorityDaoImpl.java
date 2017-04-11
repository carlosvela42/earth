package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.MenuLink;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.entity.MgrMenuU;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.*;
import co.jp.nej.earth.util.CommonUtil;
import co.jp.nej.earth.util.DateUtil;
import com.google.gson.Gson;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.dml.SQLInsertClause;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author p-tvo-thuynd
 */

@Repository
public class MenuAuthorityDaoImpl implements MenuAuthorityDao {

    public Map<String, MenuAccessRight> getMixAuthority(String userId) throws EarthException {
        Map<String, MenuAccessRight> menuAccessRightMap = new HashMap<String, MenuAccessRight>();
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            QMgrMenu qMgrMenu = QMgrMenu.newInstance();
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
                if (!StringUtils.isEmpty(functionInformation)) {
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

    @Override
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            QMgrMenuU qMgrMenuU = QMgrMenuU.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qCtlMenu).where(qCtlMenu.userId.in(userIds)).execute();
            earthQueryFactory.delete(qMgrMenuU).where(qMgrMenuU.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            QMgrMenuP qMgrMenuP = QMgrMenuP.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long deleted = earthQueryFactory.delete(qMgrMenuP).where(qMgrMenuP.profileId.in(profileIds)).execute();
            return deleted > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean insertMixAuthority(String menuId, List<UserAccessRight> userAccessRights) throws EarthException {
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            SQLInsertClause insert = earthQueryFactory.insert(qCtlMenu);
            for (UserAccessRight userAccessRight : userAccessRights) {
                insert.set(qCtlMenu.functionId, menuId).set(qCtlMenu.userId, userAccessRight.getUserId()).set
                        (qCtlMenu.accessAuthority, userAccessRight.getAccessRight().getValue()).set(qCtlMenu
                        .lastUpdateTime, DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD)).addBatch();
            }
            long inserted = insert.execute();
            return inserted > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteAllMixAuthority(String menuId) throws EarthException {
        try {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long delete = earthQueryFactory.delete(qCtlMenu).where(qCtlMenu.functionId.eq(menuId)).execute();
            return delete > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<UserAccessRight> getUserAuthority(String menuId) throws EarthException {
        try {
            QMgrMenuU qMgrMenuU = QMgrMenuU.newInstance();
            QBean<MgrMenuU> selectList = Projections.bean(MgrMenuU.class, qMgrMenuU.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrMenuU> mgrMenuUs = earthQueryFactory.select(selectList).from(qMgrMenuU).where(qMgrMenuU.functionId.eq(menuId))
                    .fetch();
            List<UserAccessRight> userAccessRights = new ArrayList<UserAccessRight>();
            for (MgrMenuU mgrMenuU : mgrMenuUs) {
                UserAccessRight userAccessRight = new UserAccessRight();
                userAccessRight.setUserId(mgrMenuU.getUserId());
                userAccessRight.setAccessRight(AccessRight.values()[mgrMenuU.getAccessAuthority()]);
                userAccessRights.add(userAccessRight);
            }
            return userAccessRights;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<UserAccessRight> getUserAuthorityByProfiles(String menuId) throws EarthException {
        try {
            QMgrMenuP qMgrMenuP = QMgrMenuP.newInstance();
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            ResultSet resultSet = earthQueryFactory.select(qMgrMenuP.functionId, qMgrUserProfile
                    .userId, qMgrMenuP.accessAuthority, qMgrMenuP.lastUpdateTime).from(qMgrMenuP)
                    .innerJoin
                            (qMgrUserProfile).on
                            (qMgrMenuP.profileId.eq(qMgrUserProfile.profileId)).where
                            (qMgrMenuP.functionId.eq(menuId))
                    .getResults();
            List<UserAccessRight> userAccessRights = CommonUtil.getUserAccessRightsFromResult(resultSet);
            return userAccessRights;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EarthException(ex.getMessage());
        }
    }

}
