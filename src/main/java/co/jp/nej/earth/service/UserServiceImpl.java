package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.*;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.Constant.DatePattern;
import co.jp.nej.earth.model.constant.Constant.MessageCodeLogin;
import co.jp.nej.earth.model.constant.Constant.MessageUser;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

@Transactional
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
    private EvidenceLogDao evidenceLogDao;

    @Autowired
    private MenuAuthorityDao menuAuthorityDao;

    @Autowired
    private PasswordPolicy passwordPolicy;

    /**
     * @param userId   id of user
     * @param password password of user
     * @param session  HttpSession object
     * @return list message determined that user log in successfully or not
     */
    public List<Message> login(String userId, String password, HttpSession session) throws EarthException {
        List<Message> listMessage = new ArrayList<Message>();
        if (StringUtils.isEmpty(userId)) {
            Message message = new Message(MessageCodeLogin.USR_BLANK,
                    messageSource.getMessage("E0001", new String[]{"UserId"}, Locale.ENGLISH));
            listMessage.add(message);
        }
        if (StringUtils.isEmpty(password)) {
            Message message = new Message(MessageCodeLogin.PWD_BLANK,
                    messageSource.getMessage("E0001", new String[]{"Password"}, Locale.ENGLISH));
            listMessage.add(message);
        }

        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(password)) {
            try {
                String encryptedPassword = CryptUtil.encryptOneWay(password);
                MgrUser mgrUser = userDao.getById(userId);
                if (!LoginUtil.isUserExisted(encryptedPassword, mgrUser)) {
                    Message message = new Message(MessageCodeLogin.INVALID_LOGIN,
                            messageSource.getMessage("E0003", null, Locale.ENGLISH));
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
                    Map<TemplateKey, TemplateAccessRight> templateAccessRightMap = new HashMap<TemplateKey, TemplateAccessRight>();
                    /*
                     * List<MgrWorkspace> listMgrWorkspace =
                     * workspaceDao.getAll(); for (MgrWorkspace mgrWorkspace :
                     * listMgrWorkspace) { templateAccessRightMap
                     * .putAll(templateAuthorityDao.getMixAuthority(userId,
                     * mgrWorkspace.getWorkspaceId())); }
                     */
                    List<String> workspaceIds = new ArrayList<String>();
//                    workspaceIds.add("001");
                    workspaceIds.add("002");
                    workspaceIds.add("003");
                    for (String s : workspaceIds) {
                        templateAccessRightMap.putAll(templateAuthorityDao.getMixAuthority(userId, s));
                    }
                    TemplateUtil.saveToSession(session, templateAccessRightMap);
                    Map<String, MenuAccessRight> menuAccessRightMap = menuAuthorityDao.getMixAuthority(userId);
                    session.setAttribute(Session.MENU_STRUCTURE, menuUtil.buildMenuTree(menuAccessRightMap));
                    MenuUtil.saveToSession(session, menuAccessRightMap);
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
                licenseHistoryDao.insertOne(ls);
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
    }

    public List<MgrUser> getAll() {
        try {
            return userDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> validate(MgrUser mgrUser, boolean insert) {
        List<Message> listMessage = new ArrayList<Message>();
        try {
            if (StringUtils.isEmpty(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_BLANK,
                        messageSource.getMessage("E0001", new String[]{"UserId"}, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (!EStringUtil.checkAlphabet(mgrUser.getUserId())) {
                Message message = new Message(MessageUser.USR_SPECIAL,
                        messageSource.getMessage("E0007", new String[]{"UserId"}, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (StringUtils.isEmpty(mgrUser.getName())) {
                Message message = new Message(MessageUser.NAME_BLANK,
                        messageSource.getMessage("E0001", new String[]{"Name"}, Locale.ENGLISH));
                listMessage.add(message);
                return listMessage;
            }
            if (insert) {
                if (!mgrUser.isChangePassword()) {
                    Message message = new Message(MessageUser.CHANGEPWD_BLANK, messageSource.getMessage("E0012",
                            new String[]{"Change Password", "Create User"}, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (StringUtils.isEmpty(mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK,
                            messageSource.getMessage("E0001", new String[]{"New Password"}, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (StringUtils.isEmpty(mgrUser.getConfirmPassword())) {
                    Message message = new Message(MessageUser.PWD_BLANK,
                            messageSource.getMessage("E0001", new String[]{"Confirm Password"}, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                if (!EStringUtil.Contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                    Message message = new Message(MessageUser.PWD_CORRECT, messageSource.getMessage("E1008",
                            new String[]{"New Password", "Confirm Password"}, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }
                List<String> passwordValidate = passwordPolicy.validate(mgrUser.getPassword());
                if(passwordValidate!=null && passwordValidate.size()>0){
                    listMessage=getMessagePasswordPolicy(passwordValidate);
                    return listMessage;
                }
                if (isExist(mgrUser.getUserId())) {
                    Message message = new Message(MessageUser.USR_EXIST, messageSource.getMessage("E0005",
                            new String[]{mgrUser.getUserId(), "User"}, Locale.ENGLISH));
                    listMessage.add(message);
                    return listMessage;
                }

            } else {
                if (mgrUser.isChangePassword()) {
                    if (StringUtils.isEmpty(mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK,
                                messageSource.getMessage("E0001", new String[]{"New Password"}, Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (StringUtils.isEmpty(mgrUser.getConfirmPassword())) {
                        Message message = new Message(MessageUser.PWD_BLANK,
                                messageSource.getMessage("E0001", new String[]{"Confirm Password"}, Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    if (!EStringUtil.Contains(mgrUser.getConfirmPassword(), mgrUser.getPassword())) {
                        Message message = new Message(MessageUser.PWD_CORRECT, messageSource.getMessage("E1008",
                                new String[]{"New Password", "Confirm Password"}, Locale.ENGLISH));
                        listMessage.add(message);
                        return listMessage;
                    }
                    List<String> passwordValidate = passwordPolicy.validate(mgrUser.getPassword());
                    if(passwordValidate!=null && passwordValidate.size()>0){
                        listMessage=getMessagePasswordPolicy(passwordValidate);
                        return listMessage;
                    }
                }
            }
            return listMessage;
        } catch (Exception ex) {
            Message message = new Message(MessageUser.USR_BLANK,
                    messageSource.getMessage("E1009", new String[]{""}, Locale.ENGLISH));
            listMessage.add(message);
            return listMessage;
        }
    }

    public boolean insertOne(MgrUser mgrUser) throws EarthException {
        try {
            mgrUser.setLastUpdateTime(DateUtil.getCurrentDate(DatePattern.DATE_FORMAT_YYYY_MM_DD));
            mgrUser.setPassword(CryptUtil.encryptOneWay(mgrUser.getPassword()));
            mgrUser.setConfirmPassword(CryptUtil.encryptOneWay(mgrUser.getConfirmPassword()));
            mgrUser = userDao.insertOne(mgrUser);
            return (mgrUser != null);
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

    public boolean deleteList(List<String> userIds) {
        try {
            userProfileDao.deleteListByUserIds(userIds);
            evidenceLogDao.deleteListByUserIds(userIds);
            eventDao.deleteListByUserIds(userIds);
            loginControlDao.deleteListByUserIds(userIds);
            templateAuthorityDao.deleteListByUserIds(userIds);
            menuAuthorityDao.deleteListByUserIds(userIds);
            return userDao.deleteList(userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    private List<Message> getMessagePasswordPolicy(List<String> passwordPolicys){
        List<Message> messages =new ArrayList<Message>();
        for(String string : passwordPolicys){
            Message message = new Message("",string);
            messages.add(message);
        }
        return messages;
    }
}
