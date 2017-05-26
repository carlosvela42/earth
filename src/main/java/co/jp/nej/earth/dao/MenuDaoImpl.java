package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenuP;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuDaoImpl extends BaseDaoImpl<MgrMenu> implements MenuDao {

    private static final Logger LOG = LoggerFactory.getLogger(MenuDaoImpl.class);

    public MenuDaoImpl() throws Exception {
        super();
    }

    @Override
    public List<MgrMenu> getMenuByProfileId(List<String> profileIds) throws EarthException {
        try {
            QMgrMenu qMgrMenu = QMgrMenu.newInstance();
            QMgrMenuP qMgrMenuP = QMgrMenuP.newInstance();
            QBean<MgrMenu> selectList = Projections.bean(MgrMenu.class, qMgrMenu.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrMenu> mgrMenus = earthQueryFactory.select(selectList).from(qMgrMenu).innerJoin(qMgrMenuP).on(
                    qMgrMenu.functionId.eq(qMgrMenuP.functionId))
                    .where(qMgrMenuP.profileId.in(profileIds))
                    .orderBy(qMgrMenuP.functionId.asc(), qMgrMenuP.functionId.desc())
                    .fetch();
            return mgrMenus;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }


}
