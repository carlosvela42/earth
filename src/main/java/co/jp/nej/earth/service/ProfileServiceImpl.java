package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import co.jp.nej.earth.dao.MenuAuthorityDao;
import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.TemplateAuthorityDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.dao.UserProfileDao;
import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.ScreenItem;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.sql.QCtlMenu;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.UserAcessRightUtil;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private EMessageResource eMessageResource;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuAuthorityDao menuAuthorityDao;

    @Autowired
    private TemplateAuthorityDao templateAuthorityDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private WorkspaceDao workspaceDao;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Override
    public List<MgrProfile> getAll() throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        List<MgrProfile> mgrProfiles = null;
        try {
            mgrProfiles = profileDao.findAll(Constant.EARTH_WORKSPACE_ID, null, null, null);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            throw new EarthException(ex.getMessage());
        }
        return mgrProfiles;
    }

    @Override
    public Map<String, Object> getDetail(String profileId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        Map<String, Object> detail = new HashMap<String, Object>();
        try {

            MgrProfile mgrProfile = profileDao.getById(profileId);
            List<String> users = userDao.getUserIdsByProfileId(profileId);
            List<MgrUser> mgrUsers = userDao.getAll();
            String userIds = "";
            userIds = EStringUtil.getStringFromList(",", users);

            detail.put("mgrProfile", mgrProfile);
            detail.put("userIds", userIds);
            detail.put("mgrUsers", mgrUsers);
            transactionManager.getManager().commit(transactionManager.getTxStatus());

        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            throw new EarthException(ex.getMessage());
        }
        return detail;
    }

    @Override
    public List<Message> validate(MgrProfile mgrProfile, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (EStringUtil.isEmpty(mgrProfile.getProfileId())) {
                Message message = new Message(Constant.MessageUser.USR_BLANK,
                        eMessageResource.get(ErrorCode.E0001, new String[] { ScreenItem.PROFILE_ID }));
                listMessage.add(message);
                return listMessage;
            }

            if (insert) {
                if (!EStringUtil.checkAlphabet(mgrProfile.getProfileId())) {
                    Message message = new Message(Constant.MessageUser.USR_SPECIAL,
                            eMessageResource.get(ErrorCode.E0007, new String[] { ScreenItem.PROFILE_ID }));
                    listMessage.add(message);
                    return listMessage;
                }
            }

            if (EStringUtil.isEmpty(mgrProfile.getDescription())) {
                Message message = new Message(Constant.MessageUser.NAME_BLANK,
                        eMessageResource.get(ErrorCode.E0001, new String[] { ScreenItem.DESCRIPTION }));
                listMessage.add(message);
                return listMessage;
            }

            if (insert) {
                if (isExist(mgrProfile.getProfileId())) {
                    Message message = new Message(Constant.MessageUser.USR_EXIST, eMessageResource.get(ErrorCode.E0005,
                            new String[] { mgrProfile.getProfileId(), ScreenItem.PROFILE }));
                    listMessage.add(message);
                    return listMessage;
                }
            }

            return listMessage;
        } catch (Exception ex) {
            Message message = new Message(EStringUtil.EMPTY,
                    eMessageResource.get(ErrorCode.E1009, new String[] { EStringUtil.EMPTY }));
            listMessage.add(message);
            return listMessage;
        }
    }

    @Override
    public boolean insertAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        mgrProfile.setLastUpdateTime(
                DateUtil.getCurrentDate(DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD)));

        long insertProfile = profileDao.insertOne(mgrProfile);
        boolean assignUser = true;
        if (userIds.size() > 0) {
            assignUser = profileDao.assignUsers(mgrProfile.getProfileId(), userIds) == userIds.size();
        }
        if (insertProfile > 0 && assignUser) {
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return true;
        }
        transactionManager.getManager().rollback(transactionManager.getTxStatus());
        return false;
    }

    @Override
    public boolean updateAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {

        List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
        transactionManagers.add(new TransactionManager(Constant.EARTH_WORKSPACE_ID));
        try {
            if (profileDao.updateOne(mgrProfile) <= 0) {
                throw new EarthException("Update information of Profile fail");
            }
            userProfileDao.deleteListByProfileIds(new ArrayList<>(Arrays.asList(mgrProfile.getProfileId())));
            if (userDao.getUserIdsByProfileId(mgrProfile.getProfileId()).size() > 0) {
                throw new EarthException("UnAssign Users for Profile fail");
            }
            if (profileDao.assignUsers(mgrProfile.getProfileId(), userIds) != userIds.size()) {
                throw new EarthException("Assign User to Profile fail");
            }

            List<String> profileIds = new ArrayList<>();
            profileIds.add(mgrProfile.getProfileId());
            List<MgrMenu> mgrMenus = menuService.getMenuByProfileId(profileIds);

         // Insert mix authority for menus.
            insertMenusMixAuthority(mgrMenus);

            // Insert mix authority for templates.
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            if (mgrWorkspaces != null && mgrWorkspaces.size() > 0) {
                List<MgrUserProfile> mgrUserProfiles = userProfileDao.getListByProfileIds(profileIds);
                for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                    transactionManagers.add(new TransactionManager(mgrWorkspace.getWorkspaceId()));
                    List<TemplateKey> templateKeys = templateAuthorityDao
                            .getTemplateKeysByProfile(mgrWorkspace.getWorkspaceId(), mgrProfile.getProfileId());
                    if (templateKeys != null && templateKeys.size() > 0
                            && mgrUserProfiles != null && mgrUserProfiles.size() > 0) {
                        insertTemplatesMixAuthority(templateKeys, mgrUserProfiles);
                    }
                }
            }

        } catch (EarthException ex) {
            // Rollback transactions.
            if (transactionManagers != null && transactionManagers.size() > 0) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    TransactionManager transactionManager1 = transactionManagers.get(i);
                    transactionManager1.getManager().rollback(transactionManager1.getTxStatus());
                }
            }
            LOG.error(ex.getMessage());
            return false;
        }

        // Commit transactions.
        if (transactionManagers != null && transactionManagers.size() > 0) {
            for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                TransactionManager transactionManager1 = transactionManagers.get(i);
                transactionManager1.getManager().commit(transactionManager1.getTxStatus());
            }
        }

        return true;
    }

    private void insertTemplatesMixAuthority(List<TemplateKey> templateKeys, List<MgrUserProfile> mgrUserProfiles)
            throws EarthException {
        for (TemplateKey templateKey : templateKeys) {
            templateAuthorityDao.deleteAllMixAuthority(templateKey);

            if (templateAuthorityDao.searchAllMixAuthority(templateKey) > 0) {
                throw new EarthException("Delete Mix template " + templateKey.getTemplateId() + " in workspace "
                        + templateKey.getTemplateId());
            }

            List<ProfileAccessRight> profileAccessRights = templateAuthorityDao.getProfileAuthority(templateKey);
            Map<String, AccessRight> mapAccessRightP = new HashMap<String, AccessRight>();

            if (profileAccessRights != null && profileAccessRights.size() > 0) {
                for (ProfileAccessRight profileAccessRight : profileAccessRights) {
                    mapAccessRightP.put(profileAccessRight.getProfileId(), profileAccessRight.getAccessRight());
                }
                List<UserAccessRight> userAccessRightByProfiles = UserAcessRightUtil
                        .getUserAccessRightProfiles(mgrUserProfiles, mapAccessRightP);
                List<UserAccessRight> userAccessRights = templateAuthorityDao.getUserAuthority(templateKey);
                if ((userAccessRightByProfiles != null && userAccessRightByProfiles.size() > 0)
                        && (userAccessRights != null && userAccessRights.size() > 0)) {
                    List<UserAccessRight> templateAccessRights = UserAcessRightUtil.mixAuthority(userAccessRights,
                            userAccessRightByProfiles);

                    if (templateAuthorityDao.insertMixAuthority(templateKey, templateAccessRights)
                            != templateAccessRights.size()) {
                        throw new EarthException("Insert Mix template " + templateKey.getTemplateId() + " in workspace "
                                + templateKey.getTemplateId());
                    }
                }
            }
        }
    }

    private void insertMenusMixAuthority(List<MgrMenu> mgrMenus) throws EarthException {
        if (mgrMenus != null) {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            // Insert mix authority for menu.
            for (MgrMenu mgrMenu : mgrMenus) {
                menuAuthorityDao.deleteAllMixAuthority(new ArrayList<>(Arrays.asList(mgrMenu.getFunctionId())));

                BooleanBuilder condition = new BooleanBuilder();
                Predicate pre1 = qCtlMenu.functionId.eq(mgrMenu.getFunctionId());
                condition.and(pre1);
                if (menuAuthorityDao.search(Constant.EARTH_WORKSPACE_ID, pre1, null, null, null).size() > 0) {
                    throw new EarthException("Delete Mix Menu fail");
                }

                List<UserAccessRight> userAccessRightByProfiles = menuAuthorityDao
                        .getUserAuthorityByProfiles(mgrMenu.getFunctionId());
                List<UserAccessRight> userAccessRights = menuAuthorityDao.getUserAuthority(mgrMenu.getFunctionId());
                if ((userAccessRightByProfiles != null && userAccessRightByProfiles.size() > 0)
                        && (userAccessRights != null && userAccessRights.size() > 0)) {
                    List<UserAccessRight> menuAccessRights = UserAcessRightUtil.mixAuthority(userAccessRights,
                            userAccessRightByProfiles);

                    if (menuAuthorityDao.insertMixAuthority(mgrMenu.getFunctionId(),
                            menuAccessRights) != menuAccessRights.size()) {
                        throw new EarthException("Insert Mix Menu fail");
                    }
                }
            }
        }
    }

    @Override
    public boolean deleteList(List<String> profileIds) throws EarthException {
        QCtlMenu qCtlMenu = QCtlMenu.newInstance();
        List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        transactionManagers.add(transactionManager);
        try {
            long usersProfile = userProfileDao.getListByProfileIds(profileIds).size();
            if (userProfileDao.deleteListByProfileIds(profileIds) != usersProfile) {
                throw new EarthException("UnAssign Users for Profile fail");
            }

            List<MgrMenu> mgrMenus = menuService.getMenuByProfileId(profileIds);

            // Insert mix authority for menu.
            for (MgrMenu mgrMenu : mgrMenus) {

                menuAuthorityDao.deleteAllMixAuthority(new ArrayList<>(Arrays.asList(mgrMenu.getFunctionId())));

                BooleanBuilder condition = new BooleanBuilder();
                Predicate pre1 = qCtlMenu.functionId.eq(mgrMenu.getFunctionId());
                condition.and(pre1);
                if (menuAuthorityDao.search(Constant.EARTH_WORKSPACE_ID, pre1, null, null, null).size() > 0) {
                    throw new EarthException("Delete Mix Menu fail");
                }

                List<UserAccessRight> userAccessRights = menuAuthorityDao.getUserAuthority(mgrMenu.getFunctionId());

                if (userAccessRights != null && userAccessRights.size() > 0) {
                    if (menuAuthorityDao.insertMixAuthority(mgrMenu.getFunctionId(),
                            userAccessRights) != userAccessRights.size()) {
                        throw new EarthException("Insert Mix Menu fail");
                    }
                }
            }
            if (menuAuthorityDao.deleteListByProfileIds(profileIds) != mgrMenus.size()) {
                throw new EarthException("Delete Authority Menu of Profile fail");
            }

            // Insert mix authority for template.
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                transactionManager = new TransactionManager(mgrWorkspace.getWorkspaceId());
                transactionManagers.add(transactionManager);
                for (String profileId : profileIds) {
                    List<TemplateKey> templateKeys = templateAuthorityDao
                            .getTemplateKeysByProfile(mgrWorkspace.getWorkspaceId(), profileId);
                    for (TemplateKey templateKey : templateKeys) {
                        templateAuthorityDao.deleteAllMixAuthority(templateKey);
                        if (templateAuthorityDao.searchAllMixAuthority(templateKey) > 0) {
                            throw new EarthException("Delete Mix template " + templateKey.getTemplateId()
                                    + " in workspace " + templateKey.getTemplateId());
                        }

                        List<UserAccessRight> userAccessRights = templateAuthorityDao.getUserAuthority(templateKey);
                        if (userAccessRights != null && userAccessRights.size() > 0) {
                            if (templateAuthorityDao.insertMixAuthority(templateKey,
                                    userAccessRights) != userAccessRights.size()) {
                                throw new EarthException("Insert Mix template " + templateKey.getTemplateId()
                                        + " in workspace " + templateKey.getTemplateId());
                            }
                        }
                    }
                    templateAuthorityDao.deleteListByProfileIds(mgrWorkspace.getWorkspaceId(),
                            new ArrayList<>(Arrays.asList(profileId)));
                    if (templateAuthorityDao.getTemplateKeysByProfile(mgrWorkspace.getWorkspaceId(), profileId)
                            .size() > 0) {
                        throw new EarthException("Delete Authority Template of Profile fail");
                    }
                }
            }
            if (profileDao.deleteList(profileIds) != profileIds.size()) {
                throw new EarthException("Delete profile fail");
            }

        } catch (Exception ex) {
            // Rollback transactions.
            if (transactionManagers != null && transactionManagers.size() > 0) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    TransactionManager transactionManager1 = transactionManagers.get(i);
                    transactionManager1.getManager().rollback(transactionManager1.getTxStatus());
                }
            }
            LOG.error(ex.getMessage());
            return false;
        }
        // Commit transactions.
        if (transactionManagers != null && transactionManagers.size() > 0) {
            for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                TransactionManager transactionManager1 = transactionManagers.get(i);
                transactionManager1.getManager().commit(transactionManager1.getTxStatus());
            }
        }
        return true;
    }

    @Override
    public boolean insertOne(MgrProfile mgrProfile) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            long insertOne = profileDao.insertOne(mgrProfile);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return insertOne > 0L;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            return false;
        }
    }

    private boolean isExist(String profileId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            MgrProfile mgrProfile = profileDao.getById(profileId);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return (mgrProfile != null);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            return true;
        }
    }

    @Override
    public boolean assignUsers(String profileId, List<String> userIds) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            long asign = profileDao.assignUsers(profileId, userIds);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return asign == userIds.size();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            return false;
        }
    }

}