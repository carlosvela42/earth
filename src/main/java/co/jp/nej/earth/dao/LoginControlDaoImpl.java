package co.jp.nej.earth.dao;

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

    /**
     *
     */
    public List<StrCal> getNumberOnlineUserByProfile(String userId) throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
        QMgrProfile qMgrProfile = QMgrProfile.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);

        List<StrCal> strCals = new ArrayList<StrCal>();
        StrCal strCal = null;
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
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return strCals;
    }

    public long updateOutTime(String sessionId, String outTime) throws EarthException {
        QCtlLogin qcCtlLogin = QCtlLogin.newInstance();
        EarthQueryFactory query = ConnectionManager.getEarthQueryFactory(EARTH_WORKSPACE_ID);

        return (long) executeWithException(() -> {
            return query
                    .update(qcCtlLogin).set(qcCtlLogin.logoutTime, outTime)
                    .where(qcCtlLogin.sessionId.eq(sessionId))
                    .execute();
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
