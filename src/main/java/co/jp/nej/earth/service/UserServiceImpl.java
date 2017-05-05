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
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.TransactionManager;
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
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.model.sql.QCtlMenu;
import co.jp.nej.earth.model.sql.QCtlTemplate;
import co.jp.nej.earth.model.sql.QMgrUser;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import co.jp.nej.earth.util.CryptUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.LoginUtil;
import co.jp.nej.earth.util.MenuUtil;
import co.jp.nej.earth.util.PasswordPolicy;
import co.jp.nej.earth.util.TemplateUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginControlDao loginControlDao;

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Autowired
    private EMessageResource eMessageResource;

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

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean insertList(String workspaceId, List<MgrUser> mgrUsers) throws EarthException {
        PlatformTransactionManager manager = ConnectionManager.getTransactionManager(workspaceId);
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = manager.getTransaction(txDef);
        try {
            for (MgrUser mgrUser : mgrUsers) {
                mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
                mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
                mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
                long insert = userDao.add(workspaceId, mgrUser);
                if (insert == 0) {
                    manager.rollback(txStatus);
                    throw new EarthException("Delete unsuccessfully!");
                }
            }
            manager.commit(txStatus);
            return true;
        } catch (Exception ex) {
            manager.rollback(txStatus);
            throw new EarthException(ex.getMessage());
        }
    }

    /**
     * @param userId   id of user
     * @param password password of user
     * @param session  HttpSession object
     * @return list message determined that user log in successfully or not
     */
    @Transactional
    public List<Message> login(String userId, String password, HttpSession session) throws EarthException {
        List<Message> listMessage = new ArrayList<Message>();
        if (EStringUtil.isEmpty(userId)) {
            Message message = new Message(MessageCodeLogin.USR_BLANK,
                    eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.USER_ID}));
            listMessage.add(message);
        }

        if (EStringUtil.isEmpty(password)) {
            Message message = new Message(MessageCodeLogin.PWD_BLANK,
                    eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.PASSWORD}));
            listMessage.add(message);
        }

        if (!EStringUtil.isEmpty(userId) && !EStringUtil.checkAlphabet(userId)) {
            Message message = new Message(MessageUser.USR_SPECIAL,
                    eMessageResource.get(ErrorCode.E0007, new String[]{ScreenItem.USER_ID}));
            listMessage.add(message);
        }

        if (!EStringUtil.isEmpty(userId) && !EStringUtil.isEmpty(password)) {
            List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
            TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
            transactionManagers.add(transactionManager);
            try {
                String encryptedPassword = CryptUtil.encryptOneWay(password);
                MgrUser mgrUser = userDao.getById(userId);
                if (mgrUser != null) {
                    if (!LoginUtil.isUserExisted(encryptedPassword, mgrUser)) {
                        Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                                eMessageResource.get(ErrorCode.E0003, null));
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

                        Map<TemplateKey, TemplateAccessRight> templateAccessRightMap
                                = new HashMap<TemplateKey, TemplateAccessRight>();
                        List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();

                        for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                            transactionManager = new TransactionManager(mgrWorkspace.getWorkspaceId());
                            transactionManagers.add(transactionManager);
                            templateAccessRightMap.putAll(
                                    templateAuthorityDao.getMixAuthority(userId, mgrWorkspace.getWorkspaceId()));
                        }

                        TemplateUtil.saveToSession(session, templateAccessRightMap);
                        Map<String, MenuAccessRight> menuAccessRightMap = menuAuthorityDao.getMixAuthority(userId);
                        session.setAttribute(Session.MENU_STRUCTURE, menuUtil.buildMenuTree(menuAccessRightMap));
                        MenuUtil.saveToSession(session, menuAccessRightMap);
                        session.setAttribute(Session.WORKSPACES, mgrWorkspaces);

                        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                            TransactionManager transactionManager1 = transactionManagers.get(i);
                            transactionManager1.getManager().commit(transactionManager1.getTxStatus());
                        }
                    }
                } else {
                    Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                            eMessageResource.get(ErrorCode.E0003, null));
                    listMessage.add(message);
                }
            } catch (Exception e) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    TransactionManager transactionManager1 = transactionManagers.get(i);
                    transactionManager1.getManager().rollback(transactionManager1.getTxStatus());
                }
                throw new EarthException(e.getMessage());
            }
        }

        return listMessage;
    }

    @Transactional
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
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        List<MgrUser> mgrUsers = null;
        try {
            mgrUsers = userDao.findAll(Constant.EARTH_WORKSPACE_ID, null, null, null);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            ex.printStackTrace();
            throw new EarthException(ex.getMessage());
        }
        return mgrUsers;
    }

    public List<Message> validate(MgrUser mgrUser, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (EStringUtil.isEmpty(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_BLANK,
                        eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.USER_ID}));

                listMessage.add(message);
                return listMessage;
            }
            if (!EStringUtil.checkAlphabet(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_SPECIAL,
                        eMessageResource.get(ErrorCode.E0007, new String[]{ScreenItem.USER_ID}));
                listMessage.add(message);
                return listMessage;
            }
            if (EStringUtil.isEmpty(mgrUser.getName())) {
                Message message = new Message(MessageUser.NAME_BLANK,
                        eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.NAME}));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (!mgrUser.isChangePassword()) {
                    Message message = new Message(MessageUser.CHANGEPWD_BLANK, eMessageResource.get(ErrorCode.E0001,
                            new String[]{ScreenItem.CHANGE_PASSWORD, ScreenItem.CREATE_USER}));
                    listMessage.add(message);
                    return listMessage;
                }
                if (EStringUtil.isEmpty(mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK,
                            eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.NEW_PASSWORD}));
                    listMessage.add(message);
                    return listMessage;
                }
                if (EStringUtil.isEmpty(mgrUser.getConfirmPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK,
                            eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.CONFIRM_PASSWORD}));
                    listMessage.add(message);
                    return listMessage;
                }
                if (!EStringUtil.contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_CORRECT, eMessageResource.get(ErrorCode.E1008,
                            new String[]{ScreenItem.NEW_PASSWORD, ScreenItem.CONFIRM_PASSWORD}));
                    listMessage.add(message);
                    return listMessage;
                }
                List<String> passwordValidate = passwordPolicy.validate(mgrUser.getPassword());
                if (passwordValidate != null && passwordValidate.size() > 0) {
                    listMessage = getMessagePasswordPolicy(passwordValidate);
                    return listMessage;
                }
                if (isExist(mgrUser.getUserId())) {
                    Message message = new Message(MessageUser.USR_EXIST, eMessageResource.get(ErrorCode.E0005,
                            new String[]{mgrUser.getUserId(), ScreenItem.USER}));
                    listMessage.add(message);
                    return listMessage;
                }

            } else {
                if (mgrUser.isChangePassword()) {
                    if (EStringUtil.isEmpty(mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK,
                                eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.NEW_PASSWORD}));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (EStringUtil.isEmpty(mgrUser.getConfirmPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK,
                                eMessageResource.get(ErrorCode.E0001, new String[]{ScreenItem.CONFIRM_PASSWORD}));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (!EStringUtil.contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_CORRECT, eMessageResource.get(ErrorCode.E1008,
                                new String[]{ScreenItem.NEW_PASSWORD, ScreenItem.CONFIRM_PASSWORD}));
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
            Message message = new Message(MessageUser.USR_BLANK, eMessageResource.get("E1009", new String[]{""}));
            listMessage.add(message);
            return listMessage;
        }
    }

    public boolean insertOne(MgrUser mgrUser) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
            mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
            mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
            long insert = userDao.add(Constant.EARTH_WORKSPACE_ID, mgrUser);
            if (insert == 0) {
                throw new EarthException("Insert unsuccessfully!");
            }

        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            return false;
        }
        transactionManager.getManager().commit(transactionManager.getTxStatus());
        return true;
    }

    public boolean updateOne(MgrUser mgrUser) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
        try {
            if (mgrUser.getPassword() != null) {
                mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
                mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
            }
            if (userDao.updateOne(mgrUser) <= 0) {
                throw new EarthException("Update unsuccessfully!");
            }
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            return false;
        }
        transactionManager.getManager().commit(transactionManager.getTxStatus());
        return true;
    }

    public boolean deleteList(List<String> userIds) throws EarthException {

        QCtlEvent qCtlEvent = QCtlEvent.newInstance();
        QMgrUser qMgrUser = QMgrUser.newInstance();
        QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
        QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
        QCtlMenu qCtlMenu = QCtlMenu.newInstance();

        List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
        transactionManagers.add(new TransactionManager(Constant.EARTH_WORKSPACE_ID));
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            BooleanBuilder conditionSearch = null;
            Predicate pre1 = null;
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                transactionManagers.add(new TransactionManager(mgrWorkspace.getWorkspaceId()));

                List<Map<Path<?>, Object>> conditionEvents = new ArrayList<>();
                for (String userId : userIds) {
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qCtlEvent.userId, userId);
                    conditionEvents.add(condition);
                }
                eventDao.deleteList(mgrWorkspace.getWorkspaceId(), conditionEvents);

                conditionSearch = new BooleanBuilder();
                pre1 = qCtlEvent.userId.in(userIds);
                conditionSearch.and(pre1);
                if (eventDao.search(mgrWorkspace.getWorkspaceId(), conditionSearch, null, null,
                        null).size() > 0) {
                    throw new EarthException("Delete cltEvent of User unsuccessfully");
                }

                List<Map<Path<?>, Object>> conditionLogAccesses = new ArrayList<>();
                for (String userId : userIds) {
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qStrLogAccess.userId, userId);
                    conditionLogAccesses.add(condition);
                }
                strLogAccessDao.deleteList(mgrWorkspace.getWorkspaceId(), conditionLogAccesses);

                conditionSearch = new BooleanBuilder();
                pre1 = qStrLogAccess.userId.in(userIds);
                conditionSearch.and(pre1);
                if (strLogAccessDao.search(mgrWorkspace.getWorkspaceId(), conditionSearch, null,
                        null, null).size() > 0) {
                    throw new EarthException("Delete StrLogAccessDao of User unsuccessfully");
                }

                templateAuthorityDao.deleteListByUserIds(mgrWorkspace.getWorkspaceId(), userIds);
                conditionSearch = new BooleanBuilder();
                pre1 = qCtlTemplate.userId.in(userIds);
                conditionSearch.and(pre1);
                if (templateAuthorityDao.search(mgrWorkspace.getWorkspaceId(), conditionSearch,
                        null, null, null).size() > 0) {
                    throw new EarthException("Delete CtlTemplate of User unsuccessfully");
                }
            }
            menuAuthorityDao.deleteListByUserIds(userIds);

            BooleanBuilder conditionSearchMenu = new BooleanBuilder();
            Predicate pre1Menu = qCtlMenu.userId.in(userIds);
            conditionSearchMenu.and(pre1Menu);
            if (menuAuthorityDao.search(Constant.EARTH_WORKSPACE_ID, conditionSearchMenu, null,
                    null, null).size() > 0) {
                throw new EarthException("Delete MenuAuthority of User unsuccessfully");
            }

            List<Map<Path<?>, Object>> conditionCtlLogins = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlLogin.userId, userId);
                conditionCtlLogins.add(condition);
            }
            loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditionCtlLogins);

            conditionSearchMenu = new BooleanBuilder();
            pre1Menu = qCtlLogin.userId.in(userIds);
            conditionSearchMenu.and(pre1Menu);
            if (loginControlDao.search(Constant.EARTH_WORKSPACE_ID, conditionSearchMenu, null,
                    null, null).size() > 0) {
                throw new EarthException("Delete CtlLogin of User unsuccessfully");
            }

            List<Map<Path<?>, Object>> conditionUserProfiles = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrUserProfile.userId, userId);
                conditionUserProfiles.add(condition);
            }
            userProfileDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditionUserProfiles);

            conditionSearchMenu = new BooleanBuilder();
            pre1Menu = qMgrUserProfile.userId.in(userIds);
            conditionSearchMenu.and(pre1Menu);
            if (userProfileDao.search(Constant.EARTH_WORKSPACE_ID, conditionSearchMenu, null,
                    null, null).size() > 0) {
                throw new EarthException("Delete MgrUserProfile of User unsuccessfully");
            }

            List<Map<Path<?>, Object>> conditions = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrUser.userId, userId);
                conditions.add(condition);
            }

            if (userDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditions) != userIds.size()) {
                throw new EarthException("Delete User fail");
            }

        } catch (EarthException ex) {
            // Rollback transactions.
            if (transactionManagers != null && transactionManagers.size() > 0) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    TransactionManager transactionManager1 = transactionManagers.get(i);
                    transactionManager1.getManager().rollback(transactionManager1.getTxStatus());
                }
            }
            ex.printStackTrace();
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

    public Map<String, Object> getDetail(String userId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("mgrUser", userDao.getById(userId));
            detail.put("mgrProfiles", profileDao.getProfilesByUserId(userId));
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return detail;
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            throw new EarthException(ex.getMessage());
        }
    }

    private boolean isExist(String userId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            MgrUser mgrUser = userDao.getById(userId);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return (mgrUser != null);
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            return true;
        }
    }

    @Transactional
    public List<CtlLogin> getAllMgrLogin(String workspaceId, Long offset, Long limit,
                                         OrderSpecifier<String> orderByColumn) throws EarthException {
        return loginControlDao.findAll(Constant.EARTH_WORKSPACE_ID, offset, limit, orderByColumn);
    }

    @Transactional
    public CtlLogin getCtlLoginDetail(Map<Path<?>, Object> condition) throws EarthException {

        return loginControlDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
    }

    @Transactional
    public long deleteCtlLogin(Map<Path<?>, Object> condition) throws EarthException {

        return loginControlDao.delete(Constant.EARTH_WORKSPACE_ID, condition);
    }

    @Transactional
    public long deleteCtlLogins(List<Map<Path<?>, Object>> condition) throws EarthException {

        return loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, condition);
    }

    @Transactional
    public long deleteAllCtlLogins() throws EarthException {
        try {
            return loginControlDao.deleteAll(Constant.EARTH_WORKSPACE_ID);
        } catch (EarthException ex) {
            ex.printStackTrace();
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public long addCtlLogin(CtlLogin login) throws EarthException {
        return loginControlDao.add(Constant.EARTH_WORKSPACE_ID, login);
    }

    @Override
    @Transactional
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
    @Transactional
    public List<CtlLogin> searchMgrLogin(String workspaceId, Predicate condition, Long offset, Long limit,
                                         OrderSpecifier<String> orderByColumn) throws EarthException {

        return loginControlDao.search(Constant.EARTH_WORKSPACE_ID, condition, offset, limit, orderByColumn);
    }
}