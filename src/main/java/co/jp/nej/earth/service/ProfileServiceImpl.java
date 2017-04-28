package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.EvidenceLogDao;
import co.jp.nej.earth.dao.MenuAuthorityDao;
import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.TemplateAuthorityDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.dao.UserProfileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.ScreenItem;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.util.UserAcessRightUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private MessageSource messageSource;

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
    private EvidenceLogDao evidenceLogDao;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Override
    public List<MgrProfile> getAll() throws EarthException {
        try {
            return profileDao.getAll();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDetail(String profileId) throws EarthException {
        try {
            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("mgrProfile", profileDao.getById(profileId));
            List<String> users = userDao.getUserIdsByProfileId(profileId);
            String userIds = "";
            for (String s : users) {
                userIds += s + ",";
            }
            if (userIds.length() > 0) {
                userIds = userIds.substring(0, userIds.length() - 1);
            }
            detail.put("userIds", userIds);
            detail.put("mgrUsers", userDao.getAll());
            return detail;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<Message> validate(MgrProfile mgrProfile, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (EStringUtil.isEmpty(mgrProfile.getProfileId())) {
                Message message = new Message(Constant.MessageUser.USR_BLANK, messageSource.getMessage(ErrorCode.E0001,
                        new String[] { ScreenItem.PROFILE_ID }, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (!EStringUtil.checkAlphabet(mgrProfile.getProfileId())) {
                    Message message = new Message(Constant.MessageUser.USR_SPECIAL, messageSource
                            .getMessage(ErrorCode.E0007, new String[] { ScreenItem.PROFILE_ID }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
            }
            if (EStringUtil.isEmpty(mgrProfile.getDescription())) {
                Message message = new Message(Constant.MessageUser.NAME_BLANK, messageSource
                        .getMessage(ErrorCode.E0001, new String[] { ScreenItem.DESCRIPTION }, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (isExist(mgrProfile.getProfileId())) {
                    Message message = new Message(Constant.MessageUser.USR_EXIST,
                            messageSource.getMessage(ErrorCode.E0005,
                                    new String[] { mgrProfile.getProfileId(), ScreenItem.PROFILE }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
            }
            return listMessage;
        } catch (Exception ex) {
            Message message = new Message("",
                    messageSource.getMessage(ErrorCode.E1009, new String[] { "" }, Locale.ENGLISH));
            listMessage.add(message);
            return listMessage;
        }
    }

    @Override
    public boolean insertAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {
        try {
            mgrProfile.setLastUpdateTime(
                    DateUtil.getCurrentDate(DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD)));
            MgrProfile insertProfile = profileDao.insertOne(mgrProfile);
            boolean assignUser = true;
            if (userIds.size() > 0) {
                assignUser = profileDao.assignUsers(mgrProfile.getProfileId(), userIds);
            }
            if (insertProfile != null && assignUser) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean updateAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {
        try {
            MgrProfile updateProfile = profileDao.updateOne(mgrProfile);
            boolean unAssignUser = profileDao.unAssignAllUsers(mgrProfile.getProfileId());
            List<String> profileIds = new ArrayList<>();
            profileIds.add(mgrProfile.getProfileId());
            List<MgrMenu> mgrMenus = menuService.getMenuByProfileId(profileIds);
            /**
             * insert mix authority for menu
             */
            for (MgrMenu mgrMenu : mgrMenus) {
                menuAuthorityDao.deleteAllMixAuthority(mgrMenu.getFunctionId());
                List<UserAccessRight> userAccessRightByProfiles = menuAuthorityDao
                        .getUserAuthorityByProfiles(mgrMenu.getFunctionId());
                List<UserAccessRight> userAccessRights = menuAuthorityDao.getUserAuthority(mgrMenu.getFunctionId());
                List<UserAccessRight> menuAccessRights = UserAcessRightUtil.mixAuthority(userAccessRights,
                        userAccessRightByProfiles);
                menuAuthorityDao.insertMixAuthority(mgrMenu.getFunctionId(), menuAccessRights);
            }

            List<TemplateKey> templateKeys = templateAuthorityDao.getTemplateKeysByProfile(mgrProfile.getProfileId());
            List<MgrUserProfile> mgrUserProfiles = userProfileDao.getListByProfileIds(profileIds);
            /**
             * insert mix authority for template
             */
            for (TemplateKey templateKey : templateKeys) {
                templateAuthorityDao.deleteAllMixAuthority(templateKey);
                List<ProfileAccessRight> profileAccessRights = templateAuthorityDao.getProfileAuthority(templateKey);
                Map<String, AccessRight> mapAccessRightP = new HashMap<String, AccessRight>();
                for (ProfileAccessRight profileAccessRight : profileAccessRights) {
                    mapAccessRightP.put(profileAccessRight.getProfileId(), profileAccessRight.getAccessRight());
                }
                List<UserAccessRight> userAccessRightByProfiles = UserAcessRightUtil.getUserAccessRightProfiles(
                        mgrUserProfiles, mapAccessRightP);
                List<UserAccessRight> userAccessRights = templateAuthorityDao.getUserAuthority(templateKey);
                List<UserAccessRight> templateAccessRights = UserAcessRightUtil.mixAuthority(userAccessRights,
                        userAccessRightByProfiles);
                templateAuthorityDao.insertMixAuthority(templateKey, templateAccessRights);
            }
            if (updateProfile != null && unAssignUser) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteList(List<String> profileIds) throws EarthException {
        try {
            userProfileDao.deleteListByProfileIds(profileIds);
            evidenceLogDao.deleteListByProfileIds(profileIds);
            templateAuthorityDao.deleteListByProfileIds(profileIds);
            menuAuthorityDao.deleteListByProfileIds(profileIds);
            return profileDao.deleteList(profileIds);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    private boolean isExist(String profileId) {
        try {
            return (profileDao.getById(profileId) != null);
        } catch (Exception ex) {
            return true;
        }
    }

}