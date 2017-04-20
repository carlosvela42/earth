package co.jp.nej.earth.dao;

import static co.jp.nej.earth.model.constant.Constant.EARTH_WORKSPACE_ID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.model.sql.QMgrProfile;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import com.querydsl.core.types.Path;

@Repository
public class LoginControlDaoImpl extends BaseDaoImpl<CtlLogin> implements LoginControlDao {

    private static final QCtlLogin qcCtlLogin = QCtlLogin.newInstance();

    public LoginControlDaoImpl() throws Exception {
        super();
    }

    public boolean insertOne(CtlLogin ctlLogin) throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        SQLInsertClause sic = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID).insert(qCtlLogin)
                .populate(ctlLogin);
        return sic.execute() > 0L;
    }

    /**
     *
     */
    public List<StrCal> getNumberOnlineUserByProfile(String userId) throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
        QMgrProfile qMgrProfile = QMgrProfile.newInstance();

        List<StrCal> strCals = new ArrayList<StrCal>();
        StrCal strCal = null;
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);
        try {
            ResultSet resultSet = earthQueryFactory
                    .select(qMgrUserProfile.profileId, qCtlLogin.userId.count().as("useLicenseCount"),
                            qMgrProfile.availableLicenseCount)
                    .from(qMgrUserProfile).join(qCtlLogin).on(qMgrUserProfile.userId.eq(qCtlLogin.userId))
                    .join(qMgrProfile).on(qMgrUserProfile.profileId.eq(qMgrProfile.profileId))
                    .where(qCtlLogin.logoutTime.isNull()
                            .and(qMgrUserProfile.profileId.in(earthQueryFactory.select(qMgrUserProfile.profileId)
                                    .from(qMgrUserProfile).where(qMgrUserProfile.userId.eq(userId)))))
                    .groupBy(qMgrUserProfile.profileId, qMgrProfile.availableLicenseCount).getResults();
            while (resultSet.next()) {
                strCal = new StrCal();
                strCal.setProfileId(resultSet.getString(ColumnNames.PROFILE_ID.toString()));
                strCal.setUseLicenseCount(resultSet.getInt("useLicenseCount"));
                strCal.setAvailableLicenseCount(resultSet.getInt(ColumnNames.AVAILABLE_LICENSE_COUNT.toString()));
                strCals.add(strCal);
            }
        } catch (SQLException e) {
            throw new EarthException(e.getMessage());
        }
        return strCals;
    }

    public boolean updateOutTime(String sessionId, String outTime) throws EarthException {
        QCtlLogin qcCtlLogin = QCtlLogin.newInstance();
        SQLUpdateClause suc = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID).update(qcCtlLogin);
        suc.set(qcCtlLogin.logoutTime, outTime);
        suc.where(qcCtlLogin.sessionId.eq(sessionId));
        return suc.execute() > 0L;
    }

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {

            List<Map<Path<?>, Object>> conditions = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qcCtlLogin.userId, userId);
                conditions.add(condition);
            }
            return this.deleteList(Constant.EARTH_WORKSPACE_ID, conditions) >= 0;

        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
