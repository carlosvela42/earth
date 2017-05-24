package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.dao.MenuAuthorityDao;
import co.jp.nej.earth.dao.MenuDao;
import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.TemplateAuthorityDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.dao.TestTemplateAuthorityDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.dao.UserProfileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MenuAccessRight;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlMenu;
import co.jp.nej.earth.model.entity.CtlTemplate;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.entity.MgrMenuP;
import co.jp.nej.earth.model.entity.MgrMenuU;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.sql.QCtlMenu;
import co.jp.nej.earth.model.sql.QMgrMenu;
import co.jp.nej.earth.model.sql.QMgrProfile;
import co.jp.nej.earth.model.sql.QMgrUserProfile;
import co.jp.nej.earth.util.ConversionUtil;
import com.querydsl.core.BooleanBuilder;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class PreparingDataService extends BaseService {

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;
    @Autowired
    private TemplateAuthorityDao templateAuthorityDao;

    @Autowired
    private MenuAuthorityDao menuAuthorityDao;

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestTemplateAuthorityDao testTemplateAuthorityDao;

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private MenuDao menuDao;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceImpl.class);

    public void insertCtlTemplate(String workspaceId, CtlTemplate ctlTemplate) {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            templateAuthorityDao.add(workspaceId, ctlTemplate);
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public Map<TemplateKey, TemplateAccessRight> getMixAuthorityTemplate(String userId, String workspaceId)
            throws EarthException {
        return templateAuthorityDao.getMixAuthority("admin", workspaceId);
    }

    public Map<String, MenuAccessRight> getMixAuthorityMenu(String userId) throws EarthException {
        return menuAuthorityDao.getMixAuthority(userId);
    }

    // MinhTV Test UserService
    public boolean deleteUserProfile(List<String> userIds) throws EarthException {
        QMgrUserProfile qMgrUserProfile = QMgrUserProfile.newInstance();
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            userProfileDao.deleteListByUserIds(userIds);
            BooleanBuilder condition = new BooleanBuilder();
            Predicate pre1 = qMgrUserProfile.userId.in(userIds);
            condition.and(pre1);
            if (userProfileDao.search(Constant.EARTH_WORKSPACE_ID, pre1).size() > 0) {
                throw new EarthException("Delete UserProfile fail");
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return true;
        } catch (EarthException ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteListUsers(List<String> userIds) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        boolean del = false;
        try {
            del = userDao.deleteList(userIds) == userIds.size();

        } catch (EarthException ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            return false;
        }
        if (del) {
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } else {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
        }
        return del;

    }

    public boolean deleteListProfiles(List<String> profileIds) throws EarthException {
        QMgrProfile qMgrProfile = QMgrProfile.newInstance();
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        try {
            profileDao.deleteList(profileIds);
            BooleanBuilder condition = new BooleanBuilder();
            Predicate pre1 = qMgrProfile.profileId.in(profileIds);
            condition.and(pre1);
            if (profileDao.search(Constant.EARTH_WORKSPACE_ID, pre1).size() > 0) {
                throw new EarthException("Delete Profile fail");
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return true;
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public void insertMgrTemplate(String workspaceId, MgrTemplate mgrTemplate) throws EarthException {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            long count = templateDao.add(workspaceId, mgrTemplate);
            if (count < 0) {
                throw new EarthException("Insert MgrTemplate unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public void deleteOneCtlTemplate(String workspaceId) {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            long count = templateAuthorityDao.deleteAll(workspaceId);
            if (count < 0) {
                throw new EarthException("Delete CtlTemplate unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public void deleteAllCtlTemplate() {
        deleteOneCtlTemplate("001");
        deleteOneCtlTemplate("002");
        deleteOneCtlTemplate("003");
    }

    public void deleteAllMgrTemplate() {
        deleteOneMgrTemplate("001");
        deleteOneMgrTemplate("002");
        deleteOneMgrTemplate("003");
    }

    public void deleteOneMgrTemplate(String workspaceId) {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            long count = templateDao.deleteAll(workspaceId);
            if (count < 0) {
                throw new EarthException("Delete MgrTemplate unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public void insertLicenseHistory(StrCal strCal) throws EarthException {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(Constant.EARTH_WORKSPACE_ID);
            txStatus = transactionManager.getTransaction(txDef);
            long count = licenseHistoryDao.add(Constant.EARTH_WORKSPACE_ID, strCal);
            if (count < 0) {
                throw new EarthException("Insert StrCal unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public void deleteAllLicenseHistory() {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            transactionManager = ConnectionManager.getTransactionManager(Constant.EARTH_WORKSPACE_ID);
            txStatus = transactionManager.getTransaction(txDef);
            long count = licenseHistoryDao.deleteAll(Constant.EARTH_WORKSPACE_ID);
            if (count < 0) {
                throw new EarthException("Delete StrCal unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException e) {
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
        }
    }

    public long deleteMgrTemplateU(TemplateKey templateKey) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(templateKey.getWorkspaceId());
        long numOfRecordDeleted = 0L;
        try {
            numOfRecordDeleted = templateAuthorityDao.deleteAllUserAuthority(templateKey);
            if (templateAuthorityDao.getUserAuthority(templateKey).size() > 0) {
                throw new EarthException("Delete UserAuthority failed");
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
        }
        return numOfRecordDeleted;
    }

    public long deleteMgrTemplateP(TemplateKey templateKey) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(templateKey.getWorkspaceId());
        long numOfRecordDeleted = 0L;
        try {
            numOfRecordDeleted = templateAuthorityDao.deleteAllProfileAuthority(templateKey);
            if (templateAuthorityDao.getProfileAuthority(templateKey).size() > 0) {
                throw new EarthException("Delete ProfileAuthority fail");
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
        }
        return numOfRecordDeleted;
    }

    public long deleteCtlTemplate(TemplateKey templateKey) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(templateKey.getWorkspaceId());
        long numOfRecordDeleted = 0L;
        try {
            numOfRecordDeleted = templateAuthorityDao.deleteAllMixAuthority(templateKey);
            if (templateAuthorityDao.countMixAuthority(templateKey) > 0) {
                throw new EarthException("Delete MixTemplateAuthority fail");
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
        }
        return numOfRecordDeleted;
    }

    public long insertMgrUserProfile(MgrUserProfile mgrUserProfile) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        long numOfRecordInserted = 0L;
        try {
            numOfRecordInserted = userProfileDao.add(Constant.EARTH_WORKSPACE_ID, mgrUserProfile);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
        }
        return numOfRecordInserted;
    }

    public List<MgrUserProfile> getListByProfileIds(List<String> profileIds) throws EarthException {
        return userProfileDao.getListByProfileIds(profileIds);
    }

    public List<UserAccessRight> getMixAuthorityTemplate(TemplateKey templateKey) throws EarthException {
        return testTemplateAuthorityDao.getMixAuthorityTemplate(templateKey);
    }

    public boolean deleteMenuList(List<String> functionIds) throws EarthException {
        QMgrMenu qMgrMenu = QMgrMenu.newInstance();
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        for (String functionId : functionIds) {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrMenu.functionId, functionId);
            conditions.add(condition);
        }
        return (boolean) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return menuDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditions) > 0;
        });
    }

    // Minhtv test Menu
    public boolean insertMenu(MgrMenu mgrMenu) throws EarthException {
        return (boolean) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return menuDao.add(Constant.EARTH_WORKSPACE_ID, mgrMenu) > 0L;
        });
    }

    public long insertMenuU(MgrMenuU mgrMenuU)
            throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return menuAuthorityDao.insertMenuU(mgrMenuU);
        });
    }

    public long deleteUserAccessRight(List<String> functionIds) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            long del = 0L;
            for (String functionId : functionIds) {
                del += menuAuthorityDao.deleteAllUserAuthority(functionId);
            }
            return del;
        });
    }

    public long insertMenuP(MgrMenuP mgrMenuP) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return menuAuthorityDao.insertMenuP(mgrMenuP);
        });
    }

    public long deleteProfileAccessRight(List<String> functionIds) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            long del = 0L;
            for (String functionId : functionIds) {
                del += menuAuthorityDao.deleteAllProfileAuthority(functionId);
            }
            return del;
        });
    }

    public List<CtlMenu> getMixMenuAuthority(String functionId) throws EarthException {
        return (List<CtlMenu>) ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            QCtlMenu qCtlMenu = QCtlMenu.newInstance();
            BooleanBuilder condition = new BooleanBuilder();
            Predicate pre1 = qCtlMenu.functionId.eq(functionId);
            condition.and(pre1);
            return menuAuthorityDao.search(Constant.EARTH_WORKSPACE_ID, condition);
        }), CtlMenu.class);
    }

    public long deleteMixMenuAuthority(List<String> functionIds) throws EarthException {
        return (long) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return menuAuthorityDao.deleteAllMixAuthority(functionIds);
        });
    }
}