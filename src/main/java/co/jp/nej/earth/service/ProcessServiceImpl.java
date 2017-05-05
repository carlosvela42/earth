package co.jp.nej.earth.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.StrageDbDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.EventControl;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.StrageDb;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QStrageDb;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.util.DateUtil;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessDao processDao;
    @Autowired
    private StrageFileDao strageFileDao;
    @Autowired
    private StrageDbDao strageDbDao;
    @Autowired
    private MessageSource messageSource;

    private static final String XML_EXTENTION = "xml";
    private static final int NUM_255 = 255;
    private static final int NUM_260 = 260;
    private static final String STR_FILE = "file";

    public String openProcess(String token, String workspaceId, String processId, String workItemId) {
        // 1. Create new Event.
        EventControl event = new EventControl();
        event.setWorkItemId(workItemId);
        return null;
    }

    public String closeProcess(String token, String workspaceId, String processId) {
        return null;
    }

    public Process getProcess(String token, String workspaceId, String processId) {
        return null;
    }

    public String updateProcess(String token, String workspaceId, Process process) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MgrProcess> getAllByWorkspace(String workspaceId) throws EarthException {
        return processDao.findAll(workspaceId, null, null, null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response validateDeleteAction(DeleteProcessForm form) {
        Response response = new Response();
        if (form.getProcessIds() == null || form.getProcessIds().isEmpty()) {
            response.setResult(false);
            response.setMessage(messageSource.getMessage(ErrorCode.E0013, new String[] {}, Locale.ENGLISH));
        } else if (!form.isConfirmDelete()) {
            response.setResult(false);
            response.setMessage(messageSource.getMessage(ErrorCode.E0014, new String[] {}, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteList(DeleteProcessForm form) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        QMgrProcess qMgrProcess = QMgrProcess.newInstance();
        QStrageFile qStrageFile = QStrageFile.newInstance();
        QStrageDb qStrageDb = QStrageDb.newInstance();
        // loop list process id
        for (Integer processId : form.getProcessIds()) {
            Map<Path<?>, Object> processCondition = new HashMap<>();
            processCondition.put(qMgrProcess.processId, processId);
            conditions.add(processCondition);
            // delete data in strage file
            Map<Path<?>, Object> fileCondition = new HashMap<>();
            fileCondition.put(qStrageFile.processId, processId);
            strageFileDao.delete(form.getWorkspaceId(), fileCondition);
            // delete data in strage db
            Map<Path<?>, Object> dbCondition = new HashMap<>();
            dbCondition.put(qStrageDb.processId, processId);
            strageDbDao.delete(form.getWorkspaceId(), dbCondition);
        }
        return processDao.deleteList(form.getWorkspaceId(), conditions) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response validateProcess(ProcessForm form) {
        Response response = new Response();
        if (validationProcessText(form.getProcess())) {
            response.setResult(false);
            // TODO have no message code for this
            response.setMessage(messageSource.getMessage(ErrorCode.E0019, new String[] {}, Locale.ENGLISH));
            return response;
        }
        if (!XML_EXTENTION.equalsIgnoreCase(form.getFileExtention())) {
            response.setResult(false);
            response.setMessage(messageSource.getMessage(ErrorCode.E0019, new String[] {}, Locale.ENGLISH));
            return response;
        }
        if (!form.getProcess().getProcessDefinition().matches("(?s).*?<(\\S+?)[^>]*>.*?</\\1>.*")) {
            response.setResult(false);
            response.setMessage(messageSource.getMessage(ErrorCode.E0020, new String[] {}, Locale.ENGLISH));
            return response;
        }
        if (STR_FILE.equalsIgnoreCase(form.getProcess().getDocumentDataSavePath())) {
            if (form.getStrageFile().getSiteId() == null) {
                response.setResult(false);
                response.setMessage(messageSource.getMessage(ErrorCode.E0018, new String[] {}, Locale.ENGLISH));
            }
            return response;
        }
        if (!testConnection(form.getStrageDb())) {
            response.setResult(false);
            response.setMessage(messageSource.getMessage(ErrorCode.E0021, new String[] {}, Locale.ENGLISH));
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insertOne(ProcessForm form) throws EarthException {
        MgrProcess process = form.getProcess();
        // TODO
        process.setProcessId(processDao.findAll(form.getWorkspaceId(), null, null, null).size() + 1);
        // parse data to json
        process.setProcessDefinition(XML.toJSONObject(process.getProcessDefinition()).toString());
        long resultNum = processDao.add(form.getWorkspaceId(), process);
        if (resultNum <= 0) {
            return false;
        }
        if (STR_FILE.equalsIgnoreCase(process.getDocumentDataSavePath())) {
            resultNum = strageFileDao.add(form.getWorkspaceId(), form.getStrageFile());
            return resultNum > 0;
        }
        resultNum = strageDbDao.add(form.getWorkspaceId(), form.getStrageDb());
        return resultNum > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getDetail(String workspaceId, int processId) throws EarthException {
        Map<String, Object> result = new HashMap<>();
        QMgrProcess qMgrProcess = QMgrProcess.newInstance();
        Map<Path<?>, Object> processCondition = new HashMap<>();
        processCondition.put(qMgrProcess.processId, processId);
        MgrProcess process = processDao.findOne(workspaceId, processCondition);
        result.put("process", process);
        if (STR_FILE.equalsIgnoreCase(process.getDocumentDataSavePath())) {
            QStrageFile qStrageFile = QStrageFile.newInstance();
            Map<Path<?>, Object> fileCondition = new HashMap<>();
            fileCondition.put(qStrageFile.processId, processId);
            result.put("strageFile", strageFileDao.findOne(workspaceId, fileCondition));
        } else {
            QStrageDb qStrageDb = QStrageDb.newInstance();
            Map<Path<?>, Object> dbCondition = new HashMap<>();
            dbCondition.put(qStrageDb.processId, processId);
            result.put("strageDb", strageDbDao.findOne(workspaceId, dbCondition));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateOne(ProcessForm form) throws EarthException {
        QMgrProcess qMgrProcess = QMgrProcess.newInstance();
        MgrProcess process = form.getProcess();
        process.setProcessDefinition(XML.toJSONObject(process.getProcessDefinition()).toString());
        // update processCondition
        Map<Path<?>, Object> processCondition = new HashMap<>();
        processCondition.put(qMgrProcess.processId, process.getProcessId());
        // update map
        Map<Path<?>, Object> processUpdateMap = new HashMap<>();
        processUpdateMap.put(qMgrProcess.description, process.getDescription());
        processUpdateMap.put(qMgrProcess.documentDataSavePath, process.getDocumentDataSavePath());
        processUpdateMap.put(qMgrProcess.processDefinition, process.getProcessDefinition());
        processUpdateMap.put(qMgrProcess.processName, process.getProcessName());
        processUpdateMap.put(qMgrProcess.processVersion, process.getProcessVersion());
        processUpdateMap.put(qMgrProcess.lastUpdateTime, DateUtil.getCurrentDateString());
        // update process
        long resultNum = processDao.update(form.getWorkspaceId(), processCondition, processUpdateMap);
        if (resultNum <= 0) {
            return false;
        }
        // delete strageFile and strageDb from db if existed
        QStrageFile qStrageFile = QStrageFile.newInstance();
        QStrageDb qStrageDb = QStrageDb.newInstance();
        Map<Path<?>, Object> fileCondition = new HashMap<>();
        fileCondition.put(qStrageFile.processId, process.getProcessId());
        strageFileDao.delete(form.getWorkspaceId(), fileCondition);
        Map<Path<?>, Object> dbCondition = new HashMap<>();
        dbCondition.put(qStrageDb.processId, process.getProcessId());
        strageDbDao.delete(form.getWorkspaceId(), dbCondition);
        // insert strageFile strageDb
        if (STR_FILE.equalsIgnoreCase(process.getDocumentDataSavePath())) {
            resultNum = strageFileDao.add(form.getWorkspaceId(), form.getStrageFile());
            return resultNum > 0;
        }
        resultNum = strageDbDao.add(form.getWorkspaceId(), form.getStrageDb());
        return resultNum > 0;
    }

    /**
     * check validation process text field
     *
     * @param process
     * @return
     */
    private boolean validationProcessText(MgrProcess process) {
        if (process.getProcessName() != null || process.getProcessName().length() > NUM_255) {
            return true;
        }
        if (process.getDescription() != null || process.getDescription().length() > NUM_255) {
            return true;
        }
        if (process.getDocumentDataSavePath() != null || process.getDocumentDataSavePath().length() > NUM_260) {
            return true;
        }
        return false;
    }

    /**
     * test conection
     *
     * @param strageDb
     * @return
     */
    private boolean testConnection(StrageDb strageDb) {
        Connection connection = null;
        String dbURL = "jdbc:sqlserver://localhost\\sqlexpress";
        String className = null;
        if ("Oracle".equals(strageDb.getDbType())) {
            className = DatabaseType.ORACLE.toString();
            dbURL = "jdbc:oracle:thin:@" + strageDb.getDbServer() + ":" + strageDb.getDbPort() + ":"
                    + strageDb.getSchemaName();
        } else {
            className = DatabaseType.SQL_SERVER.toString();
            dbURL = "jdbc:sqlserver://" + strageDb.getDbServer() + ":" + strageDb.getDbPort() + ";databaseName={"
                    + strageDb.getSchemaName() + "}";
        }
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(dbURL, strageDb.getDbUser(), strageDb.getDbPassword());
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        if (connection == null) {
            return false;
        }
        return true;
    }
}
