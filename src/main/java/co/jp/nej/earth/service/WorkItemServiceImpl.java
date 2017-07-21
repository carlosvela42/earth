package co.jp.nej.earth.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

import co.jp.nej.earth.dao.DatProcessDao;
import co.jp.nej.earth.dao.DocumentDao;
import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.dao.FolderItemDao;
import co.jp.nej.earth.dao.LayerDao;
import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.dao.WorkItemDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EEventId;
import co.jp.nej.earth.id.EWorkItemId;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.Column;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.Row;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserInfo;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.WorkItemDictionary;
import co.jp.nej.earth.model.WorkItemListDTO;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.Action;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.EventStatus;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.enums.Operation;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.enums.Type;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.sql.QDocument;
import co.jp.nej.earth.model.sql.QFolderItem;
import co.jp.nej.earth.model.sql.QLayer;
import co.jp.nej.earth.model.sql.QTemplateData;
import co.jp.nej.earth.model.sql.QWorkItem;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.CryptUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EModelUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.TemplateUtil;
import co.jp.nej.earth.util.UniqueIdUtil;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import co.jp.nej.earth.web.form.SearchForm;

/**
 * @author p-tvo-sonta
 */
@Service
public class WorkItemServiceImpl extends BaseService implements WorkItemService {

    @Autowired
    private WorkItemDao workItemDao;

    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private DatProcessDao datProcessDao;

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private LayerDao layerDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Autowired
    private EWorkItemId eWorkItemId;

    @Autowired
    private EEventId eEventId;

    private static final String EVENT_TASK = "DataUpdateProcess";
    private static final Logger LOG = LoggerFactory.getLogger(WorkItemServiceImpl.class);

    private static final int DEFAULT_LOG_VERSION = 1;

    /*
     * Open a Process, Including 2 modes: Edit and ReadOnly.
     *
     */
    @Override
    public List<Message> openWorkItem(HttpSession session, String workspaceId, String processId, String workItemId,
                                      Integer openMode) throws EarthException {
        return ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            List<Message> messages = new ArrayList<>();

            if (isOpennedWorkItem(session, SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId))) {
                messages.add(new Message(ErrorCode.E1010,
                                             messageSource.get(ErrorCode.E1010, new String[]{"workItem"})));
                return messages;
            }

            String workItemTemplateId = templateDao.getTemplateIdByItemId(workspaceId, workItemId,
                    TemplateType.WORKITEM);
            TemplateAccessRight templateAccessRight = TemplateUtil.getAuthorityFromSession(session);
            AccessRight accessRight = templateAccessRight.get(new TemplateKey(workspaceId, workItemTemplateId));

            // In case Template Access Right is none.
            if (accessRight.eq(AccessRight.NONE)) {
                messages.add(new Message(ErrorCode.E0025, messageSource.get(ErrorCode.E0025)));
                return messages;
            }

            if (OpenProcessMode.isEdit(openMode)) {
                if (accessRight.le(AccessRight.RW)) {
                    messages.add(new Message(ErrorCode.E0025, messageSource.get(ErrorCode.E0025)));
                    return messages;
                }

                if (eventDao.getEventByWorkItemId(workspaceId, workItemId) != null) {
                    messages.add(new Message(ErrorCode.E1010,
                                                messageSource.get(ErrorCode.E1010, new String[]{"workItem"})));
                    return messages;
                }

                return openWorkItemWithEditMode(session, workspaceId, processId, workItemId);
            }

            if (accessRight.le(AccessRight.RO)) {
                messages.add(new Message(ErrorCode.E0025, messageSource.get(ErrorCode.E0025)));
                return messages;
            }

            return openWorkItemWithReadOnlyMode(session, workspaceId, processId, workItemId);
        }), Message.class);
    }

    public List<Message> closeWorkItem(HttpSession session, String workspaceId, String workItemId)
        throws EarthException {
        List<Message> messages = new ArrayList<>();
        String originWorkItemKey = SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId);
        Integer openProcessMode = null;
        try {
            openProcessMode = (Integer) getDataItemFromSession(session, originWorkItemKey, Session.OPEN_PROCESS_MODE);
        } catch (EarthException e) {
            messages.add(new Message(null, e.getMessage()));
            return messages;
        }

        if (OpenProcessMode.isEdit(openProcessMode.intValue())) {
            this.executeTransaction(workspaceId, () -> {
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.workitemId, workItemId);
                condition.put(qCtlEvent.status, EventStatus.OPEN.name());
                eventDao.delete(workspaceId, condition);

                return true;
            });
        }

        // Clear Session.
        session.removeAttribute(originWorkItemKey);
        return messages;
    }

    /*
     * Open Process with Edit Mode.
     *
     * 1. Create new Event and insert into EventControl with status is
     * Open(WorkItem is locked).</br> 2. Get all WorkItem informations from data
     * tables: </br> WorkItems, FolderItems,Documents, Layers, Annotations,
     * Templates,...</br> 3. Save them into session of current user.</br> 4.
     * Convert tree object to JSON format and update into
     * EventControl(WorkItemData column).</br>
     *
     */
    private List<Message> openWorkItemWithEditMode(HttpSession session, String workspaceId, String processId,
                                                   String workItemId) throws EarthException {
        List<Message> messages = new ArrayList<>();
        // Create new event and insert into EventControl with status is Open.
        // WorkItem is locked.
        CtlEvent ctlEvent = new CtlEvent();
        ctlEvent.setStatus(EventStatus.OPEN.name());
        ctlEvent.setWorkitemId(workItemId);
        ctlEvent.setTaskId(EVENT_TASK);
        UserInfo userInfo = (UserInfo) session.getAttribute(Constant.Session.USER_INFO);
        ctlEvent.setUserId(userInfo.getUserId());
        ctlEvent.setEventId(eEventId.getAutoId());
        eventDao.add(workspaceId, ctlEvent);

        // Get all WorkItem informations from data tables.
        TemplateAccessRight templateAccessRight = TemplateUtil.getAuthorityFromSession(session);
        WorkItemDictionary workItemDics = this.getWorkItemDataStructure(workItemId, workspaceId, templateAccessRight);
        if (workItemDics.size() == 0) {
            messages.add(new Message(ErrorCode.E1013, messageSource.get(ErrorCode.E1013, new String[]{"workItem"})));
            return messages;
        }

        workItemDics.put(Session.OPEN_PROCESS_MODE, OpenProcessMode.EDIT.getMode());

        // Convert tree object to JSON format and update into
        // EventControl(WorkItemData column).
        try {
            ctlEvent.setWorkitemData(
                new ObjectMapper().writeValueAsString(workItemDics.get(EModelUtil.getWorkItemIndex(workItemId))));
            QCtlEvent qCtlEvent = QCtlEvent.newInstance();
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qCtlEvent.workitemId, workItemId);
            condition.put(qCtlEvent.eventId, ctlEvent.getEventId());
            Map<Path<?>, Object> updateMap = new HashMap<>();
            updateMap.put(qCtlEvent.workitemData, ctlEvent.getWorkitemData());
            updateMap.put(qCtlEvent.lastUpdateTime, DateUtil.getCurrentDateString());
            eventDao.update(workspaceId, condition, updateMap);

            // Save them into session of current user.
            session.setAttribute(SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId), workItemDics);
        } catch (JsonProcessingException e) {
            messages.add(new Message(null, e.getMessage()));
        }

        return messages;
    }

    /*
     * Open Process with ReadOnly Mode.
     *
     */
    private List<Message> openWorkItemWithReadOnlyMode(HttpSession session, String workspaceId, String processId,
                                                       String workItemId) throws EarthException {

        List<Message> messages = new ArrayList<>();
        // Get all WorkItem information from data tables.
        TemplateAccessRight templateAccessRight = TemplateUtil.getAuthorityFromSession(session);
        WorkItemDictionary workItemDics = this.getWorkItemDataStructure(workItemId, workspaceId, templateAccessRight);
        if (workItemDics.size() == 0) {
            messages.add(new Message(ErrorCode.E1013, messageSource.get(ErrorCode.E1013, new String[]{"workItem"})));
            return messages;
        }

        workItemDics.put(Session.OPEN_PROCESS_MODE, OpenProcessMode.EDIT.getMode());

        // Save them into session of current user
        session.setAttribute(SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId), workItemDics);

        return messages;
    }

    /**
     * {@inheritDoc}}
     *
     * @throws EarthException
     */
    @Override
    public boolean insertOrUpdateWorkItemToDbFromEvent(String workspaceId, Long threadId, String hostName,
                                                       Integer processServiceID, String userId)
        throws EarthException, IOException {
        if (EStringUtil.isEmpty(workspaceId)) {
            return false;
        }

        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                String transactionToken = this.generateTransactionToken(processServiceID, hostName, threadId);
                boolean isUpdateEventSuccess = eventDao.updateStatusAndTokenEvent(transactionToken, workspaceId);
                CtlEvent event = null;
                if (isUpdateEventSuccess) {
                    event = eventDao.getEventIsEditing(transactionToken, workspaceId, userId);
                }
                if (event == null) {
                    return false;
                }
                WorkItem workItem = new ObjectMapper().readValue(event.getWorkitemData(), WorkItem.class);
                workItem.setWorkspaceId(workspaceId);
                if (EStringUtil.isEmpty(workItem.getWorkitemId())) {
                    insertWorkItemToDb(workItem);
                } else {
                    updateWorkItemToDb(workItem);
                }
                Integer historyNo = workItem.getLastHistoryNo();

                // Insert | update process data
                DatProcess datProcess = workItem.getDataProcess();
                if (datProcess != null) {
                    insertOrUpdateProcessToDb(workspaceId, workItem.getDataProcess(), historyNo);
                }

                List<FolderItem> folderItems = workItem.getFolderItems();
                if (folderItems != null) {
                    insertOrUpdateBatchFolderToDb(workspaceId, workItem.getWorkitemId(), folderItems, historyNo);
                }

                eventDao.deleteEvent(workspaceId, event);
                writeLog(workspaceId, processServiceID, event, workItem.getWorkitemId());
            } catch (IOException e) {
                throw new EarthException(e);
            }
            return true;
        });
    }

    /**
     * writeLog
     *
     * @param workspaceId
     * @param processId
     * @param event
     * @param workItemId
     * @throws EarthException
     */
    private void writeLog(String workspaceId, int processId, CtlEvent event, String workItemId) throws EarthException {
        StrLogAccess logAccess = new StrLogAccess();
        logAccess.setEventId(event.getEventId());
        logAccess.setProcessTime(DateUtil.getCurrentDateString());
        logAccess.setUserId(event.getUserId());
        logAccess.setWorkitemId(workItemId);
        Integer maxHistoryNoLog = strLogAccessDao.getMaxHistoryNo(workspaceId, event.getEventId());
        logAccess.setHistoryNo(++maxHistoryNoLog);
        logAccess.setProcessId(processId);
        logAccess.setProcessVersion(DEFAULT_LOG_VERSION);
        logAccess.setTaskId(event.getTaskId());
        logAccess.setLastUpdateTime(DateUtil.getCurrentDateString());

        strLogAccessDao.add(workspaceId, logAccess);
    }

    /**
     * {@inheritDoc}}
     *
     * @throws EarthException
     */
    private void insertOrUpdateProcessToDb(String workspaceId, DatProcess datProcess, Integer historyNo)
        throws EarthException {
        if ((EStringUtil.isEmpty(workspaceId)) || (datProcess == null)) {
            throw new EarthException("Invalid parameter datProcess or workspaceId is null");
        }
        if (EStringUtil.isEmpty(datProcess.getProcessId())) {
            datProcess.setProcessId(datProcessDao.getMaxId(workspaceId, datProcess.getWorkItemId()));
            datProcessDao.add(workspaceId, datProcess);
        }
        datProcessDao.updateProcess(workspaceId, datProcess);

        // Insert workItem template data
        templateDao.insertProcessTemplateData(workspaceId, datProcess, historyNo);
    }

    /**
     * Insert list folder to database
     *
     * @param workspaceId
     * @param folderItems
     * @return
     * @throws EarthException
     */
    private boolean insertOrUpdateBatchFolderToDb(String workspaceId, String workItemId, List<FolderItem> folderItems,
                                                  Integer historyNo)throws EarthException {
        if (EStringUtil.isEmpty(workspaceId)) {
            throw new EarthException("Invalid parameter workspaceId");
        }
        if (CollectionUtils.isEmpty(folderItems)) {
            return false;
        }
        QFolderItem qFolderItem = QFolderItem.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
        SQLInsertClause sqlInsertFolderItemClause = earthQueryFactory.insert(qFolderItem);
        SQLUpdateClause sqlUpdateFolderItemClause = earthQueryFactory.update(qFolderItem);
        List<SQLInsertClause> batchSQLTemplateData = new ArrayList<>();
        List<SQLInsertClause> batchInsertWorkItemTree = new ArrayList<>();
        List<SQLUpdateClause> batchUpdateWorkItemTree = new ArrayList<>();
        List<SQLDeleteClause> batchDeleteWorkItemTree = new ArrayList<>();
        MgrTemplate mgrTemplate = null;
        SQLInsertClause sqlInsertTemplateDataClause = null;
        for (FolderItem folderItem: folderItems) {
            folderItem.setLastUpdateTime(DateUtil.getCurrentDateString());
            if(EStringUtil.isEmpty(folderItem.getFolderItemNo())) {
                folderItem.setFolderItemNo(String.valueOf(new UniqueIdUtil().createId()));
                folderItem.setWorkitemId(workItemId);
                int maxFolderOrder = folderItemDao.getMaxFolderItemOrder(workspaceId, folderItem.getWorkitemId());
                folderItem.setFolderItemOrder(++maxFolderOrder);
                folderItemDao.addBatchFolderItem(folderItem, sqlInsertFolderItemClause);
                batchInsertWorkItemTree.add(sqlInsertFolderItemClause);
            } else {
                folderItemDao.updateBatchFolderItem(folderItem, sqlUpdateFolderItemClause, qFolderItem);
                batchUpdateWorkItemTree.add(sqlUpdateFolderItemClause);
            }
            mgrTemplate = folderItem.getMgrTemplate();
            TemplateData templateData = folderItem.getFolderItemData();
            if ((mgrTemplate != null) && (templateData != null)) {
                sqlInsertTemplateDataClause = earthQueryFactory.insert(QTemplateData.newInstance(mgrTemplate));
                templateData.setHistoryNo(historyNo);
                earthQueryFactory.insertBatchTemplateData(sqlInsertTemplateDataClause, mgrTemplate
                    , templateData, null, folderItem.getWorkitemId()
                    , folderItem.getFolderItemNo(), null, null);
                batchSQLTemplateData.add(sqlInsertTemplateDataClause);
            }

            // Insert or update document
            insertOrUpdateBatchDocumentToDb(workspaceId, folderItem, historyNo,
                    batchSQLTemplateData, batchInsertWorkItemTree, batchUpdateWorkItemTree, batchDeleteWorkItemTree);
        }
        // Insert folderItem - document- layer
        for(SQLInsertClause sqlInsert : batchInsertWorkItemTree) {
            if (sqlInsert != null && (sqlInsert.getBatchCount() > 0)) {
                sqlInsert.execute();
            }
        }

        // update folderItem - document- layer
        for(SQLUpdateClause sqlUpdate : batchUpdateWorkItemTree) {
            if (sqlUpdate != null && (sqlUpdate.getBatchCount() > 0)) {
                sqlUpdate.execute();
            }
        }

        // Insert template data
        for(SQLInsertClause sqlInsert : batchSQLTemplateData) {
            if (sqlInsert != null && (sqlInsert.getBatchCount() > 0)) {
                sqlInsert.execute();
            }
        }
        return true;
    }

    /**
     * Insert Or Update Batch DocumentToDb
     *
     * @param workspaceId
     * @param folderItem
     * @return
     * @throws EarthException
     */
    private boolean insertOrUpdateBatchDocumentToDb(String workspaceId, FolderItem folderItem, Integer historyNo,
                                                    List<SQLInsertClause> batchSQLTemplateData,
                                                    List<SQLInsertClause> batSQLInsertDocument,
                                                    List<SQLUpdateClause> batSQLUpdateDocument,
                                                    List<SQLDeleteClause> batchDeleteDocument)
        throws EarthException {
        if (EStringUtil.isEmpty(workspaceId) || (batchSQLTemplateData == null)
                || (batSQLInsertDocument == null) || (batSQLUpdateDocument == null)) {
            throw new EarthException("Invalid parameter_crudBatchDocumentToDb");
        }
        if (folderItem == null) {
            return false;
        }
        QDocument qDocument = QDocument.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
        SQLInsertClause sqlInsertClause = earthQueryFactory.insert(qDocument);
        SQLUpdateClause sqlUpdateClause = earthQueryFactory.update(qDocument);
        MgrTemplate mgrTemplate = null;
        SQLInsertClause sqlInsertTemplateDataClause = null;
        List<Document> documents = folderItem.getDocuments();
        if (CollectionUtils.isEmpty(documents)) {
            return true;
        }

        for (Document document: documents) {
            document.setLastUpdateTime(DateUtil.getCurrentDateString());
            if(EStringUtil.isEmpty(document.getDocumentNo())) {
                document.setWorkitemId(folderItem.getWorkitemId());
                document.setFolderItemNo(folderItem.getFolderItemNo());
                document.setDocumentNo(String.valueOf(new UniqueIdUtil().createId()));
                int maxDocumentOrder = documentDao.getMaxDocumentOrder(workspaceId, document.getWorkitemId(),
                        document.getFolderItemNo());
                document.setDocumentOrder(++maxDocumentOrder);
                documentDao.addBatch(document, sqlInsertClause);
                batSQLInsertDocument.add(sqlInsertClause);
            } else {
                documentDao.updateBatch(document, sqlUpdateClause, qDocument);
                batSQLUpdateDocument.add(sqlUpdateClause);
            }

            mgrTemplate = document.getMgrTemplate();
            TemplateData templateData = document.getDocumentData();
            if ((mgrTemplate != null) && (templateData != null)) {
                templateData.setHistoryNo(historyNo);
                sqlInsertTemplateDataClause = earthQueryFactory.insert(QTemplateData.newInstance(mgrTemplate));
                earthQueryFactory.insertBatchTemplateData(sqlInsertTemplateDataClause, mgrTemplate,
                    templateData, null, document.getWorkitemId(),
                    document.getFolderItemNo(), document.getDocumentNo(), null);
                batchSQLTemplateData.add(sqlInsertTemplateDataClause);
            }

            // Insert or update layer
            insertOrUpdateBatchLayerToDb(workspaceId, document, historyNo,
                    batchSQLTemplateData, batSQLInsertDocument, batSQLUpdateDocument, batchDeleteDocument);
        }

        return true;
    }

    /**
     * Insert or update batch layer
     *
     * @param workspaceId
     * @param document
     * @return
     * @throws EarthException
     */
    private boolean insertOrUpdateBatchLayerToDb(String workspaceId, Document document, Integer historyNo,
                                                 List<SQLInsertClause> batSQLTemplateData,
                                                 List<SQLInsertClause> batSQLInsertLayer,
                                                 List<SQLUpdateClause> batSQLUpdateLayer,
                                                 List<SQLDeleteClause> batchDeleteLayer)
            throws EarthException {
        if (EStringUtil.isEmpty(workspaceId) || (batSQLTemplateData == null)
                || (batSQLInsertLayer == null) || (batSQLUpdateLayer == null) || (batchDeleteLayer == null)) {
            throw new EarthException("Invalid parameter: Function update batch layer");
        }
        if (document == null) {
            return false;
        }
        QLayer qLayer = QLayer.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
        SQLInsertClause sqlInsertClause = earthQueryFactory.insert(qLayer);
        SQLUpdateClause sqlUpdateClause = earthQueryFactory.update(qLayer);
        SQLDeleteClause sqlDeleteClause = earthQueryFactory.delete(qLayer);
        MgrTemplate mgrTemplate = null;
        SQLInsertClause sqlInsertTemplateDataClause = null;
        for (Layer layer : document.getLayers()) {
            layer.setLastUpdateTime(DateUtil.getCurrentDateString());
            if (EStringUtil.isEmpty(layer.getLayerNo())) {
                layer.setLayerNo(String.valueOf(new UniqueIdUtil().createId()));
                layer.setWorkitemId(document.getWorkitemId());
                layer.setFolderItemNo(document.getFolderItemNo());
                layer.setDocumentNo(document.getDocumentNo());
                int maxLayerOrder = layerDao.getMaxLayerOrder(workspaceId, document.getWorkitemId(),
                        document.getFolderItemNo(), document.getDocumentNo());
                layer.setLayerOrder(++maxLayerOrder);
                layerDao.addBatch(layer, sqlInsertClause);
                batSQLInsertLayer.add(sqlInsertClause);
            } else if (Action.UPDATE.equals(document.getAction())) {
                layerDao.updateBatch(layer, sqlUpdateClause, qLayer);
                batSQLUpdateLayer.add(sqlUpdateClause);
            } else if (Action.DELETE.equals(document.getAction())) {
                layerDao.deleteBatch(layer, sqlDeleteClause, qLayer);
                batchDeleteLayer.add(sqlDeleteClause);
            }

            mgrTemplate = layer.getMgrTemplate();
            TemplateData templateData = layer.getLayerData();
            if ((mgrTemplate == null) || (templateData == null)) {
                continue;
            }

            sqlInsertTemplateDataClause = earthQueryFactory.insert(QTemplateData.newInstance(mgrTemplate));
            templateData.setHistoryNo(historyNo);
            earthQueryFactory.insertBatchTemplateData(sqlInsertTemplateDataClause, mgrTemplate,
                    templateData, null, layer.getWorkitemId(),
                    layer.getFolderItemNo(), layer.getDocumentNo(), layer.getLayerNo());
            batSQLTemplateData.add(sqlInsertTemplateDataClause);
        }

        return true;
    }

    /**
     * insert work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     */
    private boolean insertWorkItemToDb(WorkItem workItem) throws EarthException {
        if (workItem == null) {
            return false;
        }
        String workspaceId = workItem.getWorkspaceId();
        if (EStringUtil.isEmpty(workspaceId)) {
            throw new EarthException("Invalid workspaceId is null or empty ");
        }
        workItem.setWorkitemId(eWorkItemId.getAutoId());

        Integer maxHistoryNoDb = workItemDao.getMaxHistoryNo(workItem.getWorkspaceId(), workItem.getWorkitemId());
        workItem.setLastHistoryNo(++maxHistoryNoDb);
        workItemDao.add(workspaceId, workItem);

        // Insert workItem template data
        templateDao.insertWorkItemTemplateData(workItem);
        return true;
    }

    /**
     * update work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     */
    private boolean updateWorkItemToDb(WorkItem workItem) throws EarthException {
        if (workItem == null) {
            return false;
        }
        workItemDao.updateWorkItem(workItem);

        // Insert workItem template data
        templateDao.insertWorkItemTemplateData(workItem);
        return true;
    }

    /**
     * Get WorkItem from session
     *
     * @param session     HttpSession object
     * @param workspaceId Id of Workspace
     * @param workItemId  Id of workItem
     * @throws EarthException
     */
    @Override
    public WorkItem getWorkItemSession(HttpSession session, String workspaceId, String workItemId)
        throws EarthException {
        // Get workItem data from session.
        WorkItem workItemSession = (WorkItem) getDataItemFromSession(session,
            SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
            EModelUtil.getWorkItemIndex(workItemId));

        checkPermission(workItemSession, Operation.GET_DATA);
        WorkItem workItem = EModelUtil.clone(workItemSession, WorkItem.class);
        workItem.setFolderItems(new ArrayList<FolderItem>());
        return workItem;
    }

    @Override
    public boolean updateWorkItemSession(HttpSession session, String workspaceId, WorkItem workItem)
        throws EarthException {
        // Get WorkItem data from session.
        WorkItem workItemSession = (WorkItem) getDataItemFromSession(
            session,
            SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItem.getWorkitemId()),
            EModelUtil.getWorkItemIndex(workItem.getWorkitemId()));

        checkPermission(workItemSession, Operation.UPDATE_DATA);

        // Update work item data.
        workItemSession.setWorkItemData(workItem.getWorkItemData());
        workItemSession.setMgrTemplate(workItem.getMgrTemplate());
        workItemSession.setTemplateId(workItem.getTemplateId());
        workItemSession.setTaskId(workItem.getTaskId());

        return true;
    }

    @Override
    public WorkItem getWorkItemStructureSession(HttpSession session, String workspaceId, String workItemId)
        throws EarthException {
        // Get WorkItem data from session.
        WorkItem workItemSession = (WorkItem) getDataItemFromSession(
            session,
            SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
            EModelUtil.getWorkItemIndex(workItemId));

        checkPermission(workItemSession, Operation.GET_DATA);

        WorkItem workItem = EModelUtil.clone(workItemSession, WorkItem.class);

        // Set workItem data is null.
        workItem.setWorkItemData(null);

        // set folder items, documents, layers data in workItem is null.
        List<FolderItem> folderItems = workItem.getFolderItems();
        List<FolderItem> noneDisplayfolderItems = new ArrayList<>();
        for (FolderItem folderItem : folderItems) {
            if (AccessRight.NONE.eq(folderItem.getAccessRight())) {
                noneDisplayfolderItems.add(folderItem);
            } else {
                folderItem.setFolderItemData(null);
                List<Document> documents = folderItem.getDocuments();
                List<Document> noneDisplayDocuments = new ArrayList<>();
                for (Document document : documents) {
                    if (AccessRight.NONE.eq(document.getAccessRight())) {
                        noneDisplayDocuments.add(document);
                    } else {
                        document.setDocumentData(null);
                        List<Layer> layers = document.getLayers();
                        List<Layer> noneDisplayLayers = new ArrayList<>();
                        for (Layer layer : layers) {
                            if (AccessRight.NONE.eq(layer.getAccessRight())) {
                                noneDisplayLayers.add(layer);
                            } else {
                                layer.setLayerData(null);
                            }
                        }

                        // Remove all layers have access right is None.
                        layers.removeAll(noneDisplayLayers);
                    }
                }

                // Remove all documents have access right is None.
                documents.removeAll(noneDisplayDocuments);
            }
        }

        // Remove all Folder Items have access right is None.
        folderItems.removeAll(noneDisplayfolderItems);

        return workItem;
    }

    @Override
    public List<WorkItem> searchWorkItems(HttpSession session, String workspaceId, WorkItem searchCondition)
        throws EarthException {
        return ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            QWorkItem qWorkItem = QWorkItem.newInstance();

            List<WorkItem> workItemList = null;
            // Create search condition
            if (searchCondition != null) {
                BooleanBuilder condition = new BooleanBuilder();
                if (!EStringUtil.isEmpty(searchCondition.getWorkitemId())) {
                    condition.and(qWorkItem.workitemId.eq(searchCondition.getWorkitemId()));
                }

                if (searchCondition.getLastHistoryNo() > 0) {
                    condition.and(qWorkItem.lastHistoryNo.eq(searchCondition.getLastHistoryNo()));
                }

                if (!EStringUtil.isEmpty(searchCondition.getTaskId())) {
                    condition.and(qWorkItem.taskId.eq(searchCondition.getTaskId()));
                }

                if (!EStringUtil.isEmpty(searchCondition.getTemplateId())) {
                    condition.and(qWorkItem.templateId.eq(searchCondition.getTemplateId()));
                }

                if (!EStringUtil.isEmpty(searchCondition.getLastUpdateTime())) {
                    condition.and(qWorkItem.lastUpdateTime.eq(searchCondition.getLastUpdateTime()));
                }

                workItemList = workItemDao.search(workspaceId, condition);
            }

            // If Search condition is null search all data.
            workItemList = workItemDao.findAll(workspaceId);

            // Get list of templates.
            List<String> templateIds = new ArrayList<>();
            for (WorkItem workItem : workItemList) {
                if (!EStringUtil.isEmpty(workItem.getTemplateId())) {
                    templateIds.add(workItem.getTemplateId());
                }
            }

            Map<String, MgrTemplate> templates = null;
            if (templateIds.size() > 0) {
                templates = templateDao.getTemplates(workspaceId, templateIds);
            }

            for (WorkItem workItem : workItemList) {
                if (!EStringUtil.isEmpty(workItem.getTemplateId())) {
                    workItem.setMgrTemplate(templates.get(workItem.getTemplateId()));
                }
            }
            return workItemList;

        }), WorkItem.class);
    }

    @Override
    public List<WorkItemListDTO> getWorkItemsByWorkspace(String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.SYSTEM_WORKSPACE_ID, () -> {
            return workItemDao.getWorkItemsByWorkspace(workspaceId);
        }), WorkItemListDTO.class);
    }

    @Override
    public List<WorkItemListDTO> getWorkItemsByWorkspace(String workspaceId, List<String> workItemIds)
        throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.SYSTEM_WORKSPACE_ID, () -> {
            return workItemDao.getWorkItemsByWorkspace(workspaceId, workItemIds);
        }), WorkItemListDTO.class);
    }

    @Override
    public boolean unlock(List<String> workItemId, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return workItemDao.unlock(workItemId, workspaceId) > 0;
        });
    }

    public List<Row> getRowValue(SearchForm searchForm, List<Column> columns, String workspaceId,
                                 String stringCondition) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.SYSTEM_WORKSPACE_ID, () -> {
            return workItemDao.getRowValue(searchForm, columns, workspaceId, stringCondition);
        }), Row.class);
    }

    @Override
    public Map<String, Object> getRowValues(SearchByColumnsForm searchByColumnsForm, String workspaceId) throws
        EarthException {

        MgrTemplate mgrTemplate = (MgrTemplate) executeTransaction(workspaceId, () -> {
            return templateDao.getTemplate(workspaceId, searchByColumnsForm.getTemplateId());
        });
        List<Row> rowList = new ArrayList<Row>();
        Map<String, Object> map = new HashMap<>();
        List<Column> columns = new ArrayList<>();
        if (mgrTemplate != null) {

            QTemplateData qTemplateData = QTemplateData.newInstance(mgrTemplate);
            SearchColumn searchColumn = new SearchColumn();
            BooleanBuilder condition =
                searchColumn.searchColumns(qTemplateData, searchByColumnsForm.getValid(),
                    searchByColumnsForm.getSearchByColumnForms());
            List<TemplateData> templateDataList =
                ConversionUtil.castList(executeTransaction(workspaceId, () -> {
                    return templateDao.getTemplateDataList(workspaceId, mgrTemplate, condition,
                        searchByColumnsForm.getSkip(), searchByColumnsForm.getLimit(), null);
                }), TemplateData.class);

            List<Field> fields = mgrTemplate.getTemplateFields();
            for (Field field : fields) {
                Column column = new Column();
                column.setName(field.getName());
                column.setType(ConversionUtil.castObject(Type.getLabel(field.getType()), String.class));
                column.setDescription(field.getDescription());
                column.setEncrypted(field.isEncrypted());
                columns.add(column);
            }
            for (TemplateData templateData : templateDataList) {
                List<Column> listCol = new ArrayList<>();
                Map<String, Object> mapTemplate = templateData.getDataMap();
                for (int i = 0; i < columns.size(); i++) {
                    String name = columns.get(i).getName();
                    boolean encrypted = columns.get(i).getEncrypted();
                    Column col = new Column();
                    col.setName(name);
                    String value = ConversionUtil.castObject(mapTemplate.get(name).toString(), String.class);
                    if (encrypted) {
                        try {
                            value = CryptUtil.decryptData(value);
                        } catch (Exception ex) {
                            LOG.error(ex.getMessage());
                        }
                    }
                    col.setValue(value);
                    listCol.add(col);
                }
                Row row = new Row();
                row.setWorkitemId((String) mapTemplate.get(ColumnNames.WORKITEM_ID.toString()));
                row.setColumns(listCol);
                rowList.add(row);
            }
        }
        map.put("columns", columns);
        map.put("rows", rowList);
        return map;
    }

    @Override
    public Integer getProcessIdByWorkItem(String workspaceId, String workitemId) throws EarthException {
        return (Integer) executeTransaction(Constant.SYSTEM_WORKSPACE_ID, () -> {
            return workItemDao.getProcessIdByWorkItem(workspaceId, workitemId);
        });
    }

    @Override
    public boolean closeAndSaveWorkItem(HttpSession session, String workItemId, String workspaceId)
        throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            try {
                String originWorkItemKey = SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId);
                Integer openProcessMode = (Integer) getDataItemFromSession(session, originWorkItemKey,
                    Session.OPEN_PROCESS_MODE);
                if (OpenProcessMode.isReadOnly(openProcessMode.intValue())) {
                    throw new EarthException(messageSource.get(ErrorCode.E1011, new String[]{"workItem"}));
                }

                // Get all WorkItem informations from session.
                WorkItem workItemSession = (WorkItem) getDataItemFromSession(session, originWorkItemKey,
                    EModelUtil.getWorkItemIndex(workItemId));

                checkPermission(workItemSession, Operation.UPDATE_DATA);

                String jsonWorkItem = new ObjectMapper().writeValueAsString(workItemSession);

                // Update Event Control.
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.workitemId, workItemId);
                Map<Path<?>, Object> updateMap = new HashMap<>();
                updateMap.put(qCtlEvent.workitemData, jsonWorkItem);
                updateMap.put(qCtlEvent.status, EventStatus.EDIT.name());
                boolean result = (eventDao.update(workspaceId, condition, updateMap) > 0);

                // Clear session.
                session.removeAttribute(SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId));
                return result;
            } catch (JsonProcessingException e) {
                throw new EarthException(e);
            }
        });
    }

    private WorkItemDictionary getWorkItemDataStructure(String workItemId, String workspaceId,
                                                        TemplateAccessRight templateAccessRight) throws EarthException {
        return (WorkItemDictionary) this.executeTransaction(workspaceId, () -> {
            // Get WorkItem structure without Template.
            Map<WorkItem, List<String>> workItemMap = workItemDao.getWorkItemStructure(workspaceId, workItemId);
            Map.Entry<WorkItem, List<String>> entry = workItemMap.entrySet().iterator().next();
            WorkItem workItem = entry.getKey();

            // Get list of templates in workItem.
            List<String> templateIds = entry.getValue();
            Map<String, MgrTemplate> templates = templateDao.getTemplates(workspaceId, templateIds);

            // Get max history no of WorkItem.
            int lastHistoryNo = workItemDao.getMaxHistoryNo(workspaceId, workItemId);

            // Get list of data on templates.
            Map<String, TemplateData> templateDataMap = templateDao.getTemplateDataMap(workspaceId, workItemId,
                templates, lastHistoryNo);

            // Creating WorkItem dictionary.
            WorkItemDictionary workItemDics = createWorkItemDictionaries(workItem, templates, templateDataMap,
                templateAccessRight);

            return workItemDics;
        });
    }

    /**
     * Generate transaction token
     *
     * @param processServiceID
     * @param hostName
     * @param threadId
     * @return
     */
    private String generateTransactionToken(Integer processServiceID, String hostName, Long threadId) {
        StringBuilder stringBuilder = new StringBuilder(processServiceID);
        stringBuilder.append(hostName);
        stringBuilder.append(threadId);
        stringBuilder.append(DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS));
        return stringBuilder.toString();
    }

}
