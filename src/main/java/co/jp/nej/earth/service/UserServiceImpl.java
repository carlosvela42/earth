package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

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
import co.jp.nej.earth.model.enums.Channel;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.model.sql.QMgrUser;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.CryptUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.LoginUtil;
import co.jp.nej.earth.util.MenuUtil;
import co.jp.nej.earth.util.PasswordPolicy;
import co.jp.nej.earth.util.TemplateUtil;

@Service
public class UserServiceImpl extends BaseService implements UserService {

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
            throw new EarthException(ex);
        }
    }

    /**
     * @param userId   id of user
     * @param password password of user
     * @param session  HttpSession object
     * @return list message determined that user log in successfully or not
     */
    public List<Message> login(String userId, String password, HttpSession session, int channel) throws EarthException {
        List<Message> listMessage = new ArrayList<Message>();
        if (!EStringUtil.isEmpty(userId) && !EStringUtil.checkAlphabet(userId)) {
            Message message = new Message(MessageUser.USR_SPECIAL,
                    eMessageResource.get(ErrorCode.E0004, new String[] { ScreenItem.USER_ID }));
            listMessage.add(message);
            return listMessage;
        }

        if (!EStringUtil.isEmpty(userId)) {
            TransactionManager transactionManager = null;
            try {
                transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
                String encryptedPassword = CryptUtil.encryptOneWay(password);
                MgrUser mgrUser = userDao.getById(userId);
                if (mgrUser != null) {
                    if (!LoginUtil.isUserExisted(encryptedPassword, mgrUser)) {
                        Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                                eMessageResource.get(ErrorCode.E0002,null));
                        listMessage.add(message);
                    } else {
                        String loginToken = LoginUtil.generateToken(userId, DateUtil.getCurrentDate());
                        session.setAttribute(Session.USER_INFO, new UserInfo(userId, mgrUser.getName(), loginToken));

                        CtlLogin ctlLogin = new CtlLogin(loginToken, userId, DateUtil.getCurrentDateString(), null);
                        loginControlDao.add(Constant.EARTH_WORKSPACE_ID, ctlLogin);
                        countAndUpdateLicenseHistory(userId);

                        // Save All Workspaces information into session.
                        List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
                        session.setAttribute(Session.WORKSPACES, mgrWorkspaces);

                        // Save All templates access right into session.
                        TemplateAccessRight templateAccessRights = getTemplatesAccessRightOfAllWorkspaces(userId,
                                mgrWorkspaces);
                        TemplateUtil.saveToSession(session, templateAccessRights);

                        // Save All Menus Access right into session.
                        Map<String, MenuAccessRight> menuAccessRightMap = menuAuthorityDao.getMixAuthority(userId);
                        if (channel == Channel.INTERNAL.getValue()) {
                            MenuUtil.saveToSession(session, menuAccessRightMap);
                            session.setAttribute(Session.MENU_STRUCTURE, MenuUtil.buildMenuTree(menuAccessRightMap));
                        }
                    }
                } else {
                    Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                            eMessageResource.get(ErrorCode.E0002, null));
                    listMessage.add(message);
                }
            } catch (Exception e) {
                if (transactionManager != null) {
                    transactionManager.getManager().rollback(transactionManager.getTxStatus());
                }
                throw new EarthException(e);
            }

            if (listMessage.size() > 0) {
                if (transactionManager != null) {
                    transactionManager.getManager().rollback(transactionManager.getTxStatus());
                }
            } else {
                if (transactionManager != null) {
                    transactionManager.getManager().commit(transactionManager.getTxStatus());
                }
            }
        }
        return listMessage;
    }

    /**
     * @param userId   id of user
     * @param password password of user
     * @param session  HttpSession object
     * @return list message determined that user log in successfully or not
     */
    public List<Message> loginBatch(String userId, String password) throws EarthException {
        List<Message> listMessage = new ArrayList<Message>();
        if (!EStringUtil.isEmpty(userId) && !EStringUtil.checkAlphabet(userId)) {
            Message message = new Message(MessageUser.USR_SPECIAL,
                eMessageResource.get(ErrorCode.E0004, new String[]{ScreenItem.USER_ID}));
            listMessage.add(message);
        }

        if (!EStringUtil.isEmpty(userId)) {
            TransactionManager transactionManager = null;
            try {
                transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
                String encryptedPassword = CryptUtil.encryptOneWay(password);
                MgrUser mgrUser = userDao.getById(userId);
                if (mgrUser != null) {
                    if (!LoginUtil.isUserExisted(encryptedPassword, mgrUser)) {
                        Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                            eMessageResource.get(ErrorCode.E0002, null));
                        listMessage.add(message);
                    } else {
                        String loginToken = LoginUtil.generateToken(userId, DateUtil.getCurrentDate());

                        CtlLogin ctlLogin = new CtlLogin(loginToken, userId, DateUtil.getCurrentDateString(), null);
                        loginControlDao.add(Constant.EARTH_WORKSPACE_ID, ctlLogin);
                        countAndUpdateLicenseHistory(userId);

                    }
                } else {
                    Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                        eMessageResource.get(ErrorCode.E0002, null));
                    listMessage.add(message);
                }
            } catch (Exception e) {
                if (transactionManager != null) {
                    transactionManager.getManager().rollback(transactionManager.getTxStatus());
                }
                throw new EarthException(e);
            }

            if (listMessage.size() > 0) {
                if (transactionManager != null) {
                    transactionManager.getManager().rollback(transactionManager.getTxStatus());
                }
            } else {
                if (transactionManager != null) {
                    transactionManager.getManager().commit(transactionManager.getTxStatus());
                }
            }
        }
        return listMessage;
    }

    private TemplateAccessRight getTemplatesAccessRightOfAllWorkspaces(String userId,
            List<MgrWorkspace> mgrWorkspaces) {
        TemplateAccessRight templateAccessRights = new TemplateAccessRight();

        TransactionManager transactionMgr = null;
        for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
            try {
                transactionMgr = new TransactionManager(mgrWorkspace.getWorkspaceId());
                templateAccessRights.putAll(
                        templateAuthorityDao.getMixAuthority(userId, mgrWorkspace.getWorkspaceId()));
                transactionMgr.getManager().commit(transactionMgr.getTxStatus());
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                LOG.error("EARTH: Reason could be can not connect with workspace with Id: "
                        + mgrWorkspace.getWorkspaceId()
                        + ". If so, consider removing this workspace record from database.");
                if (transactionMgr != null) {
                    transactionMgr.getManager().rollback(transactionMgr.getTxStatus());
                }
            }
        }

        return templateAccessRights;
    }

    public boolean logout(HttpSession session) throws EarthException {
        return (boolean) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            if (LoginUtil.isLogin(session)) {
                UserInfo userInfo = (UserInfo) session.getAttribute(Session.USER_INFO);
                String sessionId = userInfo.getLoginToken();

                loginControlDao.updateOutTime(sessionId, DateUtil.getCurrentDateString());
                String userId = userInfo.getUserId();
                countAndUpdateLicenseHistory(userId);
                session.invalidate();
                return true;
            }

            return false;
        });
    }

    private void countAndUpdateLicenseHistory(String userId) throws EarthException {
        try {
            List<StrCal> strCals = loginControlDao.getNumberOnlineUserByProfile(userId);
            List<StrCal> strCalEntireSystem = loginControlDao.getLicenceOfEntireSystem();
            if (strCalEntireSystem != null && strCalEntireSystem.size() > 0) {
                strCals.addAll(strCalEntireSystem);
            }
            String currentDate = DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);
            for (StrCal ls : strCals) {
                ls.setProcessTime(currentDate);
                licenseHistoryDao.add(Constant.EARTH_WORKSPACE_ID, ls);
            }
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    public List<MgrUser> getAll() throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        List<MgrUser> mgrUsers = null;
        try {
            mgrUsers = userDao.findAll(Constant.EARTH_WORKSPACE_ID, null, null, null, null);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            throw new EarthException(ex);
        }
        return mgrUsers;
    }

    public List<Message> validate(MgrUser mgrUser, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (!EStringUtil.checkJapanese(mgrUser.getName())) {
                Message message = new Message(MessageUser.NAME_SPECIAL,
                    eMessageResource.get(ErrorCode.E0004, new String[]{ScreenItem.NAME}));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (isExist(mgrUser.getUserId())) {
                    Message message = new Message(MessageUser.USR_EXIST, eMessageResource.get(ErrorCode.E0003,
                        new String[]{mgrUser.getUserId(), ScreenItem.USER}));
                    listMessage.add(message);
                    return listMessage;
                }
            }
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
            return listMessage;
        } catch (Exception ex) {
            Message message = new Message(MessageUser.USR_BLANK, eMessageResource.get("E1009", new String[]{""}));
            listMessage.add(message);
            return listMessage;
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

    public boolean insertOne(MgrUser mgrUser) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
            mgrUser.setPassword(CryptUtil.encryptOneWay(!EStringUtil.isEmpty(mgrUser.getPassword()) ? mgrUser
                .getPassword() : ""));

            mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(!EStringUtil.isEmpty(mgrUser.getConfirmPassword())
                ? mgrUser.getConfirmPassword() : ""));
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

        List<TransactionManager> transactionManagers = new ArrayList<TransactionManager>();
        transactionManagers.add(new TransactionManager(Constant.EARTH_WORKSPACE_ID));
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                transactionManagers.add(new TransactionManager(mgrWorkspace.getWorkspaceId()));

                List<Map<Path<?>, Object>> conditionEvents = new ArrayList<>();
                for (String userId : userIds) {
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qCtlEvent.userId, userId);
                    conditionEvents.add(condition);
                }
                eventDao.deleteList(mgrWorkspace.getWorkspaceId(), conditionEvents);

                List<Map<Path<?>, Object>> conditionLogAccesses = new ArrayList<>();
                for (String userId : userIds) {
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qStrLogAccess.userId, userId);
                    conditionLogAccesses.add(condition);
                }
                strLogAccessDao.deleteList(mgrWorkspace.getWorkspaceId(), conditionLogAccesses);

                templateAuthorityDao.deleteListByUserIds(mgrWorkspace.getWorkspaceId(), userIds);
            }
            menuAuthorityDao.deleteListByUserIds(userIds);

            List<Map<Path<?>, Object>> conditionCtlLogins = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlLogin.userId, userId);
                conditionCtlLogins.add(condition);
            }
            loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditionCtlLogins);

            List<Map<Path<?>, Object>> conditionUserProfiles = new ArrayList<>();
            for (String userId : userIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrUserProfile.userId, userId);
                conditionUserProfiles.add(condition);
            }
            userProfileDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditionUserProfiles);

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
            throw new EarthException(ex);
        }
    }

    public CtlLogin getCtlLoginDetail(Map<Path<?>, Object> condition) throws EarthException {
        return ConversionUtil.castObject(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
        }), CtlLogin.class);
    }

    public long deleteCtlLogin(Map<Path<?>, Object> condition) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.delete(Constant.EARTH_WORKSPACE_ID, condition);
        });
    }

    public long deleteCtlLogins(List<Map<Path<?>, Object>> condition) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, condition);
        });
    }

    public long deleteAllCtlLogins() throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.deleteAll(Constant.EARTH_WORKSPACE_ID);
        });
    }

    @Override
    public long addCtlLogin(CtlLogin login) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.add(Constant.EARTH_WORKSPACE_ID, login);
        });
    }

    @Override
    public long updateCtlLogin(Map<Path<?>, Object> condition, Map<Path<?>, Object> updateMap) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.update(Constant.EARTH_WORKSPACE_ID, condition, updateMap);
        });
    }

    // List message for password.
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
                                         List<OrderSpecifier<?>> orderBys) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return loginControlDao.search(Constant.EARTH_WORKSPACE_ID, condition, offset, limit, orderBys, null);
        }), CtlLogin.class);
    }
}
