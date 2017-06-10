package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.WorkSpace;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longlt
 *
 */
@Service
public class WorkspaceServiceImpl extends BaseService implements WorkspaceService {
  private static final Logger LOG = LoggerFactory.getLogger(WorkspaceService.class);
  @Autowired
  private WorkspaceDao workspaceDao;

  @Autowired
  private EMessageResource messageSource;

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
    mgrWorkspaceConnect.setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
    mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
    mgrWorkspaceConnect.setPort(Constant.WorkSpace.PORT);
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

      // Insert schema information.
      TransactionDefinition earthDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
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
    return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
      List<Message> messages = new ArrayList<Message>();
      if (!StringUtils.isEmpty(mgrWorkspaceConnect.getWorkspaceId())
          && !StringUtils.isEmpty(mgrWorkspaceConnect.getSchemaName())
          && !StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())
          && !StringUtils.isEmpty(mgrWorkspaceConnect.getOwner())
          && !StringUtils.isEmpty(mgrWorkspaceConnect.getDbServer())) {

        if (workspaceDao.getById(mgrWorkspaceConnect.getWorkspaceId())) {
          Message message = new Message(WorkSpace.ISEXIT_WORKSPACE,
              messageSource.get(ErrorCode.E0005, new String[] { "WORKSPACE", "SYSTEM" }));
          messages.add(message);
        }
      }
      return messages;
    }), Message.class);
  }

  public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
    return (boolean) executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
      mgrWorkspaceConnect.setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
      mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
      mgrWorkspaceConnect.setPort(Constant.WorkSpace.PORT);
      return workspaceDao.updateOne(mgrWorkspaceConnect);
    });
  }
}
