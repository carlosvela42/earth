package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenu;
import co.jp.nej.earth.model.sql.QMgrMenuP;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class MenuServiceImpl implements MenuService {

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
                    .fetch();
            return mgrMenus;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
