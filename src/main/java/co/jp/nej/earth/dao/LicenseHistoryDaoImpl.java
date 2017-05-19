package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.sql.QStrCal;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;

@Repository
public class LicenseHistoryDaoImpl extends BaseDaoImpl<StrCal> implements LicenseHistoryDao {

    public LicenseHistoryDaoImpl() throws Exception {
        super();
    }

    public List<StrCal> search(String fromTime, String toTime) throws EarthException {
        QStrCal qStrCal = QStrCal.newInstance();
        QBean<StrCal> selectList = Projections.bean(StrCal.class, qStrCal.all());
        EarthQueryFactory query = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
        return ConversionUtil.castList(executeWithException(() -> {
            List<StrCal> strCals = null;
            if (EStringUtil.isEmpty(fromTime) && EStringUtil.isEmpty(toTime)) {
                strCals = query.select(selectList).from(qStrCal)
                        .orderBy(qStrCal.processTime.asc(), qStrCal.profileId.asc()).fetch();
            } else if (!EStringUtil.isEmpty(fromTime) && EStringUtil.isEmpty(toTime)) {
                strCals = query.select(selectList).from(qStrCal).where(qStrCal.processTime.trim().goe(fromTime))
                        .orderBy(qStrCal.processTime.asc(), qStrCal.profileId.asc()).fetch();
            } else if (EStringUtil.isEmpty(fromTime) && !EStringUtil.isEmpty(toTime)) {
                strCals = query.select(selectList).from(qStrCal).where(qStrCal.processTime.trim().loe(toTime))
                        .orderBy(qStrCal.processTime.asc(), qStrCal.profileId.asc()).fetch();
            } else if (!EStringUtil.isEmpty(fromTime) && !EStringUtil.isEmpty(toTime)) {
                strCals = query.select(selectList).from(qStrCal)
                        .where(qStrCal.processTime.trim().between(fromTime, toTime))
                        .orderBy(qStrCal.processTime.asc(), qStrCal.profileId.asc()).fetch();
            }
            return strCals;
        }), StrCal.class);
    }
}