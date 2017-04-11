package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrTemplateU;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplateP;
import co.jp.nej.earth.model.sql.QMgrTemplateU;
import co.jp.nej.earth.util.DateUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.dml.SQLInsertClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author p-tvo-thuynd this class implements TemplateAuthorityDao, to do some
 *         action related to authority of user/profile to template
 */
@Repository
public class TemplateAuthorityDaoImpl implements TemplateAuthorityDao {

    @Autowired
    private WorkspaceDao workspaceDao;

    @Override
    public Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId)
            throws EarthException {
        Map<TemplateKey, TemplateAccessRight> templateAccessRightMap = new HashMap<TemplateKey, TemplateAccessRight>();
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qCtlTemplate.templateId, qCtlTemplate.accessAuthority).from(qCtlTemplate)
                    .where(qCtlTemplate.userId.eq(userId)).getResults();
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
            throw new EarthException(e.getMessage());
        }
        return templateAccessRightMap;
    }

    @Override
    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager
                        .getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
                earthQueryFactory.delete(qMgrTemplateP).where(qMgrTemplateP.profileId.in(profileIds)).execute();
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager
                        .getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
                earthQueryFactory.delete(qCtlTemplate).where(qCtlTemplate.userId.in(userIds)).execute();
                earthQueryFactory.delete(qMgrTemplateU).where(qMgrTemplateU.userId.in(userIds)).execute();
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean insertMixAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights)
            throws EarthException {
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            SQLInsertClause insert = earthQueryFactory.insert(qCtlTemplate);
            for (UserAccessRight userAccessRight : userAccessRights) {
                insert.set(qCtlTemplate.templateId, templateKey.getTemplateId())
                        .set(qCtlTemplate.userId, userAccessRight.getUserId())
                        .set(qCtlTemplate.accessAuthority, userAccessRight.getAccessRight().getValue())
                        .set(qCtlTemplate.lastUpdateTime,
                                DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS))
                        .addBatch();
            }
            long inserted = insert.execute();
            return inserted > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteAllMixAuthority(TemplateKey templateKey) throws EarthException {
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
            long delete = earthQueryFactory.delete(qCtlTemplate)
                    .where(qCtlTemplate.templateId.eq(templateKey.getTemplateId())).execute();
            return delete > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<TemplateKey> getTemplateKeysByProfile(String profileId) throws EarthException {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            List<TemplateKey> templateKeys = new ArrayList<TemplateKey>();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager
                        .getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
                List<String> templateIds = (List<String>) earthQueryFactory.select(qMgrTemplateP.templateId)
                        .from(qMgrTemplateP).where(qMgrTemplateP.profileId.eq(profileId)).fetch();
                for (String templateId : templateIds) {
                    TemplateKey templateKey = new TemplateKey();
                    templateKey.setTemplateId(templateId);
                    templateKey.setWorkspaceId(mgrWorkspace.getWorkspaceId());
                    templateKeys.add(templateKey);
                }
            }
            return templateKeys;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());

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
            throw new EarthException(e.getMessage());
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
            throw new EarthException(e.getMessage());
        }
        return profileAccessRights;
    }

    @Override
    public boolean deleteAllUserAuthority(TemplateKey templateKey) throws EarthException {
        QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
        earthQueryFactory.delete(qMgrTemplateU).where(qMgrTemplateU.templateId.eq(templateKey.getTemplateId()))
                .execute();
        return true;
    }

    @Override
    public boolean insertUserAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights)
            throws EarthException {
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
        return insert.execute() > 0;
    }

    @Override
    public boolean deleteAllProfileAuthority(TemplateKey templateKey) throws EarthException {
        QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(templateKey.getWorkspaceId());
        earthQueryFactory.delete(qMgrTemplateP).where(qMgrTemplateP.templateId.eq(templateKey.getTemplateId()))
                .execute();
        return true;
    }

    @Override
    public boolean insertProfileAuthority(TemplateKey templateKey, List<ProfileAccessRight> profileAccessRights)
            throws EarthException {
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
        return insert.execute() > 0;
    }
}