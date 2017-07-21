package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.model.sql.QMstCode;
import co.jp.nej.earth.model.sql.QMstSystem;
import com.querydsl.core.types.Path;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.jp.nej.earth.model.constant.Constant.EARTH_WORKSPACE_ID;

@Repository
public class LoginControlDaoImpl extends BaseDaoImpl<CtlLogin> implements LoginControlDao {

    private static final QCtlLogin qcCtlLogin = QCtlLogin.newInstance();

    public LoginControlDaoImpl() throws Exception {
        super();
    }

    public List<StrCal> getNumberOnlineUserByProfile(String userId) throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
        QMstSystem qMstSystem = QMstSystem.newInstance();
        QMstCode qMstCode = QMstCode.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);
        List<StrCal> strCals = new ArrayList<StrCal>();
        try {
            ResultSet resultSet = earthQueryFactory
                    .select(qMstCode.codeValue.as("division"), qMgrUserProfile.profileId,
                            qCtlLogin.userId.count().as("useLicenseCount"),
                            qMstSystem.configValue.as("availableLicenceCount"))
                    .from(qMstCode, qMgrUserProfile).join(qCtlLogin).on(qMgrUserProfile.userId.eq(qCtlLogin.userId))
                    .leftJoin(qMstSystem).on(qMgrUserProfile.profileId.eq(qMstSystem.variableName))
                    .where(qCtlLogin.logoutTime.isNull().and(qCtlLogin.userId.eq(userId))
                            .and(qMstCode.codeId.eq(Constant.NUM_2)).and(qMstCode.section.eq(Constant.DIVISION)))
                    .groupBy(qMstCode.codeValue, qMgrUserProfile.profileId, qMstSystem.configValue).getResults();
            while (resultSet.next()) {
                StrCal strCal = new StrCal();
                strCal.setDivision(resultSet.getString("division"));
                strCal.setProfileId(resultSet.getString(ColumnNames.PROFILE_ID.toString()));
                strCal.setUseLicenseCount(resultSet.getInt("useLicenseCount"));
                strCal.setAvailableLicenseCount(resultSet.getInt("availableLicenceCount"));
                strCals.add(strCal);
            }
        } catch (Exception e) {
            throw new EarthException(e);
        }
        return strCals;
    }

    public List<StrCal> getLicenceOfEntireSystem() throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        QMstSystem qMstSystem = QMstSystem.newInstance();
        QMstCode qMstCode = QMstCode.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);
        List<StrCal> strCals = new ArrayList<StrCal>();
        try {
            ResultSet resultSet = earthQueryFactory
                    .select(qMstCode.codeValue.as("division"),
                            (earthQueryFactory.select(qCtlLogin.userId.countDistinct()).from(qCtlLogin)
                                    .where(qCtlLogin.logoutTime.isNull()).as("useLicenseCount")),
                            qMstSystem.configValue.as("availableLicenceCount"))
                    .from(qMstCode, qMstSystem)
                    .where(qMstCode.codeId.eq(Constant.NUM_1).and(qMstCode.section.eq(Constant.DIVISION))
                            .and(qMstSystem.section.eq(Constant.AVAILABLELICENCECOUNT)
                                    .and(qMstSystem.variableName.eq(Constant.ENTIRE_SYSTEM))))
                    .getResults();
            while (resultSet.next()) {
                StrCal strCal = new StrCal();
                strCal.setDivision(resultSet.getString("division"));
                strCal.setProfileId(null);
                strCal.setUseLicenseCount(resultSet.getInt("useLicenseCount"));
                strCal.setAvailableLicenseCount(resultSet.getInt("availableLicenceCount"));
                strCals.add(strCal);
            }
        } catch (Exception e) {
            throw new EarthException(e);
        }
        return strCals;
    }

    public long updateOutTime(String sessionId, String outTime) throws EarthException {
        QCtlLogin qcCtlLogin = QCtlLogin.newInstance();
        EarthQueryFactory query = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);

        return (long) executeWithException(() -> {
            return query.update(qcCtlLogin).set(qcCtlLogin.logoutTime, outTime)
                    .where(qcCtlLogin.sessionId.eq(sessionId)).execute();
        });
    }

    public long deleteListByUserIds(List<String> userIds) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        for (String userId : userIds) {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qcCtlLogin.userId, userId);
            conditions.add(condition);
        }
        return this.deleteList(Constant.EARTH_WORKSPACE_ID, conditions);

    }
}
