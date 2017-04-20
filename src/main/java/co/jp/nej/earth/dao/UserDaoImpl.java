package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.sql.QMgrUser;
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
public class UserDaoImpl extends BaseDaoImpl<MgrUser> implements UserDao {

    private static final QMgrUser qMgrUser = QMgrUser.newInstance();

    public UserDaoImpl() throws Exception {
        super();
    }

    public MgrUser getById(String userId) throws EarthException {
        try {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrUser.userId, userId);
            return this.findOne(Constant.EARTH_WORKSPACE_ID, condition);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<MgrUser> getAll() throws EarthException {
        try {
            return this.findAll(Constant.EARTH_WORKSPACE_ID, null, null, null);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public MgrUser updateOne(MgrUser mgrUser) throws EarthException {
        try {
            mgrUser = new MgrUser(mgrUser.getUserId(), mgrUser.getName(), mgrUser.getPassword(), mgrUser
                    .getConfirmPassword(), mgrUser.isChangePassword());
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrUser.userId, mgrUser.getUserId());
            Map<Path<?>, Object> valueMap = new HashMap<>();
            if (mgrUser.isChangePassword()) {
                valueMap.put(qMgrUser.name, mgrUser.getName());
                valueMap.put(qMgrUser.password, mgrUser.getPassword());

            } else {
                valueMap.put(qMgrUser.name, mgrUser.getName());
            }
            return this.update(Constant.EARTH_WORKSPACE_ID, condition, valueMap) >= 0 ? mgrUser : null;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean deleteList(List<String> userIds) throws EarthException {
        try {
            List<Map<Path<?>, Object>> conditions = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrUser.userId, userId);
                conditions.add(condition);
            }
            return this.deleteList(Constant.EARTH_WORKSPACE_ID, conditions) > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<MgrUser> getUsersByProfileId(String profileId) throws EarthException {
        try {
            QBean<MgrUser> selectList = Projections.bean(MgrUser.class, qMgrUser.all());
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            List<MgrUser> mgrUsers = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(
                    selectList).from(qMgrUser).innerJoin(qMgrUserProfile).on(qMgrUser.userId.eq(qMgrUserProfile
                    .userId)).where(qMgrUserProfile.profileId.eq(profileId)).fetch();
            return mgrUsers;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<String> getUserIdsByProfileId(String profileId) throws EarthException {
        try {
            QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
            List<String> userIds = (List<String>) ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                    .select(
                qMgrUser.userId).from(qMgrUser).innerJoin(qMgrUserProfile).on(qMgrUser.userId.eq(qMgrUserProfile
                    .userId)).where(qMgrUserProfile.profileId.eq(profileId)).fetch();
            return userIds;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

}
