package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.dml.SQLInsertClause;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlTemplate;
import co.jp.nej.earth.model.entity.MgrTemplateU;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplateP;
import co.jp.nej.earth.model.sql.QMgrTemplateU;
import co.jp.nej.earth.util.DateUtil;

/**
 * @author p-tvo-thuynd
 *
 * This class implements TemplateAuthorityDao, to do some
 *         action related to authority of user/profile to template
 */
@Repository
public class TemplateAuthorityDaoImpl extends BaseDaoImpl<CtlTemplate> implements TemplateAuthorityDao {

    public TemplateAuthorityDaoImpl() throws Exception {
        super();
    }

    /**
     * get map of TemplateAccessRight object from DB.
     *
     * @param userId
     *            id of current user.
     * @param workspaceId
     *            id of working workspace.
     * @return map of TemplateAccessRight object with key is TemplateKey object.
     */
    @Override
    public Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId)
            throws EarthException {
        Map<TemplateKey, TemplateAccessRight> templateAccessRightMap = new HashMap<TemplateKey, TemplateAccessRight>();
        try {
            EarthQueryFactory factory = ConnectionManager.getEarthQueryFactory(workspaceId);
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            ResultSet resultSet = factory.select(qCtlTemplate.templateId, qCtlTemplate.accessAuthority)
                    .from(qCtlTemplate).where(qCtlTemplate.userId.eq(userId)).getResults();
            TemplateAccessRight templateAccessRight = null;
            TemplateKey templateKey = null;
            while (resultSet.next()) {
                templateAccessRight = new TemplateAccessRight();
                templateKey = new TemplateKey();
                templateKey.setWorkspaceId(workspaceId);
                templateKey.setTemplateId(resultSet.getString(ColumnNames.TEMPLATE_ID.toString()));
                templateAccessRight.setTemplateKey(templateKey);
                templateAccessRight.setAccessRight(
                        AccessRight.values()[resultSet.getInt(ColumnNames.ACCESS_AUTHORITY.toString())]);
                templateAccessRightMap.put(templateKey, templateAccessRight);
            }
        } catch (Exception e) {
            throw new EarthException(e);
        }
        return templateAccessRightMap;
    }

    @Override
    public long deleteListByProfileIds(String workspaceId, List<String> profileIds) throws EarthException {
        try {
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            return earthQueryFactory.delete(qMgrTemplateP).where(qMgrTemplateP.profileId.in(profileIds)).execute();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public long deleteListByUserIds(String workspaceId, List<String> userIds) throws EarthException {
        try {
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            earthQueryFactory.delete(qMgrTemplateU).where(qMgrTemplateU.userId.in(userIds)).execute();
            return earthQueryFactory.delete(qCtlTemplate).where(qCtlTemplate.userId.in(userIds)).execute();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public long insertMixAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights)
            throws EarthException {
        try {
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            SQLInsertClause insert = earthQueryFactory.insert(qCtlTemplate);
            for (UserAccessRight userAccessRight : userAccessRights) {
                insert.set(qCtlTemplate.templateId, templateKey.getTemplateId())
                        .set(qCtlTemplate.userId, userAccessRight.getUserId())
                        .set(qCtlTemplate.accessAuthority, userAccessRight.getAccessRight().getValue())
                        .set(qCtlTemplate.lastUpdateTime,
                                DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS))
                        .addBatch();
            }

            return insert.execute();
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public long searchAllMixAuthority(TemplateKey templateKey) throws EarthException {
        BooleanBuilder condition = new BooleanBuilder();
        QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
        Predicate pre1 = qCtlTemplate.templateId.eq(templateKey.getTemplateId());
        condition.and(pre1);
        return search(templateKey.getWorkspaceId(), condition).size();
    }

    @Override
    public long deleteAllMixAuthority(TemplateKey templateKey) throws EarthException {
        QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qCtlTemplate.templateId, templateKey.getTemplateId());
        return delete(templateKey.getWorkspaceId(), condition);
    }

    @Override
    public List<TemplateKey> getTemplateKeysByProfile(String workspaceId, String profileId) throws EarthException {
        try {
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            List<TemplateKey> templateKeys = new ArrayList<TemplateKey>();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            List<String> templateIds = (List<String>) earthQueryFactory.select(qMgrTemplateP.templateId)
                    .from(qMgrTemplateP).where(qMgrTemplateP.profileId.eq(profileId)).fetch();
            for (String templateId : templateIds) {
                TemplateKey templateKey = new TemplateKey();
                templateKey.setTemplateId(templateId);
                templateKey.setWorkspaceId(workspaceId);
                templateKeys.add(templateKey);
            }
            return templateKeys;
        } catch (Exception ex) {
            throw new EarthException(ex);

        }
    }

    @Override
    public List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException {
        List<UserAccessRight> userAccessRights = new ArrayList<UserAccessRight>();
        try {
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            QBean<MgrTemplateU> selectList = Projections.bean(MgrTemplateU.class, qMgrTemplateU.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            List<MgrTemplateU> mgrTemplateUs = earthQueryFactory.select(selectList).from(qMgrTemplateU)
                    .where(qMgrTemplateU.templateId.eq(templateKey.getTemplateId())).fetch();
            for (MgrTemplateU mgrTemplateU : mgrTemplateUs) {
                UserAccessRight userAccessRight = new UserAccessRight();
                userAccessRight.setUserId(mgrTemplateU.getUserId());
                userAccessRight.setAccessRight(AccessRight.values()[mgrTemplateU.getAccessAuthority()]);
                userAccessRights.add(userAccessRight);
            }
            return userAccessRights;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException {
        List<ProfileAccessRight> profileAccessRights = new ArrayList<ProfileAccessRight>();
        try {
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId())
                    .select(qMgrTemplateP.profileId, qMgrTemplateP.accessAuthority).from(qMgrTemplateP)
                    .where(qMgrTemplateP.templateId.eq(templateKey.getTemplateId())).getResults();
            while (resultSet.next()) {
                ProfileAccessRight profileAccessRight = new ProfileAccessRight();
                profileAccessRight.setProfileId(resultSet.getString(ColumnNames.PROFILE_ID.toString()));
                profileAccessRight.setAccessRight(
                        AccessRight.values()[resultSet.getInt(ColumnNames.ACCESS_AUTHORITY.toString())]);
                profileAccessRights.add(profileAccessRight);
            }
        } catch (Exception e) {
            throw new EarthException(e);
        }
        return profileAccessRights;
    }

    @Override
    public long deleteAllUserAuthority(TemplateKey templateKey) throws EarthException {
        try {
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            long deletedRecordNumber = earthQueryFactory.delete(qMgrTemplateU)
                    .where(qMgrTemplateU.templateId.eq(templateKey.getTemplateId())).execute();
            return deletedRecordNumber;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public long insertUserAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights)
            throws EarthException {
        try {
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            SQLInsertClause insert = earthQueryFactory.insert(qMgrTemplateU);
            for (UserAccessRight userAccessRight : userAccessRights) {
                insert.set(qMgrTemplateU.templateId, templateKey.getTemplateId())
                        .set(qMgrTemplateU.userId, userAccessRight.getUserId())
                        .set(qMgrTemplateU.accessAuthority, userAccessRight.getAccessRight().getValue())
                        .set(qMgrTemplateU.lastUpdateTime,
                                DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD))
                        .addBatch();
            }
            return insert.execute();
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public long deleteAllProfileAuthority(TemplateKey templateKey) throws EarthException {
        try {
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            long deletedRecordNumber = earthQueryFactory.delete(qMgrTemplateP)
                    .where(qMgrTemplateP.templateId.eq(templateKey.getTemplateId())).execute();
            return deletedRecordNumber;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public long insertProfileAuthority(TemplateKey templateKey, List<ProfileAccessRight> profileAccessRights)
            throws EarthException {
        try {
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            SQLInsertClause insert = earthQueryFactory.insert(qMgrTemplateP);
            for (ProfileAccessRight profileAccessRight : profileAccessRights) {
                insert.set(qMgrTemplateP.templateId, templateKey.getTemplateId())
                        .set(qMgrTemplateP.profileId, profileAccessRight.getProfileId())
                        .set(qMgrTemplateP.accessAuthority, profileAccessRight.getAccessRight().getValue())
                        .set(qMgrTemplateP.lastUpdateTime,
                                DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD))
                        .addBatch();
            }
            return insert.execute();
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    @Override
    public long countMixAuthority(TemplateKey templateKey) throws EarthException {
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            QBean<CtlTemplate> selectList = Projections.bean(CtlTemplate.class, qCtlTemplate.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            return earthQueryFactory.select(selectList).from(qCtlTemplate)
                    .where(qCtlTemplate.templateId.eq(templateKey.getTemplateId())).fetch().size();
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }
}
