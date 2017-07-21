package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.dao.DatProcessDao;
import co.jp.nej.earth.dao.TemplateDao;
import org.json.JSONException;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.StrageDbDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EAutoIncrease;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.StrageDb;
import co.jp.nej.earth.model.constant.Constant.EarthId;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QStrageDb;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EModelUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;

/**
 * @author p-tvo-sonta
 */
@Service
public class ProcessServiceImpl extends BaseService implements ProcessService {

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private DatProcessDao datProcessDao;

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private StrageFileDao strageFileDao;

    @Autowired
    private StrageDbDao strageDbDao;

    @Autowired
    private EAutoIncrease eAutoIncrease;

    @Autowired
    private EMessageResource eMessageResource;

    private static final int NUM_255 = 255;
    private static final int NUM_260 = 260;
    private static final String STR_FILE = "file";


    public DatProcess getProcessSession(HttpSession session, String workspaceId, String workItemId, Integer processId)
            throws EarthException {
        // Get data process from session.
        DatProcess dataProcess = (DatProcess) getDataItemFromSession(
                                                    session,
                                                    SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                                                    EModelUtil.getProcessIndex(String.valueOf(processId)));
        return dataProcess;
    }

    public boolean updateProcessSession(HttpSession session, String workspaceId, DatProcess datProcess)
            throws EarthException {
        DatProcess dataProcessSession = getProcessSession(session, workspaceId, datProcess.getWorkItemId(),
                datProcess.getProcessId());
        // Update process template data.
        dataProcessSession.setTemplateId(datProcess.getTemplateId());
        dataProcessSession.setMgrTemplate(datProcess.getMgrTemplate());
        dataProcessSession.setProcessData(datProcess.getProcessData());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MgrProcess> getAllByWorkspace(String workspaceId) throws EarthException {
        return ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            try {
                return processDao.findAll(workspaceId);
            } catch (Exception e) {
                throw new EarthException(e);
            }
        }), MgrProcess.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteList(DeleteProcessForm form) throws EarthException {
        return (boolean) this.executeTransaction(form.getWorkspaceId(), () -> {
            try {
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
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> validateProcess(ProcessForm form) {
        List<Message> messages = new ArrayList<>();

        if (validationProcessText(form.getProcess())) {
            // TODO have no message code for this
            messages.add(new Message(ErrorCode.E0001,
                    eMessageResource.get(ErrorCode.E0001, new String[]{"processinfo"})));
        }
        // if (!XML_EXTENTION.equalsIgnoreCase(form.getFileExtention())) {
        // response.setResult(false);
        // response.setMessage(messageSource.getMessage(ErrorCode.E0019, new
        // String[] {}, Locale.ENGLISH));
        // return response;
        // }
        try {
            if (form.getProcess().getProcessDefinition() != null) {
                XML.toJSONObject(form.getProcess().getProcessDefinition()).toString();
            }
        } catch (JSONException e) {
            messages.add(new Message(ErrorCode.E0030, eMessageResource.get(ErrorCode.E0030, new String[]{})));
        }

        if (STR_FILE.equalsIgnoreCase(form.getProcess().getDocumentDataSavePath())) {
            if (form.getStrageFile().getSiteId() == null) {
                messages.add(new Message(ErrorCode.E0018, eMessageResource.get(ErrorCode.E0018, new String[]{"Site"})));
            }
        } else {
            if (!testConnection(form.getStrageDb())) {
                messages.add(new Message(ErrorCode.E0013, eMessageResource.get(ErrorCode.E0013, new String[]{})));
            }
        }
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insertOne(ProcessForm form, String sessionID) throws EarthException {
        return (boolean) this.executeTransaction(form.getWorkspaceId(), () -> {
            try {
                MgrProcess process = form.getProcess();
                // TODO
                Integer newId = eAutoIncrease.getAutoId(EarthId.PROCESS, sessionID);
                if(newId == 0) {
                    return false;
                }
                process.setProcessId(newId);
                //process.setProcessId(processDao.findAll(form.getWorkspaceId()).size() + 1);
                // parse data to json
                if (!EStringUtil.isEmpty(process.getProcessDefinition())) {
                    process.setProcessDefinition(XML.toJSONObject(process.getProcessDefinition()).toString());
                }
                processDao.add(form.getWorkspaceId(), process);
                if (STR_FILE.equalsIgnoreCase(process.getDocumentDataSavePath())) {
                    if (form.getStrageFile() == null) {
                        return false;
                    }
                    form.getStrageFile().setProcessId(process.getProcessId());
                    strageFileDao.add(form.getWorkspaceId(), form.getStrageFile());
                    return true;
                }
                if (EStringUtil.isEmpty(process.getDocumentDataSavePath())
                        || process.getDocumentDataSavePath().length() > NUM_260) {
                    return true;
                }
                form.getStrageDb().setProcessId(process.getProcessId());
                strageDbDao.add(form.getWorkspaceId(), form.getStrageDb());
                return true;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getDetail(String workspaceId, String processId) throws EarthException {
        return (HashMap<String, Object>) this.executeTransaction(workspaceId, () -> {
            try {
                int intProcessId = Integer.parseInt(processId);
                Map<String, Object> result = new HashMap<>();
                QMgrProcess qMgrProcess = QMgrProcess.newInstance();
                Map<Path<?>, Object> processCondition = new HashMap<>();
                processCondition.put(qMgrProcess.processId, intProcessId);
                MgrProcess process = processDao.findOne(workspaceId, processCondition);
                result.put("process", process);
                if (STR_FILE.equalsIgnoreCase(process.getDocumentDataSavePath())) {
                    QStrageFile qStrageFile = QStrageFile.newInstance();
                    Map<Path<?>, Object> fileCondition = new HashMap<>();
                    fileCondition.put(qStrageFile.processId, intProcessId);
                    result.put("strageFile", strageFileDao.findOne(workspaceId, fileCondition));
                } else {
                    QStrageDb qStrageDb = QStrageDb.newInstance();
                    Map<Path<?>, Object> dbCondition = new HashMap<>();
                    dbCondition.put(qStrageDb.processId, intProcessId);
//                  String schemaName = ConnectionManager.getEarthQueryFactory(workspaceId).getConnection().getSchema();
//                  dbCondition.put(qStrageDb.schemaName, schemaName);
                    result.put("strageDb", strageDbDao.findOne(workspaceId, dbCondition));
                }
                return result;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateOne(ProcessForm form) throws EarthException {
        return (boolean) this.executeTransaction(form.getWorkspaceId(), () -> {
            try {
                QMgrProcess qMgrProcess = QMgrProcess.newInstance();
                MgrProcess process = form.getProcess();
                if (!EStringUtil.isEmpty(process.getProcessDefinition())) {
                    process.setProcessDefinition(XML.toJSONObject(process.getProcessDefinition()).toString());
                }
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
                    if (form.getStrageFile() == null) {
                        return false;
                    }
                    form.getStrageFile().setProcessId(process.getProcessId());
                    strageFileDao.add(form.getWorkspaceId(), form.getStrageFile());
                } else {
                    if (form.getStrageDb() == null) {
                        return false;
                    }
                    form.getStrageDb().setProcessId(process.getProcessId());
                    strageDbDao.add(form.getWorkspaceId(), form.getStrageDb());
                }
                return true;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * check validation process text field
     *
     * @param process
     * @return
     */
    private boolean validationProcessText(MgrProcess process) {
        if (EStringUtil.isEmpty(process.getProcessName()) || process.getProcessName().length() > NUM_255) {
            return true;
        }
        if (EStringUtil.isEmpty(process.getDescription()) || process.getDescription().length() > NUM_255) {
            return true;
        }
        if (EStringUtil.isEmpty(process.getDocumentDataSavePath())
                || process.getDocumentDataSavePath().length() > NUM_260) {
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
        if (EStringUtil.isEmpty(strageDb.getSchemaName())) {
            return false;
        } else if (strageDb.getSchemaName().length() > NUM_255) {
            return false;
        }
        if (strageDb.getDbUser() != null && strageDb.getDbUser().length() > NUM_255) {
            return false;
        }
        if (strageDb.getDbPassword() != null && strageDb.getDbPassword().length() > NUM_255) {
            return false;
        }
        if (strageDb.getOwner() != null && strageDb.getOwner().length() > NUM_255) {
            return false;
        }
        if (strageDb.getDbServer() != null && strageDb.getDbServer().length() > NUM_255) {
            return false;
        }
        return true;
    }
}
