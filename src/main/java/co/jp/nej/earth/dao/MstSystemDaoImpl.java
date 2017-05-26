package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MstSystem;
import co.jp.nej.earth.model.sql.QMstSystem;

@Repository
public class MstSystemDaoImpl implements MstSystemDao {

    public List<MstSystem> getMstSystem() throws EarthException {
        QMstSystem qMstSystem = QMstSystem.newInstance();

        QBean<MstSystem> selectList = Projections.bean(MstSystem.class, qMstSystem.all());

        List<MstSystem> listMstSystem = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                .select(selectList).from(qMstSystem).fetch();
        return listMstSystem;
    }

    @Override
    public List<MstSystem> getMstSystemBySection(String section) throws EarthException {
        try {
            QMstSystem qMstSystem = QMstSystem.newInstance();

            QBean<MstSystem> selectList = Projections.bean(MstSystem.class, qMstSystem.all());

            List<MstSystem> listMstSystem = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                    .select(selectList).from(qMstSystem).where(qMstSystem.section.eq(section)).fetch();
            return listMstSystem;
        } catch (Exception ex) {
            throw new EarthException(ex);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MstSystem getMstSystemBySectionAndVariable(String section, String variable) throws EarthException {
        QMstSystem qMstSystem = QMstSystem.newInstance();
        QBean<MstSystem> selectList = Projections.bean(MstSystem.class, qMstSystem.all());
        // get system config value
        return ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList).from(qMstSystem)
                .where(qMstSystem.section.eq(section).and(qMstSystem.variableName.eq(variable))).fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateMstSystem(String section, String variable, String configValue) throws EarthException {
        QMstSystem qMstSystem = QMstSystem.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
        // update system date
        try {
            return (int) earthQueryFactory.update(qMstSystem).set(qMstSystem.configValue, configValue)
                    .where(qMstSystem.section.eq(section).and(qMstSystem.variableName.eq(variable))).execute();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }
}
