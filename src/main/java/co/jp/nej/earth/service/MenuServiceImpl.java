package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenuP;

@Transactional
@Service
public class MenuServiceImpl implements MenuService {

    @Override
    public List<MgrMenu> getMenuByProfileId(List<String> profileIds) throws EarthException {
        try {
            QMgrMenu qMgrMenu = QMgrMenu.newInstance();
            QMgrMenuP qMgrMenuP = QMgrMenuP.newInstance();
            QBean<MgrMenu> selectList = Projections.bean(MgrMenu.class, qMgrMenu.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrMenu> mgrMenus = earthQueryFactory.select(selectList).from(qMgrMenu).innerJoin(qMgrMenuP).on
                    (qMgrMenu.functionId.eq(qMgrMenuP.functionId))
                    .where
                            (qMgrMenuP.profileId.in(profileIds))
                    .fetch();
            return mgrMenus;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    /**
     * Get list menu Access by MgrUser
     *
     * @param userId
     * @return
     */
    public List<MenuAccessRight> getAccessRightByUser(String userId) {
        List<MenuAccessRight> menuAccessRights = new ArrayList<MenuAccessRight>();
        /**
         * Test menu
         */
        MenuAccessRight menuAccessRightDefault = new MenuAccessRight();
        MgrMenu mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("1");
        mgrMenuDefault.setFunctionCategoryName("ユーザ");
        mgrMenuDefault.setFunctionInformation("user/showList");
        mgrMenuDefault.setFunctionId("ユーザ");
        mgrMenuDefault.setFunctionName("ユーザ");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("1");
        mgrMenuDefault.setFunctionCategoryName("ユーザ");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("プロファイル");
        mgrMenuDefault.setFunctionName("プロファイル");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);

        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("2");
        mgrMenuDefault.setFunctionCategoryName("レポート");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("ライセンス利用状況");
        mgrMenuDefault.setFunctionName("ライセンス利用状況");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("2");
        mgrMenuDefault.setFunctionCategoryName("レポート");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("証跡ログ");
        mgrMenuDefault.setFunctionName("証跡ログ");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("2");
        mgrMenuDefault.setFunctionCategoryName("レポート");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("ログイン状況");
        mgrMenuDefault.setFunctionName("ログイン状況");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);

        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("3");
        mgrMenuDefault.setFunctionCategoryName("権限");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("テンプレート");
        mgrMenuDefault.setFunctionName("テンプレート");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("3");
        mgrMenuDefault.setFunctionCategoryName("権限");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("メニュー");
        mgrMenuDefault.setFunctionName("メニュー");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);

        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("4");
        mgrMenuDefault.setFunctionCategoryName("プロセス");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("プロセス");
        mgrMenuDefault.setFunctionName("プロセス");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("4");
        mgrMenuDefault.setFunctionCategoryName("プロセス");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("サイト");
        mgrMenuDefault.setFunctionName("サイト");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("4");
        mgrMenuDefault.setFunctionCategoryName("プロセス");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("データディレクトリ");
        mgrMenuDefault.setFunctionName("データディレクトリ");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("4");
        mgrMenuDefault.setFunctionCategoryName("プロセス");
        mgrMenuDefault.setFunctionInformation("workspace/list");
        mgrMenuDefault.setFunctionId("ワークスペース");
        mgrMenuDefault.setFunctionName("ワークスペース");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("4");
        mgrMenuDefault.setFunctionCategoryName("プロセス");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("スケジュール");
        mgrMenuDefault.setFunctionName("スケジュール");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);

        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("5");
        mgrMenuDefault.setFunctionCategoryName("データモデル");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("プロセス");
        mgrMenuDefault.setFunctionName("プロセス");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("5");
        mgrMenuDefault.setFunctionCategoryName("データモデル");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("ワークアイテム");
        mgrMenuDefault.setFunctionName("ワークアイテム");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("5");
        mgrMenuDefault.setFunctionCategoryName("データモデル");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("フォルダ");
        mgrMenuDefault.setFunctionName("フォルダ");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("5");
        mgrMenuDefault.setFunctionCategoryName("データモデル");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("ドキュメント");
        mgrMenuDefault.setFunctionName("ドキュメント");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("5");
        mgrMenuDefault.setFunctionCategoryName("データモデル");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("レイヤー");
        mgrMenuDefault.setFunctionName("レイヤー");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);

        menuAccessRightDefault = new MenuAccessRight();
        mgrMenuDefault = new MgrMenu();
        mgrMenuDefault.setFunctionCategoryId("6");
        mgrMenuDefault.setFunctionCategoryName("ツール");
        mgrMenuDefault.setFunctionInformation("user");
        mgrMenuDefault.setFunctionId("ワークアイテム検索");
        mgrMenuDefault.setFunctionName("ワークアイテム検索");
        menuAccessRightDefault.setMgrMenu(mgrMenuDefault);
        menuAccessRights.add(menuAccessRightDefault);
        return menuAccessRights;
    }

}
