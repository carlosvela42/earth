package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.WorkSpace;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.PasswordPolicy;

/**
 * @author longlt
 */
@Service
public class WorkspaceServiceImpl extends BaseService implements WorkspaceService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceService.class);
    @Autowired
    private WorkspaceDao workspaceDao;

    @Autowired
    private EMessageResource messageSource;

    @Autowired
    private PasswordPolicy passwordPolicy;

    @Autowired
    private DatabaseType databaseType;

    public List<MgrWorkspaceConnect> getAllWorkspaceConnections() throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return workspaceDao.getAllMgrConnections();
        }), MgrWorkspaceConnect.class);
    }

    public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException {
        return ConversionUtil.castObject(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return workspaceDao.getMgrConnectionByWorkspaceId(workspaceId);
        }), MgrWorkspaceConnect.class);
    }

    public List<MgrWorkspace> getAll() throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return workspaceDao.getAll();
        }), MgrWorkspace.class);
    }

    public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        mgrWorkspaceConnect.setLastUpdateTime(DateUtil.getCurrentDateString());
        mgrWorkspaceConnect.setPort(WorkSpace.SQL_PORT);
        if (mgrWorkspaceConnect.getDbType().equals(DatabaseType.ORACLE)) {
            mgrWorkspaceConnect
                    .setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
            mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
            mgrWorkspaceConnect.setPort(WorkSpace.ORACLE_PORT);
        }
        PlatformTransactionManager earthTransactionManager = null;
        TransactionStatus earthStatus = null;
        PlatformTransactionManager systemTransactionManager = null;
        TransactionStatus systemStatus = null;
        try {
            TransactionDefinition sysDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
            systemTransactionManager = ConnectionManager.getTransactionManager(Constant.SYSTEM_WORKSPACE_ID);
            systemStatus = systemTransactionManager.getTransaction(sysDef);
            // Create new schema.
            workspaceDao.createSchema(mgrWorkspaceConnect, Constant.SYSTEM_WORKSPACE_ID);
            // Create table
            workspaceDao.createTable(mgrWorkspaceConnect, Constant.SYSTEM_WORKSPACE_ID);
            systemTransactionManager.commit(systemStatus);

            sysDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
            systemTransactionManager = ConnectionManager.getTransactionManager(Constant.SYSTEM_WORKSPACE_ID);
            systemStatus = systemTransactionManager.getTransaction(sysDef);
            if (DatabaseType.isSqlServer(mgrWorkspaceConnect.getDbType())) {
                workspaceDao.addRoleMember(mgrWorkspaceConnect, Constant.SYSTEM_WORKSPACE_ID);
            }
            systemTransactionManager.commit(systemStatus);
            // Insert schema information.
            TransactionDefinition earthDef = new DefaultTransactionDefinition(
                    TransactionDefinition.PROPAGATION_REQUIRED);
            earthTransactionManager = ConnectionManager.getTransactionManager(Constant.EARTH_WORKSPACE_ID);
            earthStatus = earthTransactionManager.getTransaction(earthDef);

            workspaceDao.insertOne(mgrWorkspaceConnect);
            earthTransactionManager.commit(earthStatus);
        } catch (EarthException ex) {
            LOG.error(ex.getMessage());
            if (earthTransactionManager != null) {
                earthTransactionManager.rollback(earthStatus);
            }
            if (systemTransactionManager != null) {
                systemTransactionManager.rollback(systemStatus);
            }
            throw new EarthException(ex);
        }
        return true;
    }

    public MgrWorkspaceConnect getDetail(String workspaceId) throws EarthException {
        return ConversionUtil.castObject(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            MgrWorkspaceConnect mgrWorkspaceconnect = workspaceDao.getOne(workspaceId);
            mgrWorkspaceconnect.setSchemaName(mgrWorkspaceconnect.getSchemaName().replace("c##", ""));
            mgrWorkspaceconnect.setDbUser(mgrWorkspaceconnect.getDbUser().replace("c##", ""));
            return mgrWorkspaceconnect;
        }), MgrWorkspaceConnect.class);
    }

    public List<Message> deleteList(List<String> workspaceIds) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return workspaceDao.deleteList(workspaceIds);
        }), Message.class);
    }

    public List<Message> validateInsert(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        List<Message> messages = new ArrayList<Message>();
        if (DatabaseType.isSqlServer(databaseType)) {
            String pwd = mgrWorkspaceConnect.getDbPassword();
            List<String> passwordValidate = new ArrayList<>();
            try {
                passwordValidate = passwordPolicy.validate(pwd);
                if (passwordValidate != null && passwordValidate.size() > 0) {
                    for (String string : passwordValidate) {
                        Message message = new Message(EStringUtil.EMPTY, string);
                        messages.add(message);
                    }
                    return messages;
                }
            } catch (Exception ex) {
                Message message = new Message(EStringUtil.EMPTY, ErrorCode.E0005);
                messages.add(message);
            }
        }
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {

            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getWorkspaceId())
                    && !EStringUtil.isEmpty(mgrWorkspaceConnect.getSchemaName())
                    && !EStringUtil.isEmpty(mgrWorkspaceConnect.getDbUser())
                    && !EStringUtil.isEmpty(mgrWorkspaceConnect.getOwner())
                    && !EStringUtil.isEmpty(mgrWorkspaceConnect.getDbServer())) {
                if (workspaceDao.getById(mgrWorkspaceConnect.getSchemaName())) {
                    Message message = new Message(WorkSpace.ISEXIT_WORKSPACE,
                            messageSource.get(ErrorCode.E0003, new String[] { "workspace.schemaName" }));
                    messages.add(message);
                }
            }
            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getWorkspaceId())
                    && !EStringUtil.checkAlphabet(mgrWorkspaceConnect.getWorkspaceId())) {
                messages.add(new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.id" })));
            }
            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getSchemaName())
                    && !EStringUtil.checkAlphabet(mgrWorkspaceConnect.getSchemaName())) {
                messages.add(new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.schemaName" })));
            }
            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getDbUser())
                    && !EStringUtil.checkAlphabet(mgrWorkspaceConnect.getDbUser())) {
                messages.add(new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.dbUser" })));
            }
            if (DatabaseType.isOracle(databaseType)
                    && !EStringUtil.checkAlphabet(mgrWorkspaceConnect.getDbPassword())) {
                Message message = new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.dbPassword" }));
                messages.add(message);
            }
            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getOwner())
                    && !EStringUtil.checkAlphabet(mgrWorkspaceConnect.getOwner())) {
                messages.add(new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.owner" })));
            }
            if (!EStringUtil.isEmpty(mgrWorkspaceConnect.getDbServer())
                    && !EStringUtil.checkIpAddress(mgrWorkspaceConnect.getDbServer())) {
                messages.add(new Message(ErrorCode.E0011,
                        messageSource.get(ErrorCode.E0011, new String[] { "workspace.dbServer" })));
            }
            return messages;
        }), Message.class);
    }

    public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        return (boolean) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            mgrWorkspaceConnect.setPort(WorkSpace.SQL_PORT);
            if (mgrWorkspaceConnect.getDbType().equals(DatabaseType.ORACLE)) {
                mgrWorkspaceConnect
                        .setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
                mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
                mgrWorkspaceConnect.setPort(WorkSpace.ORACLE_PORT);
            }

            return workspaceDao.updateOne(mgrWorkspaceConnect);
        });
    }

}
