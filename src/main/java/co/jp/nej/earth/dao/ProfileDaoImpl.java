package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.sql.QMgrProfile;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.util.DateUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.dml.SQLInsertClause;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileDaoImpl implements ProfileDao {

    public List<MgrProfile> getProfilesByUserId(String userid) throws EarthException {
        try {
            QMgrProfile qMgrProfile = QMgrProfile.newInstance();
            QBean<MgrProfile> selectList = Projections.bean(MgrProfile.class, qMgrProfile.all());
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            List<MgrProfile> profiles = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select
                    (selectList).from(qMgrProfile).innerJoin(qMgrUserProfile).on(qMgrProfile.profileId.eq(qMgrUserProfile.profileId)).where
                    (qMgrUserProfile.userId.eq(userid)).fetch();
            return profiles;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public MgrProfile getById(String profileId) throws EarthException {
        try {
            QMgrProfile qMgrProfile = QMgrProfile.newInstance();
            QBean<MgrProfile> selectList = Projections.bean(MgrProfile.class, qMgrProfile.all());
            MgrProfile mgrProfile = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select
                    (selectList).from(qMgrProfile).where(qMgrProfile.profileId.eq(profileId))
                    .fetchOne();
            return mgrProfile;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<MgrProfile> getAll() throws EarthException {
        try {
            QMgrProfile qMgrProfile = QMgrProfile.newInstance();
            QBean<MgrProfile> selectList = Projections.bean(MgrProfile.class, qMgrProfile.all());
            return ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList).from(qMgrProfile)
                    .fetch();

        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean deleteList(List<String> profileId) throws EarthException {
        return false;
    }

    @Override
    public MgrProfile insertOne(MgrProfile mgrProfile) throws EarthException {
        try {
            QMgrProfile qMgrProfile = QMgrProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long inserted = earthQueryFactory.insert(qMgrProfile).populate(qMgrProfile).execute();
            return inserted > 0 ? mgrProfile : null;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean assignUsers(String profileId, List<String> userIds) throws EarthException {
        try {
            QMgrUserProfile mgrUserProfile = QMgrUserProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            SQLInsertClause insert = earthQueryFactory.insert(mgrUserProfile);
            for (String userId : userIds) {
                insert.set(mgrUserProfile.profileId, profileId).set(mgrUserProfile.userId, userId).set(mgrUserProfile
                        .lastUpdateTime, DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD)).addBatch();
            }
            long inserted = insert.execute();
            return inserted > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean unAssignAllUsers(String profileIds) throws EarthException {
        try {
            QMgrUserProfile mgrUserProfile = QMgrUserProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(mgrUserProfile).where(mgrUserProfile.profileId.eq(profileIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public MgrProfile updateOne(MgrProfile mgrProfile) throws EarthException {
        try {
            QMgrProfile qMgrProfile = QMgrProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long updated = earthQueryFactory.update(qMgrProfile).set(qMgrProfile.description, mgrProfile
                    .getDescription()).set(qMgrProfile.ldapIdentifier,mgrProfile.getLdapIdentifier()).set(qMgrProfile
                    .lastUpdateTime,mgrProfile.getLastUpdateTime())
                    .where(qMgrProfile.profileId.eq(mgrProfile.getProfileId())).execute();
            return updated > 0 ? mgrProfile : null;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
