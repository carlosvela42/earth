package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.TemplateAuthorityDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.dao.UserProfileDao;
import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.sql.QMgrTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplateP;
import co.jp.nej.earth.util.CommonUtil;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private WorkspaceDao workspaceDao;

    @Autowired
    private TemplateAuthorityDao templateAuthorityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private ProfileDao profileDao;

    public List<MgrWorkspace> getAllWorkspace() throws EarthException {
        return workspaceDao.getAll();
    }

    public List<MgrTemplate> getTemplateListInfo(String workspaceId) throws EarthException {
        return templateDao.getAllByWorkspace(workspaceId);
    }

    @Override
    public List<MgrTemplate> getTemplateByProfileId(List<String> profileIds) throws EarthException {
        try {
            QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
            QMgrTemplateP qMgrTemplateP = QMgrTemplateP.newInstance();
            QBean<MgrTemplate> selectList = Projections.bean(MgrTemplate.class, qMgrTemplate.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            List<MgrTemplate> mgrTemplates = earthQueryFactory.select(selectList).from(qMgrTemplate)
                    .innerJoin(qMgrTemplateP).on(qMgrTemplate.templateId.eq(qMgrTemplateP.templateId))
                    .where(qMgrTemplateP.profileId.in(profileIds)).fetch();
            return mgrTemplates;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException {
        return templateAuthorityDao.getUserAuthority(templateKey);
    }

    @Override
    public List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException {
        return templateAuthorityDao.getProfileAuthority(templateKey);
    }

    @Override
    public MgrTemplate getById(TemplateKey templateKey) throws EarthException {
        return templateDao.getById(templateKey);
    }

    @Override
    public List<MgrUser> getAllUser() throws EarthException {
        return userDao.getAll();
    }

    @Override
    public List<MgrProfile> getAllProfile() throws EarthException {
        return profileDao.getAll();
    }

    @Override
    public Map<String, Object> getTemplateListInfo(String workspaceId, List<String> templateIds) throws EarthException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException {
        // TODO Auto-generated method stub
        return templateDao.getAllByWorkspace(workspaceId);
    }

    /**
     * @author p-tvo-thuynd this method is to save authority of user/profile to
     *         template to equivalent table
     * @param templateKey
     *            determine which template are being executed
     * @param tUser
     *            list of UserAccessRight object to be inserted into table
     * @param tProfile
     *            list of ProfileAccessRight object to be inserted into table
     * @return boolean type
     */
    @Override
    public boolean saveAuthority(TemplateKey templateKey, List<UserAccessRight> tUser,
            List<ProfileAccessRight> tProfile) throws EarthException {
        templateAuthorityDao.deleteAllUserAuthority(templateKey);
        templateAuthorityDao.insertUserAuthority(templateKey, tUser);
        templateAuthorityDao.deleteAllProfileAuthority(templateKey);
        templateAuthorityDao.insertProfileAuthority(templateKey, tProfile);
        templateAuthorityDao.deleteAllMixAuthority(templateKey);
        List<String> profileIds = new ArrayList<String>();
        for (ProfileAccessRight profileAccessRight : tProfile) {
            profileIds.add(profileAccessRight.getProfileId());
        }
        List<MgrUserProfile> mgrUserProfiles = userProfileDao.getListByProfileIds(profileIds);
        Map<String, AccessRight> mapAccessRightP = new HashMap<String, AccessRight>();
        for (ProfileAccessRight profileAccessRight : tProfile) {
            mapAccessRightP.put(profileAccessRight.getProfileId(), profileAccessRight.getAccessRight());
        }
        List<UserAccessRight> userAccessRights = CommonUtil.getUserAccessRightProfiles(mgrUserProfiles,
                mapAccessRightP);
        List<UserAccessRight> templateAccessRights = CommonUtil.mixAuthority(tUser, userAccessRights);
        templateAuthorityDao.insertMixAuthority(templateKey, templateAccessRights);
        return true;
    }

    @Override
    public List<MgrTemplate> getTemplateByType(String workspaceId, String templateType) throws EarthException {
        return templateDao.getTemplateByType(workspaceId, templateType);
    }

    @Override
    public boolean deleteTemplates(List<String> templateIds, String workspaceId) throws EarthException {
        return templateDao.deleteTemplates(templateIds, workspaceId);
    }
}
