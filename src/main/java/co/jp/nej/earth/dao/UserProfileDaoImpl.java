package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class UserProfileDaoImpl extends BaseDaoImpl<MgrUserProfile> implements UserProfileDao {

    private static final QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();

    public UserProfileDaoImpl() throws Exception {
        super();
    }

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            List<Map<Path<?>, Object>> conditions = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrUserProfile.userId, userId);
                conditions.add(condition);
            }
            return this.deleteList(Constant.EARTH_WORKSPACE_ID, conditions) > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qMgrUserProfile).where(qMgrUserProfile.profileId.in(profileIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<MgrUserProfile> getListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            QBean<MgrUserProfile> selectList = Projections.bean(MgrUserProfile.class, qMgrUserProfile.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrUserProfile> mgrUserProfiles = earthQueryFactory.select(selectList).from(qMgrUserProfile).where(
                    qMgrUserProfile.profileId.in(profileIds)).fetch();
            return mgrUserProfiles;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
