package co.jp.nej.earth.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import co.jp.nej.earth.dao.DocumentDao;
import co.jp.nej.earth.dao.LayerDao;
import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentImageKey;
import co.jp.nej.earth.model.DocumentResponse;
import co.jp.nej.earth.model.DocumentSavingInfo;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.StrageFile;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.StrDataDb;
import co.jp.nej.earth.model.entity.StrDataFile;
import co.jp.nej.earth.model.enums.DocumentSavingType;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QStrDataDb;
import co.jp.nej.earth.model.sql.QStrDataFile;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.FileUtil;
import co.jp.nej.earth.util.ImageUtil;

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
    private DocumentDao documentDao;
    @Autowired
    private LayerDao layerDao;
    @Autowired
    private EMessageResource eMessageResource;
    @Autowired
    private ProcessDao processDao;

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 500;
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
                            result = saveDocument2DataFile(workspaceId, document, fileOutPut);
                        }
                    }
                } else if (DocumentSavingType.FILE_ROUND_ROBIN.equals(documentSavingInfo.getSavingType())) {
                    Directory directory = FileUtil.getDirectoryUsedStorageMin(directories);
                    File fileOutPut = new File(directory.getFolderPath() + File.separator + file.getName());
                    copyFileToDirectory(document, file, fileOutPut);
                    result = saveDocument2DataFile(workspaceId, document, fileOutPut);
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
                            messageSource.getMessage(ErrorCode.E1012, new String[] { "" }, Locale.ENGLISH))
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
     * @param directoryFile
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
    private boolean saveDocument2DataFile(String workspaceId, Document document, File fileOutPut)
            throws EarthException {
        QStrDataFile qStrDataFile = QStrDataFile.newInstance();
        if (document.getDocumentNo() == -1) {
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
        if (document.getDocumentNo() == -1) {
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

    @Override
    public List<Document> getDocumentListInfo(String workspaceId, String workitemId, int folderItemNo,
            String documentNo) throws EarthException {
        return ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            return documentDao.getAllByWorkspace(workspaceId, workitemId, folderItemNo, documentNo);
        }), Document.class);
    }

    @Override
    public RestResponse getDocumentList(String workspaceId, String workitemId, int folderItemNo, int documentNo,
            String action) throws EarthException {
        RestResponse respone = new RestResponse();
        List<Document> lstDocument = ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            return documentDao.getAll(workspaceId, workitemId, folderItemNo);
        }), Document.class);
        Document doc = null;
        int i = 0, rowNum = -1;
        do {
            int ss = lstDocument.get(i).getDocumentNo();
            if (ss==documentNo) {
                rowNum = i;
            }
            i++;
        } while (rowNum < 0&&i<lstDocument.size());
        switch(action){
        case "first":
            rowNum = 0;
            break;
        case "previous":
            rowNum--;
            break;
        case "next":
            rowNum++;
            break;
        case "last":
            rowNum = lstDocument.size()-1;
            break;
        default:
            break;
        }
        doc = lstDocument.get(rowNum);
        int tmpDocNo = doc.getDocumentNo();
        List<Layer> lstLayer = ConversionUtil.castList(this.executeTransaction(workspaceId, () -> {
            return layerDao.getAll(workspaceId, workitemId, folderItemNo, tmpDocNo);
        }), Layer.class);
        doc.setLayers(lstLayer);
        DocumentResponse docRes = new DocumentResponse(doc,rowNum,lstDocument.size());
        respone.setResult(true);
        respone.setData(docRes);
        return respone;
    }

    @Override
    public RestResponse getDocument(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException {
        RestResponse respone = new RestResponse();
        // get work item data from session
        WorkItem workItem = null;
        try {
            workItem = (WorkItem) session.getAttribute("ORIGIN" + workspaceId + "&" + workitemId);
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "session" }));
            return respone;
        }
        // from work item data find document data
        Document document = getDocumentFromWorkItem(workItem, workitemId, folderItemNo, documentNo);
        // if don't find any folder item return message
        if (document == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "document" }));
            return respone;
        }
        // remove information of document
        document.setLayers(null);
        respone.setData(document);
        return respone;
    }

    @Override
    public RestResponse updateDocument(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Document document) throws EarthException {
        RestResponse respone = new RestResponse();
        // get work item data from session
        WorkItem workItem = null;
        try {
            workItem = (WorkItem) session.getAttribute("ORIGIN" + workspaceId + "&" + workitemId);
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "session" }));
            return respone;
        }
        // from work item data find document data and update value of this
        List<FolderItem> folderItems = workItem.getFolderItems();
        boolean isDocumentExisted = false;
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                if (folderItem.getWorkitemId().equals(workitemId)
                        && folderItem.getFolderItemNo().equals(folderItemNo)) {
                    List<Document> documents = folderItem.getDocuments();
                    if (documents != null) {
                        for (Document temp : documents) {
                            if (temp.getWorkitemId().equals(workitemId) && temp.getFolderItemNo().equals(folderItemNo)
                                    && temp.getDocumentNo().equals(document.getDocumentNo())) {
                                temp.setTemplateId(document.getTemplateId());
                                temp.setDocumentData(document.getDocumentData());
                                temp.setDocumentBinary(document.getDocumentBinary());
                                temp.setDocumentPath(document.getDocumentPath());
                                temp.setDocumentType(document.getDocumentType());
                                temp.setLastUpdateTime(document.getLastUpdateTime());
                                temp.setPageCount(document.getPageCount());
                                temp.setViewInformation(document.getViewInformation());
                                // TODO don't know what is the value of action
                                // update
                                temp.setAction(0);
                                isDocumentExisted = true;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (!isDocumentExisted) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "document" }));
            return respone;
        }
        // set workitem data after update to session
        session.setAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        return respone;
    }

    @Override
    public RestResponse displayImage(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException {
        RestResponse respone = new RestResponse();
        // get work item data from session
        WorkItem workItem = null;
        try {
            workItem = (WorkItem) session.getAttribute("ORIGIN" + workspaceId + "&" + workitemId);
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "session" }));
            return respone;
        }
        // from work item data find document data
        Document document = getDocumentFromWorkItem(workItem, workitemId, folderItemNo, documentNo);
        // if don't find any folder item return message
        if (document == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "document" }));
            return respone;
        }
        WorkItem workItem2 = null;
        try {
            workItem2 = (WorkItem) session.getAttribute("TMP" + workspaceId + "&" + workitemId);
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem2 == null) {
            try {
                document.setDocumentBinary(getBinaryDataOfDocument(workspaceId, document));
            } catch (Exception e) {
                throw new EarthException(e);
            }
            // set workitem data after update to session
            session.setAttribute("TMP" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        } else {
            document = getDocumentFromWorkItem(workItem2, workitemId, folderItemNo, documentNo);
        }
        respone.setData(document);
        return respone;
    }

    /**
     * get document from workitem
     *
     * @param workItem
     * @param workitemId
     * @param folderItemNo
     * @param documentNo
     * @return
     */
    private Document getDocumentFromWorkItem(WorkItem workItem, String workitemId, Integer folderItemNo,
            Integer documentNo) {
        List<FolderItem> folderItems = workItem.getFolderItems();
        Document document = null;
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                if (folderItem.getWorkitemId().equals(workitemId)
                        && folderItem.getFolderItemNo().equals(folderItemNo)) {
                    List<Document> documents = folderItem.getDocuments();
                    if (documents != null) {
                        for (Document temp : documents) {
                            if (temp.getWorkitemId().equals(workitemId) && temp.getFolderItemNo().equals(folderItemNo)
                                    && temp.getDocumentNo().equals(documentNo)) {
                                document = temp;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return document;
    }

    @Override
    public byte[] getBinaryDataOfDocument(String workspaceId, Document document) throws EarthException {
        return (byte[]) this.executeTransaction(workspaceId, () -> {
            try {
                QStrDataDb qStrDataDb = QStrDataDb.newInstance();
                Map<Path<?>, Object> keyMap = new HashMap<>();
                keyMap.put(qStrDataDb.workitemId, document.getWorkitemId());
                keyMap.put(qStrDataDb.folderItemNo, document.getFolderItemNo());
                keyMap.put(qStrDataDb.documentNo, document.getDocumentNo());
                StrDataDb dataDb = dataDbDao.findOne(workspaceId, keyMap);
                if (dataDb != null) {
                    if (!EStringUtil.isEmpty(dataDb.getDocumentData())) {
                        return dataDb.getDocumentData().getBytes();
                    }
                    return null;
                }
                QStrDataFile qStrDataFile = QStrDataFile.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qStrDataFile.documentNo, document.getDocumentNo());
                condition.put(qStrDataFile.folderItemNo, document.getFolderItemNo());
                condition.put(qStrDataFile.workitemId, document.getWorkitemId());
                StrDataFile dataFile = dataFileDao.findOne(workspaceId, condition);
                if (!EStringUtil.isEmpty(dataFile.getDocumentDataPath())) {
                    File file = new File(dataFile.getDocumentDataPath());
                    if (file.exists()) {
                        return FileUtil.convertFileToBinary(file);
                    }
                }
                return null;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    @Override
    public RestResponse saveImage(HttpSession session, String workspaceId, Document document) throws EarthException {
        RestResponse respone = new RestResponse();
        // get work item data from session
        WorkItem workItem = null;
        try {
            workItem = (WorkItem) session.getAttribute("TMP" + workspaceId + "&" + document.getWorkitemId());
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "session" }));
            return respone;
        }
        // from work item data find document data and update value of this
        List<FolderItem> folderItems = workItem.getFolderItems();
        boolean isDocumentExisted = false;
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                if (folderItem.getWorkitemId().equals(document.getWorkitemId())
                        && folderItem.getFolderItemNo().equals(document.getFolderItemNo())) {
                    List<Document> documents = folderItem.getDocuments();
                    if (documents != null) {
                        for (Document temp : documents) {
                            if (temp.getWorkitemId().equals(document.getWorkitemId())
                                    && temp.getFolderItemNo().equals(document.getFolderItemNo())
                                    && temp.getDocumentNo().equals(document.getDocumentNo())) {
                                temp.setTemplateId(document.getTemplateId());
                                temp.setDocumentData(document.getDocumentData());
                                temp.setDocumentBinary(document.getDocumentBinary());
                                temp.setDocumentPath(document.getDocumentPath());
                                temp.setDocumentType(document.getDocumentType());
                                temp.setLastUpdateTime(document.getLastUpdateTime());
                                temp.setPageCount(document.getPageCount());
                                temp.setViewInformation(document.getViewInformation());
                                temp.setLayers(document.getLayers());
                                // TODO don't know what is the value of action
                                // update
                                temp.setAction(0);
                                isDocumentExisted = true;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (!isDocumentExisted) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "document" }));
            return respone;
        }
        // set workitem data after update to session
        session.setAttribute("TMP" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        return respone;
    }

    @Override
    public RestResponse closeImage(HttpSession session, String workspaceId, String workitemId) throws EarthException {
        RestResponse respone = new RestResponse();
        // remove template session
        session.removeAttribute("TMP" + workspaceId + "&" + workitemId);
        return respone;
    }

    @Override
    public RestResponse saveAndCloseImages(HttpSession session, String workspaceId, String workitemId,
            List<Document> docImages) throws EarthException {
        RestResponse respone = new RestResponse();
        WorkItem workItem = null;
        try {
            workItem = (WorkItem) session.getAttribute("TMP" + workspaceId + "&" + workitemId);
        } catch (Exception e) {
            throw new EarthException(e);
        }
        if (workItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "session" }));
            return respone;
        }
        // instance map with key is DocumentImageKey and value is Document
        Map<DocumentImageKey, Document> mapDocumentImage = new HashMap<>();
        for (Document doc : docImages) {
            mapDocumentImage.put(new DocumentImageKey(doc.getWorkitemId(), doc.getFolderItemNo(), doc.getDocumentNo()),
                    doc);
        }

        // from work item data find document data and update value of this
        List<FolderItem> folderItems = workItem.getFolderItems();
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                List<Document> documents = folderItem.getDocuments();
                if (documents != null) {
                    for (Document doc : documents) {
                        DocumentImageKey documentImageKey = new DocumentImageKey(workitemId, doc.getFolderItemNo(),
                                doc.getDocumentNo());
                        if (mapDocumentImage.containsKey(documentImageKey)) {
                            doc = mapDocumentImage.get(documentImageKey);
                        }
                    }
                }
            }
        }
        // set workitem data after update to session
        session.setAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        // remove template session
        session.removeAttribute("TMP" + workspaceId + "&" + workitemId);
        return respone;

    }

    @Override
    public RestResponse closeWithoutSavingImage(HttpSession session, String workspaceId, String workitemId)
            throws EarthException {
        return closeImage(session, workspaceId, workitemId);
    }

    @Override
    public RestResponse getThumbnail(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException {
        RestResponse respone = new RestResponse();
        Document document = new Document();
        document.setWorkitemId(workitemId);
        document.setFolderItemNo(folderItemNo);
        document.setDocumentNo(documentNo);
        // TODO because don't know how to get outputType, and width, height of
        // image
        byte[] bytes = getBinaryDataOfDocument(workspaceId, document);
        if (bytes != null && bytes.length > 0) {
            respone.setData(ImageUtil.getThumbnail(bytes, WIDTH, HEIGHT, ImageUtil.PNG_TYPE));
        }
        return respone;
    }

}
