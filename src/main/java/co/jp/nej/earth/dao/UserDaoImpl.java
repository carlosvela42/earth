package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.sql.QMgrUser;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    public MgrUser getById(String userId) throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            QBean<MgrUser> selectList = Projections.bean(MgrUser.class, qMgrUser.all());
            MgrUser user = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList)
                    .from(qMgrUser).where(qMgrUser.userId.eq(userId)).fetchOne();
            return user;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<MgrUser> getAll() throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            QBean<MgrUser> selectList = Projections.bean(MgrUser.class, qMgrUser.all());
            return ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList).from(qMgrUser)
                    .fetch();
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public MgrUser insertOne(MgrUser mgrUser) throws EarthException {
        try {
            QMgrUser qUser = QMgrUser.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long inserted = earthQueryFactory.insert(qUser).populate(mgrUser).execute();
            return inserted > 0 ? mgrUser : null;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public MgrUser updateOne(MgrUser mgrUser) throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long updated;
            if (mgrUser.isChangePassword()) {
                List<Path<?>> paths = new ArrayList<Path<?>>();
                paths.add(qMgrUser.name);
                paths.add(qMgrUser.password);
                List<String> values = new ArrayList<String>();
                values.add(mgrUser.getName());
                values.add(mgrUser.getPassword());
                updated = earthQueryFactory.update(qMgrUser).set(paths, values)
                        .where(qMgrUser.userId.eq(mgrUser.getUserId())).execute();
            } else {
                updated = earthQueryFactory.update(qMgrUser).set(qMgrUser.name, mgrUser.getName())
                        .where(qMgrUser.userId.eq(mgrUser.getUserId())).execute();
            }

            if (updated > 0) {
                return mgrUser;
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean deleteList(List<String> userIds) throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            long delete = earthQueryFactory.delete(qMgrUser).where(qMgrUser.userId.in(userIds)).execute();
            return delete > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<MgrUser> getUsersByProfileId(String profileId) throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            QBean<MgrUser> selectList = Projections.bean(MgrUser.class, qMgrUser.all());
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            List<MgrUser> mgrUsers = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select
                    (selectList).from(qMgrUser).innerJoin(qMgrUserProfile).on(qMgrUser.userId.eq(qMgrUserProfile
                    .userId)).where
                    (qMgrUserProfile.profileId.eq(profileId)).fetch();
            return mgrUsers;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<String> getUserIdsByProfileId(String profileId) throws EarthException {
        try {
            QMgrUser qMgrUser = QMgrUser.newInstance();
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            List<String> userIds = (List<String>) ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select
                    (qMgrUser.userId).from(qMgrUser).innerJoin(qMgrUserProfile).on(qMgrUser.userId.eq(qMgrUserProfile
                    .userId)).where
                    (qMgrUserProfile.profileId.eq(profileId)).fetch();
            return userIds;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

}
