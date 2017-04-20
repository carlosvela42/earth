package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.dao.LoginControlDao;
import co.jp.nej.earth.dao.MenuAuthorityDao;
import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.dao.TemplateAuthorityDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.dao.UserProfileDao;
import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserInfo;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.DatePattern;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.MessageCodeLogin;
import co.jp.nej.earth.model.constant.Constant.MessageUser;
import co.jp.nej.earth.model.constant.Constant.ScreenItem;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.util.CryptUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.LoginUtil;
import co.jp.nej.earth.util.MenuUtil;
import co.jp.nej.earth.util.PasswordPolicy;
import co.jp.nej.earth.util.TemplateUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginControlDao loginControlDao;

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TemplateAuthorityDao templateAuthorityDao;

    @Autowired
    private WorkspaceDao workspaceDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Autowired
    private MenuAuthorityDao menuAuthorityDao;

    @Autowired
    private PasswordPolicy passwordPolicy;

    /**
     * @param userId
     *            id of user
     * @param password
     *            password of user
     * @param session
     *            HttpSession object
     * @return list message determined that user log in successfully or not
     */
    public List<Message> login(String userId, String password, HttpSession session) throws EarthException {
        List<Message> listMessage = new ArrayList<Message>();
        if (EStringUtil.isEmpty(userId)) {
            Message message = new Message(MessageCodeLogin.USR_BLANK,
                    messageSource.getMessage(ErrorCode.E0001, new String[] { ScreenItem.USER_ID }, Locale.ENGLISH));
            listMessage.add(message);
        }
        if (EStringUtil.isEmpty(password)) {
            Message message = new Message(MessageCodeLogin.PWD_BLANK,
                    messageSource.getMessage(ErrorCode.E0001, new String[] { ScreenItem.PASSWORD }, Locale.ENGLISH));
            listMessage.add(message);
        }
        if (!EStringUtil.checkAlphabet(userId)) {
            Message message = new Message(MessageUser.USR_SPECIAL,
                    messageSource.getMessage(ErrorCode.E0007, new String[] { ScreenItem.USER_ID }, Locale.ENGLISH));
            listMessage.add(message);
        }
        if (!EStringUtil.isEmpty(userId) && !EStringUtil.isEmpty(password)) {
            try {
                String encryptedPassword = CryptUtil.encryptOneWay(password);
                MgrUser mgrUser = userDao.getById(userId);
                if (mgrUser != null) {
                    if (!LoginUtil.isUserExisted(encryptedPassword, mgrUser)) {
                        Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                                messageSource.getMessage(ErrorCode.E0003, null, Locale.ENGLISH));
                        listMessage.add(message);
                    } else {
                        MenuUtil menuUtil = new MenuUtil();
                        String loginToken = LoginUtil.generateToken(userId, DateUtil.getCurrentDate());
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUserId(userId);
                        userInfo.setUserName(mgrUser.getName());
                        userInfo.setLoginToken(loginToken);
                        session.setAttribute(Session.USER_INFO, userInfo);

                        CtlLogin ctlLogin = new CtlLogin();
                        ctlLogin.setSessionId(loginToken);
                        ctlLogin.setUserId(userId);
                        String loginTimeStr = DateUtil.getCurrentDate().toString();
                        ctlLogin.setLoginTime(loginTimeStr);
                        ctlLogin.setLogoutTime(null);
                        ctlLogin.setLastUpdateTime(loginTimeStr);
                        if (loginControlDao.insertOne(ctlLogin)) {
                            countAndUpdateLicenseHistory(userId);
                        }

                        Map<TemplateKey, TemplateAccessRight> templateAccessRightMap =
                                new HashMap<TemplateKey, TemplateAccessRight>();
                        List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();

                        for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                            templateAccessRightMap.putAll(
                                    templateAuthorityDao.getMixAuthority(userId, mgrWorkspace.getWorkspaceId()));
                        }

                        TemplateUtil.saveToSession(session, templateAccessRightMap);
                        Map<String, MenuAccessRight> menuAccessRightMap = menuAuthorityDao.getMixAuthority(userId);
                        session.setAttribute(Session.MENU_STRUCTURE, menuUtil.buildMenuTree(menuAccessRightMap));
                        MenuUtil.saveToSession(session, menuAccessRightMap);
                        session.setAttribute(Session.WORKSPACES, mgrWorkspaces);
                    }
                } else {
                    Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                            messageSource.getMessage(ErrorCode.E0003, null, Locale.ENGLISH));
                    listMessage.add(message);
                }
            } catch (Exception e) {
                throw new EarthException(e.getMessage());
            }
        }
        return listMessage;
    }

    public boolean logout(HttpSession session) throws EarthException {
        boolean isLogout = false;
        if (session.getAttribute(Session.USER_INFO) != null) {
            UserInfo userInfo = (UserInfo) session.getAttribute(Session.USER_INFO);
            String sessionId = userInfo.getLoginToken();
            String outTime = DateUtil.getCurrentDate().toString();
            try {
                loginControlDao.updateOutTime(sessionId, outTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String userId = userInfo.getUserId();
            countAndUpdateLicenseHistory(userId);
            session.invalidate();
            isLogout = true;
        }
        return isLogout;
    }

    public void countAndUpdateLicenseHistory(String userId) throws EarthException {
        try {
            List<StrCal> strCals = loginControlDao.getNumberOnlineUserByProfile(userId);
            String currentDate = DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);
            for (StrCal ls : strCals) {
                ls.setProcessTime(currentDate);
                licenseHistoryDao.add(Constant.EARTH_WORKSPACE_ID, ls);
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
    }

    public List<MgrUser> getAll() throws EarthException {
        try {
            return userDao.findAll(Constant.EARTH_WORKSPACE_ID, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> validate(MgrUser mgrUser, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (EStringUtil.isEmpty(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_BLANK, messageSource.getMessage(ErrorCode.E0001,
                        new String[] { ScreenItem.USER_ID }, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (!EStringUtil.checkAlphabet(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_SPECIAL, messageSource.getMessage(ErrorCode.E0007,
                        new String[] { ScreenItem.USER_ID }, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (EStringUtil.isEmpty(mgrUser.getName())) {
                Message message = new Message(MessageUser.NAME_BLANK,
                        messageSource.getMessage(ErrorCode.E0001, new String[] { ScreenItem.NAME }, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (!mgrUser.isChangePassword()) {
                    Message message = new Message(MessageUser.CHANGEPWD_BLANK,
                            messageSource.getMessage(ErrorCode.E0001,
                                    new String[] { ScreenItem.CHANGE_PASSWORD, ScreenItem.CREATE_USER },
                                    Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (EStringUtil.isEmpty(mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK, messageSource.getMessage(ErrorCode.E0001,
                            new String[] { ScreenItem.NEW_PASSWORD }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (EStringUtil.isEmpty(mgrUser.getConfirmPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK, messageSource.getMessage(ErrorCode.E0001,
                            new String[] { ScreenItem.CONFIRM_PASSWORD }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (!EStringUtil.contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_CORRECT, messageSource.getMessage(ErrorCode.E1008,
                            new String[] { ScreenItem.NEW_PASSWORD, ScreenItem.CONFIRM_PASSWORD }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                List<String> passwordValidate = passwordPolicy.validate(mgrUser.getPassword());
                if (passwordValidate != null && passwordValidate.size() > 0) {
                    listMessage = getMessagePasswordPolicy(passwordValidate);
                    return listMessage;
                }
                if (isExist(mgrUser.getUserId())) {
                    Message message = new Message(MessageUser.USR_EXIST, messageSource.getMessage(ErrorCode.E0005,
                            new String[] { mgrUser.getUserId(), ScreenItem.USER }, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }

            } else {
                if (mgrUser.isChangePassword()) {
                    if (EStringUtil.isEmpty(mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK, messageSource.getMessage(ErrorCode.E0001,
                                new String[] { ScreenItem.NEW_PASSWORD }, Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (EStringUtil.isEmpty(mgrUser.getConfirmPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK, messageSource.getMessage(ErrorCode.E0001,
                                new String[] { ScreenItem.CONFIRM_PASSWORD }, Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (!EStringUtil.contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_CORRECT,
                                messageSource.getMessage(ErrorCode.E1008,
                                        new String[] { ScreenItem.NEW_PASSWORD, ScreenItem.CONFIRM_PASSWORD },
                                        Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    List<String> passwordValidate = passwordPolicy.validate(mgrUser.getPassword());
                    if (passwordValidate != null && passwordValidate.size() > 0) {
                        listMessage = getMessagePasswordPolicy(passwordValidate);
                        return listMessage;
                    }
                }
            }
            return listMessage;
        } catch (Exception ex) {
            Message message = new Message(MessageUser.USR_BLANK,
                    messageSource.getMessage("E1009", new String[] { "" }, Locale.ENGLISH));
            listMessage.add(message);
            return listMessage;
        }
    }

    public boolean insertOne(MgrUser mgrUser) throws EarthException {

        try {
            mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
            mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
            mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
            return userDao.add(Constant.EARTH_WORKSPACE_ID, mgrUser) > 0;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean updateOne(MgrUser mgrUser) throws EarthException {
        try {
            mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
            if (mgrUser.getPassword() != null) {
                mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
                mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
            }
            mgrUser = userDao.updateOne(mgrUser);
            return (mgrUser != null);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean deleteList(List<String> userIds) throws EarthException {
        try {
            userProfileDao.deleteListByUserIds(userIds);
            strLogAccessDao.deleteListByUserIds(userIds);
            eventDao.deleteListByUserIds(userIds);
            loginControlDao.deleteListByUserIds(userIds);
            templateAuthorityDao.deleteListByUserIds(userIds);
            menuAuthorityDao.deleteListByUserIds(userIds);
            return userDao.deleteList(userIds);
        } catch (EarthException ex) {
            ex.printStackTrace();
            throw new EarthException(ex.getMessage());
        }
    }

    public Map<String, Object> getDetail(String userId) throws EarthException {
        try {
            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("mgrUser", userDao.getById(userId));
            detail.put("mgrProfiles", profileDao.getProfilesByUserId(userId));
            return detail;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    private boolean isExist(String userId) {
        try {
            return (userDao.getById(userId) != null);
        } catch (Exception ex) {
            return true;
        }
    }

    public List<CtlLogin> getAllMgrLogin(String workspaceId, Long offset, Long limit,
            OrderSpecifier<String> orderByColumn) throws EarthException {
        return loginControlDao.findAll(Constant.EARTH_WORKSPACE_ID, offset, limit, orderByColumn);
    }

    public CtlLogin getCtlLoginDetail(Map<Path<?>, Object> condition) throws EarthException {

        return loginControlDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
    }

    @Override
    public long deleteCtlLogin(Map<Path<?>, Object> condition) throws EarthException {

        return loginControlDao.delete(Constant.EARTH_WORKSPACE_ID, condition);
    }

    @Override
    public long deleteCtlLogins(List<Map<Path<?>, Object>> condition) throws EarthException {

        return loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, condition);
    }

    public long deleteAllCtlLogins() throws EarthException {
        try {
            return loginControlDao.deleteAll(Constant.EARTH_WORKSPACE_ID);
        } catch (EarthException ex) {
            ex.printStackTrace();
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public long addCtlLogin(CtlLogin login) throws EarthException {
        return loginControlDao.add(Constant.EARTH_WORKSPACE_ID, login);
    }

    @Override
    public long updateCtlLogin(Map<Path<?>, Object> condition, Map<Path<?>, Object> updateMap) throws EarthException {
        return loginControlDao.update(Constant.EARTH_WORKSPACE_ID, condition, updateMap);
    }

    // List message for password
    private List<Message> getMessagePasswordPolicy(List<String> passwordPolicys) {
        List<Message> messages = new ArrayList<Message>();
        for (String string : passwordPolicys) {
            Message message = new Message(EStringUtil.EMPTY, string);
            messages.add(message);
        }
        return messages;
    }

    @Override
    public List<CtlLogin> searchMgrLogin(String workspaceId, Predicate condition, Long offset, Long limit,
            OrderSpecifier<String> orderByColumn) throws EarthException {

        return loginControlDao.search(Constant.EARTH_WORKSPACE_ID, condition, offset, limit, orderByColumn);
    }
}