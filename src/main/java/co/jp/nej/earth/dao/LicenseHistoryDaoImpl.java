package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import com.querydsl.sql.dml.SQLInsertClause;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;

import static co.jp.nej.earth.model.constant.Constant.*;

import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.sql.QStrCal;

@Repository
public class LicenseHistoryDaoImpl implements LicenseHistoryDao {

    public boolean insertOne(StrCal strCal) throws EarthException {
        QStrCal qStrCal = QStrCal.newInstance();
        SQLInsertClause sic = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID).insert(qStrCal)
                .populate(strCal);
        return sic.execute() > 0L;
    }
}