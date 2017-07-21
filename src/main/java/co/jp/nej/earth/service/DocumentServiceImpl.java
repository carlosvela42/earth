package co.jp.nej.earth.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.DataDbDao;
import co.jp.nej.earth.dao.DataFileDao;
import co.jp.nej.earth.dao.DirectoryDao;
import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentSavingInfo;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.StrageFile;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.WorkItemDictionary;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.StrDataDb;
import co.jp.nej.earth.model.entity.StrDataFile;
import co.jp.nej.earth.model.enums.Action;
import co.jp.nej.earth.model.enums.DocumentSavingType;
import co.jp.nej.earth.model.enums.Operation;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QStrDataDb;
import co.jp.nej.earth.model.sql.QStrDataFile;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.model.ws.DisplayImageResponse;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EModelUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.FileUtil;
import co.jp.nej.earth.util.SessionUtil;

@Service
public class DocumentServiceImpl extends BaseService implements DocumentService {
    @Autowired
    private DataFileDao dataFileDao;
    @Autowired
    private DataDbDao dataDbDao;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private DirectoryDao directoryDao;
    @Autowired
    private StrageFileDao strageFileDao;
    @Autowired
    private ProcessDao processDao;
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventControlServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveDocument(String workspaceId, Document document, DocumentSavingInfo documentSavingInfo)
            throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                boolean result = false;
                List<Directory> directories = documentSavingInfo.getDataDef();
                File file = new File(document.getDocumentPath());
                if (DocumentSavingType.FILE_UNTIL_FULL.equals(documentSavingInfo.getSavingType())) {
                    // loop directories
                    for (Directory directory : directories) {
                        File directoryFile = new File(directory.getFolderPath());
                        // check size of file input and all file in directory
                        // are small
                        // than reserved disk volume size or not
                        if (Long.parseLong(directory.getReservedDiskVolSize()) > (FileUtil.getFileSize(file)
                                + FileUtil.getDirectorySize(directoryFile))) {
                            File fileOutPut = new File(
                                    directoryFile.getAbsolutePath() + File.separator + file.getName());
                            copyFileToDirectory(document, file, fileOutPut);
                            result = saveDocumentToDataFile(workspaceId, document, fileOutPut);
                        }
                    }
                } else if (DocumentSavingType.FILE_ROUND_ROBIN.equals(documentSavingInfo.getSavingType())) {
                    Directory directory = FileUtil.getDirectoryUsedStorageMin(directories);
                    File fileOutPut = new File(directory.getFolderPath() + File.separator + file.getName());
                    copyFileToDirectory(document, file, fileOutPut);
                    result = saveDocumentToDataFile(workspaceId, document, fileOutPut);
                } else if (DocumentSavingType.DATABASE.equals(documentSavingInfo.getSavingType())) {
                    try {
                        result = saveDocumentToDB(workspaceId, file, document);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new EarthException(e);

                    }
                }
                deleteFile(file);
                if (!result) {
                    LOG.error(new Message("",
                            messageSource.getMessage(ErrorCode.E1012, new String[]{""}, Locale.ENGLISH))
                            .getContent());
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
    public byte[] getDocument(String workspaceId, Document document, DocumentSavingInfo documentSavingInfo)
            throws EarthException {
        return (byte[]) this.executeTransaction(workspaceId, () -> {
            try {
                if (DocumentSavingType.DATABASE.equals(documentSavingInfo.getSavingType())) {
                    QStrDataDb qStrDataDb = QStrDataDb.newInstance();
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qStrDataDb.documentNo, document.getDocumentNo());
                    condition.put(qStrDataDb.folderItemNo, document.getFolderItemNo());
                    condition.put(qStrDataDb.workitemId, document.getWorkitemId());
                    StrDataDb dataDb = dataDbDao.findOne(workspaceId, condition);
                    return dataDb.getDocumentData().getBytes();
                } else {
                    QStrDataFile qStrDataFile = QStrDataFile.newInstance();
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qStrDataFile.documentNo, document.getDocumentNo());
                    condition.put(qStrDataFile.folderItemNo, document.getFolderItemNo());
                    condition.put(qStrDataFile.workitemId, document.getWorkitemId());
                    StrDataFile dataFile = dataFileDao.findOne(workspaceId, condition);
                    if (!EStringUtil.isEmpty(dataFile.getDocumentDataPath())) {
                        return FileUtil.convertFileToBinary(new File(dataFile.getDocumentDataPath()));
                    }
                    return null;
                }
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * copy file to directory
     *
     * @param document
     * @param file
     * @throws EarthException
     */
    private void copyFileToDirectory(Document document, File file, File fileOutPut) throws EarthException {
        try {
            Files.copy(file, new FileOutputStream(fileOutPut));
        } catch (IOException e) {
            throw new EarthException(e);
        }

    }

    /**
     * save Document to table DataFile in database
     *
     * @param document
     * @param fileOutPut
     * @return
     * @throws EarthException
     */
    private boolean saveDocumentToDataFile(String workspaceId, Document document, File fileOutPut)
            throws EarthException {
        QStrDataFile qStrDataFile = QStrDataFile.newInstance();
        if (EStringUtil.isEmpty(document.getDocumentNo())) {
            // instance object
            StrDataFile dataFile = new StrDataFile();
            dataFile.setWorkitemId(document.getWorkitemId());
            dataFile.setDocumentNo(document.getDocumentNo());
            dataFile.setFolderItemNo(document.getFolderItemNo());
            dataFile.setDocumentDataPath(fileOutPut.getAbsolutePath());
            // insert DataFile
            return dataFileDao.add(workspaceId, dataFile) > 0;
        } else {
            // instance condition map
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qStrDataFile.documentNo, document.getDocumentNo());
            condition.put(qStrDataFile.folderItemNo, document.getFolderItemNo());
            condition.put(qStrDataFile.workitemId, document.getWorkitemId());
            // instance update map
            Map<Path<?>, Object> updateMap = new HashMap<>();
            updateMap.put(qStrDataFile.documentDataPath, document.getDocumentPath());
            updateMap.put(qStrDataFile.lastUpdateTime, DateUtil.getCurrentDateString());
            return dataFileDao.update(workspaceId, condition, updateMap) > 0;
        }
    }

    /**
     * save document to database
     *
     * @param file
     * @param document
     * @throws EarthException
     * @throws IOException
     */
    private boolean saveDocumentToDB(String workspaceId, File file, Document document)
            throws EarthException, IOException {
        byte[] dataFile = FileUtil.convertFileToBinary(file);
        if (EStringUtil.isEmpty(document.getDocumentNo())) {
            StrDataDb dataDb = new StrDataDb();
            dataDb.setWorkitemId(document.getWorkitemId());
            dataDb.setDocumentNo(document.getDocumentNo());
            dataDb.setFolderItemNo(document.getFolderItemNo());
            dataDb.setDocumentData(new String(dataFile));
            // insert dataDb
            return dataDbDao.add(workspaceId, dataDb) > 0;
        } else {
            QStrDataDb qStrDataDb = QStrDataDb.newInstance();
            // instance condition map
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qStrDataDb.documentNo, document.getDocumentNo());
            condition.put(qStrDataDb.folderItemNo, document.getFolderItemNo());
            condition.put(qStrDataDb.workitemId, document.getWorkitemId());
            // instance update map
            Map<Path<?>, Object> updateMap = new HashMap<>();
            updateMap = new HashMap<>();
            updateMap.put(qStrDataDb.documentData, new String(dataFile));
            updateMap.put(qStrDataDb.lastUpdateTime, DateUtil.getCurrentDateString());
            // update DataDb
            return dataDbDao.update(workspaceId, condition, updateMap) > 0;
        }
    }

    /**
     * delete File
     *
     * @param file
     * @return
     */
    private boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO because have QA need customer answers
    public DocumentSavingInfo getDocumentSavingInfo(String workspaceId, Integer processId) throws EarthException {
        return (DocumentSavingInfo) this.executeTransaction(workspaceId, () -> {
            try {
                DocumentSavingInfo documentSavingInfo = new DocumentSavingInfo();
                QMgrProcess qMgrProcess = QMgrProcess.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrProcess.processId, processId);
                MgrProcess process = processDao.findOne(workspaceId, condition);
                JSONObject json = new JSONObject(process.getProcessDefinition());
                // TODO because didn't know xml file's format
                process.setProcessDefinition(XML.toString(json));
                Integer siteId = Integer.parseInt(json.get("siteId").toString());
                QStrageFile qStrageFile = QStrageFile.newInstance();
                condition = new HashMap<>();
                condition.put(qStrageFile.siteId, siteId);
                condition.put(qStrageFile.processId, processId);
                StrageFile strageFile = strageFileDao.findOne(workspaceId, condition);
                // TODO
                if (strageFile != null) {
                    if (DocumentSavingType.FILE_UNTIL_FULL.getValue()
                            .equalsIgnoreCase(strageFile.getSiteManagementType())) {
                        documentSavingInfo.setSavingType(DocumentSavingType.FILE_UNTIL_FULL);
                    } else {
                        documentSavingInfo.setSavingType(DocumentSavingType.FILE_ROUND_ROBIN);
                    }
                } else {
                    documentSavingInfo.setSavingType(DocumentSavingType.DATABASE);
                }
                documentSavingInfo.setDataDef(directoryDao.getDirectoriesBySite((int) siteId));
                return documentSavingInfo;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * Get Document object from session
     *
     * @param session
     * @param workspaceId
     * @param workItemId
     * @param folderItemNo
     * @param documentNo
     * @return
     * @throws EarthException
     */
    @Override
    public Document getDocumentSession(HttpSession session, String workspaceId, String workItemId, String folderItemNo,
                                       String documentNo) throws EarthException {
        // Get Document data from session.
        Document documentSession = (Document) getDataItemFromSession(session,
                SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getDocumentIndex(workItemId, String.valueOf(folderItemNo), String.valueOf(documentNo)));
        checkPermission(documentSession, Operation.GET_DATA);
        Document document = EModelUtil.clone(documentSession, Document.class);
        document.setLayers(new ArrayList<Layer>());
        return document;
    }

    @Override
    public boolean updateDocumentSession(HttpSession session, String workspaceId, String workItemId,
                                         String folderItemNo, Document document) throws EarthException {
        Document documentSession = (Document) getDataItemFromSession(session,
                SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId), EModelUtil.getDocumentIndex(
                        workItemId, folderItemNo, document.getDocumentNo()));
        checkPermission(documentSession, Operation.UPDATE_DATA);
        documentSession.setTemplateId(document.getTemplateId());
        documentSession.setDocumentData(document.getDocumentData());
        documentSession.setMgrTemplate(document.getMgrTemplate());
        documentSession.setAction(Action.UPDATE.getAction());
        return true;
    }

    private FolderItem getFolderItemDataStructureSession(HttpSession session, String workspaceId, String workItemId,
                                                         String folderItemNo) throws EarthException {
        // Get FolderItem data from session
        FolderItem folderItemSession = (FolderItem) getDataItemFromSession(session,
                SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getFolderItemIndex(workItemId, folderItemNo));
        checkPermission(folderItemSession, Operation.GET_DATA);
        FolderItem folderItem = EModelUtil.clone(folderItemSession, FolderItem.class);
        return folderItem;
    }

    @Override
    public DisplayImageResponse displayImage(HttpSession session, String workspaceId, String workItemId,
                                             String folderItemNo, String currentDocumentNo) throws EarthException {
        WorkItemDictionary workItemTempDictionary = (WorkItemDictionary) session
                .getAttribute(SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId));
        if (workItemTempDictionary == null) {
            FolderItem folderItem = getFolderItemDataStructureSession(session, workspaceId, workItemId, folderItemNo);

            WorkItem originWorkItemSession = (WorkItem) getDataItemFromSession(session,
                    SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                    EModelUtil.getWorkItemIndex(workItemId));

            WorkItem workItem = EModelUtil.clone(originWorkItemSession, WorkItem.class);
            workItem.setFolderItems(new ArrayList<>());
            workItem.addFolderItem(folderItem);

            // Create new temporary WorkItem Dictionary for Displaying ImageViewer Screen.
            WorkItemDictionary workItemDictionary = createWorkItemDictionaries(workItem);
            session.setAttribute(SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId), workItemDictionary);
        }

        FolderItem tempFolderItem = (FolderItem) getDataItemFromSession(session,
                SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getFolderItemIndex(workItemId, folderItemNo));
        List<Document> documents = tempFolderItem.getDocuments();
        Map<String, Document> documentMap = new LinkedHashMap<>();
        int currentDocumentIndex = 0;
        for (Document document : documents) {
            documentMap.put(document.getDocumentNo(), document);
            if (currentDocumentNo.equals(document.getDocumentNo())) {
                currentDocumentIndex = documents.indexOf(document);
            }
        }

        return new DisplayImageResponse(currentDocumentIndex, documentMap);
    }

    @Override
    public byte[] getBinaryDataOfDocument(String workspaceId, Document document) throws EarthException {
        return (byte[]) this.executeTransaction(workspaceId, () -> {
            try {
                if (!EStringUtil.isEmpty(document.getDocumentPath())) {
                    File file = new File(document.getDocumentPath());
                    if (file.exists()) {
                        return FileUtil.convertFileToBinary(file);
                    }
                }

                QStrDataDb qStrDataDb = QStrDataDb.newInstance();
                Map<Path<?>, Object> keyMap = new HashMap<>();
                keyMap.put(qStrDataDb.workitemId, document.getWorkitemId());
                keyMap.put(qStrDataDb.folderItemNo, document.getFolderItemNo());
                keyMap.put(qStrDataDb.documentNo, document.getDocumentNo());
                StrDataDb dataDb = dataDbDao.findOne(workspaceId, keyMap);
                if (dataDb != null) {
                    if (!EStringUtil.isEmpty(dataDb.getDocumentData())) {
                        return Base64.decodeBase64(dataDb.getDocumentData());
                    }
                }
            } catch (Exception e) {
                throw new EarthException(e);
            }
            return null;
        });
    }

    @Override
    public boolean saveImageSession(HttpSession session, String workspaceId, Document document) throws EarthException {
        // Get work item data from session.
        Document documentSession = (Document) getDataItemFromSession(
                session
                ,SessionUtil.getTempWorkItemDictionaryKey(workspaceId, document.getWorkitemId())
                ,EModelUtil.getDocumentIndex(
                        document.getWorkitemId()
                        ,String.valueOf(document.getFolderItemNo())
                        ,String.valueOf(document.getDocumentNo())));

        documentSession.setLayers(document.getLayers());
        documentSession.setAction(Action.UPDATE.getAction());

        return true;
    }

    @Override
    public boolean closeImage(HttpSession session, String workspaceId, String workItemId, String folderItemNo,
                              String documentNo) throws EarthException {
        // Get Origin Document Session.
        Document orginDocument = (Document) getDataItemFromSession(
                session,
                SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getDocumentIndex(workItemId, folderItemNo, documentNo));

        // Replace document into temporary session.
        setDataItemToSession(
                session
                ,SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getDocumentIndex(workItemId, folderItemNo, documentNo),
                orginDocument);
        return true;
    }

    @Override
    public boolean saveAndCloseImages(HttpSession session, String workspaceId, String workItemId, String folderItemNo)
            throws EarthException {
        FolderItem tempFolderItemSession = (FolderItem) getDataItemFromSession(session,
                SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getFolderItemIndex(workItemId, folderItemNo));

        // Get Origin WorkItem Dictionary.
        WorkItemDictionary originWorkItemDictionary = (WorkItemDictionary) session
                .getAttribute(SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId));
        // Save document image session into origin document image session.
        List<Document> tempDocuments = tempFolderItemSession.getDocuments();
        for (Document doc : tempDocuments) {
            List<Layer> tempLayers = doc.getLayers();
            for (Layer tempLayer : tempLayers) {
                Layer newLayer = EModelUtil.clone(tempLayer, Layer.class);
                // In case create new Layer.
                if (newLayer.getLayerNo() == null) {
                    originWorkItemDictionary.put(
                            EModelUtil.getLayerIndex(
                                    tempLayer.getWorkitemId(), tempLayer.getFolderItemNo(),
                                    tempLayer.getDocumentNo(), tempLayer.getLayerNo()),
                            newLayer);
                } else {
                    // In case Update Layer.
                    Layer originLayer =  (Layer) originWorkItemDictionary.get(
                            EModelUtil.getLayerIndex(
                                    tempLayer.getWorkitemId(), tempLayer.getFolderItemNo(),
                                    tempLayer.getDocumentNo(), tempLayer.getLayerNo()));
                    originLayer.setLayerName(newLayer.getLayerName());
                    originLayer.setAnnotations(newLayer.getAnnotations());
                    originLayer.setAction(Action.UPDATE.getAction());
                }
            }
        }

        session.removeAttribute(SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId));
        return true;
    }

    @Override
    public boolean closeWithoutSavingImage(HttpSession session, String workspaceId, String workItemId)
            throws EarthException {
        session.removeAttribute(SessionUtil.getTempWorkItemDictionaryKey(workspaceId, workItemId));
        return true;
    }

    @Override
    public String getThumbnail(HttpSession session, String workspaceId, String workItemId, String folderItemNo,
                               String documentNo) throws EarthException {
        // Get Document data from session.
        Document documentSession = (Document) getDataItemFromSession(session,
                SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
                EModelUtil.getDocumentIndex(workItemId, String.valueOf(folderItemNo), String.valueOf(documentNo)));
        List<Layer> layers = documentSession.getLayers();
        if (layers.size() == 0) {
            return EStringUtil.EMPTY;
        }

        StringBuilder thumbernail = new StringBuilder();
        for (Layer layer : layers) {
            if (!EStringUtil.isEmpty(layer.getAnnotations())) {
                thumbernail.append(layer.getAnnotations());
            }
        }

        return thumbernail.toString();
    }
}
