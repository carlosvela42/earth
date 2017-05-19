package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.jp.nej.earth.dao.WorkspaceDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant.WorkSpace;
import co.jp.nej.earth.util.DateUtil;

/**
 * @author longlt
 *
 */
@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class WorkspaceServiceImpl implements WorkspaceService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    @Autowired
    private WorkspaceDao workspaceDao;

    @Autowired
    private MessageSource messageSource;

    public List<MgrWorkspaceConnect> getAllWorkspaceConnections() throws EarthException {
        return workspaceDao.getAllMgrConnections();
    }

    public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException {
        return workspaceDao.getMgrConnectionByWorkspaceId(workspaceId);
    }

    public List<MgrWorkspace> getAll() throws EarthException {
        return workspaceDao.getAll();
    }

    public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {

        mgrWorkspaceConnect.setLastUpdateTime(DateUtil.getCurrentDateString());
        return workspaceDao.insertOne(mgrWorkspaceConnect);
    }

    public MgrWorkspaceConnect getDetail(String workspaceId) throws EarthException {
        MgrWorkspaceConnect mgrWorkspaceconnect = new MgrWorkspaceConnect();
        mgrWorkspaceconnect = workspaceDao.getOne(workspaceId);
        mgrWorkspaceconnect.setSchemaName(mgrWorkspaceconnect.getSchemaName().replace("c##", ""));
        mgrWorkspaceconnect.setDbUser(mgrWorkspaceconnect.getDbUser().replace("c##", ""));
        return mgrWorkspaceconnect;
    }

    public List<Message> deleteList(List<String> workspaceIds) throws EarthException {
        boolean status = true;
        List<Message> messages = new ArrayList<Message>();
        try {
            status = workspaceDao.deleteList(workspaceIds);
            if (!status) {
                Message message = new Message(WorkSpace.ISUSE_WORKSPACE,
                        messageSource.getMessage("E0022", new String[] { "WORKSPACE" }, Locale.ENGLISH));
                messages.add(message);
            }
        } catch (Exception ex) {
            LOG.error("WorkspaceServiceImpl:deleteList:" + ex.getMessage());
            Message message = new Message(WorkSpace.ISUSE_WORKSPACE,
                    messageSource.getMessage("E0022", new String[] { "WORKSPACE" }, Locale.ENGLISH));
            messages.add(message);
            return messages;
        }
        return messages;
    }

    public List<Message> validateInsert(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        List<Message> messages = new ArrayList<Message>();
        boolean status = true;
        try {
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getWorkspaceId())) {
                Message message = new Message(WorkSpace.ID_BLANK,
                        messageSource.getMessage("E0001", new String[] { "ID" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getSchemaName())) {
                Message message = new Message(WorkSpace.SCHEMA_BLANK,
                        messageSource.getMessage("E0001", new String[] { "SCHEMA" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())) {
                Message message = new Message(WorkSpace.DBUSER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBUSER" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbPassword())) {
                Message message = new Message(WorkSpace.DBPASS_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBPASS" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getOwner())) {
                Message message = new Message(WorkSpace.OWNER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "OWNER" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())) {
                Message message = new Message(WorkSpace.DBSERVER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBSERVER" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (!StringUtils.isEmpty(mgrWorkspaceConnect.getWorkspaceId())
                    && !StringUtils.isEmpty(mgrWorkspaceConnect.getSchemaName())
                    && !StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())
                    && !StringUtils.isEmpty(mgrWorkspaceConnect.getDbPassword())
                    && !StringUtils.isEmpty(mgrWorkspaceConnect.getOwner())
                    && !StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())) {

                status = workspaceDao.getById(mgrWorkspaceConnect.getWorkspaceId());
                if (status) {
                    Message message = new Message(WorkSpace.ISEXIT_WORKSPACE,
                            messageSource.getMessage("E0005", new String[] { "WORKSPACE", "SYSTEM" }, Locale.ENGLISH));
                    messages.add(message);
                }
            }
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
        return messages;

    }

    public List<Message> validateUpdate(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        List<Message> messages = new ArrayList<Message>();
        try {
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getSchemaName())) {
                Message message = new Message(WorkSpace.SCHEMA_BLANK,
                        messageSource.getMessage("E0001", new String[] { "SCHEMA" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())) {
                Message message = new Message(WorkSpace.DBUSER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBUSER" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbPassword())) {
                Message message = new Message(WorkSpace.DBPASS_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBPASS" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getOwner())) {
                Message message = new Message(WorkSpace.OWNER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "OWNER" }, Locale.ENGLISH));
                messages.add(message);
            }
            if (StringUtils.isEmpty(mgrWorkspaceConnect.getDbUser())) {
                Message message = new Message(WorkSpace.DBSERVER_BLANK,
                        messageSource.getMessage("E0001", new String[] { "DBSERVER" }, Locale.ENGLISH));
                messages.add(message);
            }
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
        return messages;
    }

    public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
        mgrWorkspaceConnect.setLastUpdateTime(DateUtil.getCurrentDateString());
        return workspaceDao.updateOne(mgrWorkspaceConnect);
    }
}
