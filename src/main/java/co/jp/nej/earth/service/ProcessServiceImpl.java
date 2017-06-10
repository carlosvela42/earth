package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.DatProcessDao;
import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.StrageDbDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.dao.WorkItemDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.StrageDb;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.sql.QDatProcess;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QStrageDb;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.model.sql.QWorkItem;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Path;
import org.json.JSONException;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author p-tvo-sonta
 */
@Service
public class ProcessServiceImpl extends BaseService implements ProcessService {

    @Autowired
    private ProcessDao processDao;
    @Autowired
    private StrageFileDao strageFileDao;
    @Autowired
    private StrageDbDao strageDbDao;
    @Autowired
    private EMessageResource eMessageResource;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private WorkItemService workItemService;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private DatProcessDao datProcessDao;
    @Autowired
    private WorkItemDao workItemDao;

    private static final String XML_EXTENTION = "xml";
    private static final int NUM_255 = 255;
    private static final int NUM_260 = 260;
    private static final String STR_FILE = "file";
    private static final String EVENT_OPEN = "Open";
    private static final String EVENT_TASK = "DataUpdateProcess";

    @Override
    public boolean openProcess(HttpSession session, String workspaceId, String processId, String workItemId,
            OpenProcessMode openMode) throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                CtlEvent ctlEvent = new CtlEvent();
                ctlEvent.setStatus(EVENT_OPEN);
                ctlEvent.setWorkitemId(workItemId);
                ctlEvent.setTaskId(EVENT_TASK);
                // TODO we will get login user
                ctlEvent.setUserId("1");
                if (OpenProcessMode.EDIT.equals(openMode)) {
                    // TODO
                    ctlEvent.setEventId(Integer.toString(eventDao.findAll(workspaceId).size() + 1));
                    eventDao.add(workspaceId, ctlEvent);
                }
                WorkItem workItem = workItemService.getWorkItemDataStructure(workItemId, workspaceId);
                session.setAttribute("ORIGIN" + workspaceId + "&" + workItemId, workItem);
                try {
                    ctlEvent.setWorkitemData(new ObjectMapper().writeValueAsString(workItem));
                } catch (JsonProcessingException e) {
                    throw new EarthException(e);
                }
                if (OpenProcessMode.EDIT.equals(openMode)) {
                    QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qCtlEvent.workitemId, workItemId);
                    condition.put(qCtlEvent.eventId, ctlEvent.getEventId());
                    Map<Path<?>, Object> updateMap = new HashMap<>();
                    updateMap.put(qCtlEvent.workitemData, ctlEvent.getWorkitemData());
                    updateMap.put(qCtlEvent.lastUpdateTime, DateUtil.getCurrentDateString());
                    eventDao.update(workspaceId, condition, updateMap);
                }
                return true;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    public boolean closeProcess(HttpSession session, String workspaceId, String workItemId, String eventId)
            throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.workitemId, workItemId);
                condition.put(qCtlEvent.eventId, eventId);
                condition.put(qCtlEvent.status, EVENT_OPEN);
                boolean result = eventDao.delete(workspaceId, condition) > 0;
                session.removeAttribute("ORIGIN" + workspaceId + "&" + workItemId);
                return result;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    public DatProcess getProcess(HttpSession session, String workspaceId, Integer processId) throws EarthException {
        return (DatProcess) this.executeTransaction(workspaceId, () -> {
            try {
                QDatProcess qDatProcess = QDatProcess.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qDatProcess.processId, processId);
                DatProcess datProcess = datProcessDao.findOne(workspaceId, condition);
                QWorkItem qWorkItem = QWorkItem.newInstance();
                condition = new HashMap<>();
                condition.put(qWorkItem.workitemId, datProcess.getTemplateId());
                WorkItem workItem = workItemDao.findOne(workspaceId, condition);
                datProcess.setProcessData(templateDao.getProcessTemplateData(workspaceId, Integer.toString(processId),
                        datProcess.getTemplateId(), workItem.getLastHistoryNo()));
                return datProcess;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    public boolean updateProcess(HttpSession session, String workspaceId, ProcessMap datProcess) throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {

                QDatProcess qDatProcess = QDatProcess.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qDatProcess.processId, datProcess.getProcessId());
                Map<Path<?>, Object> updateMap = new HashMap<>();
                updateMap.put(qDatProcess.workitemId, datProcess.getWorkItemId());
                updateMap.put(qDatProcess.templateId, datProcess.getTemplateId());
                updateMap.put(qDatProcess.lastUpdateTime, DateUtil.getCurrentDateString());
                if (datProcessDao.update(workspaceId, condition, updateMap) < 1) {
                    return false;
                }
                QWorkItem qWorkItem = QWorkItem.newInstance();
                condition = new HashMap<>();
                condition.put(qWorkItem.workitemId, datProcess.getWorkItemId());
                WorkItem workItem = workItemDao.findOne(workspaceId, condition);
                datProcess
                        .setMgrTemplate(templateDao.getById(new TemplateKey(workspaceId, datProcess.getTemplateId())));
                return templateDao.insertProcessTemplateData(workspaceId, datProcess, workItem.getLastHistoryNo()) > 0;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
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
    public Response validateDeleteAction(DeleteProcessForm form) {
        Response response = new Response();
        if (form.getProcessIds() == null || form.getProcessIds().isEmpty()) {
            response.setResult(false);
            response.setMessage(eMessageResource.get(ErrorCode.E0013, new String[] {}));
        } else if (!form.isConfirmDelete()) {
            response.setResult(false);
            response.setMessage(eMessageResource.get(ErrorCode.E0014, new String[] {}));
        }
        return response;
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
                    eMessageResource.get(ErrorCode.E0001, new String[] { "processinfo" })));
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
            messages.add(new Message(ErrorCode.E0020, eMessageResource.get(ErrorCode.E0020, new String[] {})));
        }

        if (STR_FILE.equalsIgnoreCase(form.getProcess().getDocumentDataSavePath())) {
            if (form.getStrageFile().getSiteId() == null) {
                messages.add(new Message(ErrorCode.E0018, eMessageResource.get(ErrorCode.E0018, new String[] {})));
            }
        } else {
            if (!testConnection(form.getStrageDb())) {
                messages.add(new Message(ErrorCode.E0021, eMessageResource.get(ErrorCode.E0021, new String[] {})));
            }
        }
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insertOne(ProcessForm form) throws EarthException {
        return (boolean) this.executeTransaction(form.getWorkspaceId(), () -> {
            try {
                MgrProcess process = form.getProcess();
                // TODO
                process.setProcessId(processDao.findAll(form.getWorkspaceId()).size() + 1);
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

    @Override
    public boolean closeAndSaveProcess(HttpSession session, WorkItem workItem, String workspaceId)
            throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                WorkItem workItemSession = (WorkItem) session
                        .getAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId());
                if (workItemSession == null) {
                    return false;
                }
                workItemSession = mergeWorkItem(workItem, workItemSession);
                String json = null;
                try {
                    json = new ObjectMapper().writeValueAsString(workItemSession);
                } catch (JsonProcessingException e) {
                    throw new EarthException(e);
                }
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.workitemId, workItem.getWorkitemId());
                Map<Path<?>, Object> updateMap = new HashMap<>();
                updateMap.put(qCtlEvent.workitemData, json);
                updateMap.put(qCtlEvent.status, AgentBatch.STATUS_EDIT);
                long result = eventDao.update(workspaceId, condition, updateMap);
                session.removeAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId());
                session.removeAttribute("TMP" + workspaceId + "&" + workItem.getWorkitemId());
                return result > 0;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * merge workItem
     *
     * @param source
     * @param desc
     * @return
     */
    private WorkItem mergeWorkItem(WorkItem source, WorkItem desc) {
        if (source.getEventStatus() != null) {
            desc.setEventStatus(source.getEventStatus());
        }
        if (!EStringUtil.isEmpty(source.getTemplateId())) {
            desc.setTemplateId(source.getTemplateId());
        }
        if (!EStringUtil.isEmpty(source.getTaskId())) {
            source.setTaskId(source.getTaskId());
        }
        if (source.getWorkItemData() != null) {
            desc.setWorkItemData(source.getWorkItemData());
        }
        if (source.getFolderItems() != null) {
            for (int i = 0; i < source.getFolderItems().size(); i++) {
                FolderItem folderItem = source.getFolderItems().get(i);
                if (folderItem != null) {
                    if (desc.getFolderItems().size() > i) {
                        FolderItem folderDesc = desc.getFolderItems().get(i);
                        folderDesc.setWorkitemId(desc.getWorkitemId());
                        if (folderItem.getFolderItemData() != null) {
                            folderDesc.setFolderItemData(folderItem.getFolderItemData());
                        }
                        if (!EStringUtil.isEmpty(folderItem.getTemplateId())) {
                            folderDesc.setTemplateId(folderItem.getTemplateId());
                        }
                        if (folderItem.getAction() != null) {
                            folderDesc.setAction(folderItem.getAction());
                        }
                        if (folderItem.getDocuments() != null) {
                            for (int j = 0; j < folderItem.getDocuments().size(); j++) {
                                Document document = folderItem.getDocuments().get(j);
                                if (document != null) {
                                    if (folderDesc.getDocuments().size() > j) {
                                        Document docDesc = folderDesc.getDocuments().get(j);
                                        if (document.getAction() != null) {
                                            docDesc.setAction(document.getAction());
                                        }
                                        if (document.getDocumentBinary() != null) {
                                            docDesc.setDocumentBinary(document.getDocumentBinary());
                                        }
                                        if (document.getDocumentData() != null) {
                                            docDesc.setDocumentData(document.getDocumentData());
                                        }
                                        if (!EStringUtil.isEmpty(document.getDocumentPath())) {
                                            docDesc.setDocumentPath(document.getDocumentPath());
                                        }
                                        if (!EStringUtil.isEmpty(document.getDocumentType())) {
                                            docDesc.setDocumentType(document.getDocumentType());
                                        }
                                        if (!EStringUtil.isEmpty(document.getTemplateId())) {
                                            docDesc.setTemplateId(document.getTemplateId());
                                        }
                                        if (document.getPageCount() != null) {
                                            docDesc.setPageCount(document.getPageCount());
                                        }
                                        if (!EStringUtil.isEmpty(document.getViewInformation())) {
                                            docDesc.setViewInformation(document.getViewInformation());
                                        }
                                        if (document.getLayers() != null) {
                                            for (int k = 0; k < document.getLayers().size(); k++) {
                                                Layer layer = document.getLayers().get(k);
                                                if (docDesc.getLayers().size() > k) {
                                                    if (layer.getAction() != null) {
                                                        docDesc.getLayers().get(k).setAction(layer.getAction());
                                                    }
                                                    if (!EStringUtil.isEmpty(layer.getAnnotations())) {
                                                        docDesc.getLayers().get(k)
                                                                .setAnnotations(layer.getAnnotations());
                                                    }
                                                    if (layer.getLayerData() != null) {
                                                        docDesc.getLayers().get(k).setLayerData(layer.getLayerData());
                                                    }
                                                    if (!EStringUtil.isEmpty(layer.getOwnerId())) {
                                                        docDesc.getLayers().get(k).setOwnerId(layer.getOwnerId());
                                                    }
                                                    if (!EStringUtil.isEmpty(layer.getTemplateId())) {
                                                        docDesc.getLayers().get(k).setTemplateId(layer.getTemplateId());
                                                    }
                                                } else {
                                                    if (docDesc.getLayers() != null) {
                                                        docDesc.getLayers().add(layer);
                                                    } else {
                                                        List<Layer> layers = new ArrayList<>();
                                                        layers.add(layer);
                                                        docDesc.setLayers(layers);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (folderDesc.getDocuments() != null) {
                                            folderDesc.getDocuments().add(document);
                                        } else {
                                            List<Document> documents = new ArrayList<>();
                                            documents.add(document);
                                            folderDesc.setDocuments(documents);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (desc.getFolderItems() != null) {
                            desc.getFolderItems().add(folderItem);
                        } else {
                            List<FolderItem> folderItems = new ArrayList<>();
                            folderItems.add(folderItem);
                            desc.setFolderItems(folderItems);
                        }
                    }
                }
            }
        }
        return desc;
    }
}
