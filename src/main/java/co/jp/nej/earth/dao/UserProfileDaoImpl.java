package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDaoImpl implements UserProfileDao {
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QMgrUserProfile qMgrUserProfile= QMgrUserProfile.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qMgrUserProfile).where(qMgrUserProfile.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            QMgrUserProfile qMgrUserProfile= QMgrUserProfile.newInstance();
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
            QMgrUserProfile qMgrUserProfile= QMgrUserProfile.newInstance();
            QBean<MgrUserProfile> selectList= Projections.bean(MgrUserProfile.class,qMgrUserProfile.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrUserProfile> mgrUserProfiles = earthQueryFactory.select(selectList).from(qMgrUserProfile).where
                    (qMgrUserProfile.profileId.in(profileIds)).fetch();
            return mgrUserProfiles;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
