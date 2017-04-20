package co.jp.nej.earth.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.DataDbDao;
import co.jp.nej.earth.dao.DataFileDao;
import co.jp.nej.earth.dao.DocumentDao;
import co.jp.nej.earth.dao.FolderItemDao;
import co.jp.nej.earth.dao.LayerDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.dao.WorkItemDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.StrDataDb;
import co.jp.nej.earth.model.entity.StrDataFile;
import co.jp.nej.earth.model.sql.QDocument;
import co.jp.nej.earth.model.sql.QFolderItem;
import co.jp.nej.earth.model.sql.QLayer;
import co.jp.nej.earth.model.sql.QStrDataDb;
import co.jp.nej.earth.model.sql.QStrDataFile;
import co.jp.nej.earth.model.sql.QWorkItem;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.FileUtil;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class WorkItemServiceImpl implements WorkItemService {

    @Autowired
    private WorkItemDao workItemDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private FolderItemDao folderItemDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private LayerDao layerDao;
    @Autowired
    private DataFileDao dataFileDao;
    @Autowired
    private DataDbDao dataDbDao;

    private static QFolderItem qFolderItem = QFolderItem.newInstance();

    private static QWorkItem qWorkItem = QWorkItem.newInstance();

    private static QDocument qDocument = QDocument.newInstance();

    private static QStrDataFile qStrDataFile = QStrDataFile.newInstance();

    private static QStrDataDb qStrDataDb = QStrDataDb.newInstance();

    private static QLayer qLayer = QLayer.newInstance();

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkItem getWorkItemDataStructure(long workitemId) {
        return workItemDao.getWorkItemDataStructure(workitemId);
    }

    /**
     * {@inheritDoc}}
     *
     * @throws EarthException
     */
    @Override
    public boolean insertOrUpdateWorkItemToDb(WorkItem workItem) throws EarthException {
        Long workitemId = Long.parseLong(workItem.getWorkitemId());
        try {
            if (workitemId < 0) {
                return insertWorkItemToDb(workItem);
            }
            return updateWorkItemToDb(workItem);
        } catch (IOException e) {
            throw new EarthException(e.getMessage());
        }
    }

    /**
     * insert work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     * @throws IOException
     */
    private boolean insertWorkItemToDb(WorkItem workItem) throws EarthException, IOException {
        // TODO
        workItem.setWorkitemId(
                Integer.toString(workItemDao.findAll(workItem.getWorkspaceId(), null, null, null).size() + 1));
        workItem.setLastHistoryNo(-1);
        workItemDao.add(workItem.getWorkspaceId(), workItem);
        // get template data
        workItem.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), workItem.getTemplateId()));
        templateDao.insertWorkItemTemplateData(workItem.getWorkspaceId(), workItem, workItem.getLastHistoryNo());
        // list folder in work item
        for (FolderItem folder : workItem.getFolderItems()) {
            // insert folder item
            insertFolder2Db(folder, workItem);
        }
        return true;
    }

    /**
     * update work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     * @throws IOException
     */
    private boolean updateWorkItemToDb(WorkItem workItem) throws EarthException, IOException {
        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qWorkItem.workitemId, workItem.getWorkitemId());
        // instance update map
        Map<Path<?>, Object> updateMap = new HashMap<>();
        updateMap.put(qWorkItem.lastHistoryNo, workItem.getLastHistoryNo());
        updateMap.put(qWorkItem.taskId, workItem.getTaskId());
        updateMap.put(qWorkItem.templateId, workItem.getTemplateId());
        updateMap.put(qWorkItem.lastUpdateTime, DateUtil.getCurrentDateString());
        // update workItem
        workItemDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // insert workItemTemplate
        workItem.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), workItem.getTemplateId()));
        templateDao.insertWorkItemTemplateData(workItem.getWorkspaceId(), workItem, workItem.getLastHistoryNo());
        // list folder in work item
        for (FolderItem folder : workItem.getFolderItems()) {
            // check action is insert, update or delete folder
            if (folder.getFolderItemNo() < 0) {
                insertFolder2Db(folder, workItem);
            } else if (folder.getAction() == null || folder.getAction() == 0) {
                updateFolder2Db(folder, workItem);
            } else {
                deleteFolder2Db(folder, workItem);
            }
        }
        return true;
    }

    /**
     * insert folder
     *
     * @param folder
     * @param workItem
     * @throws EarthException
     * @throws IOException
     */
    private void insertFolder2Db(FolderItem folder, WorkItem workItem) throws EarthException, IOException {
        // TODO
        folder.setFolderItemNo(folderItemDao.findAll(workItem.getWorkspaceId(), null, null, null).size() + 1);
        folder.setWorkitemId(workItem.getWorkitemId());
        // insert folder
        folderItemDao.add(workItem.getWorkspaceId(), folder);
        // insert folder template
        folder.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), folder.getTemplateId()));
        templateDao.insertFolderItemTemplateData(workItem.getWorkspaceId(), folder, workItem.getLastHistoryNo());
        // list document in folder
        for (Document document : folder.getDocuments()) {
            insertDocument2Db(document, folder, workItem);
        }
    }

    /**
     * update folder
     *
     * @param folder
     * @param workItem
     * @throws EarthException
     * @throws IOException
     */
    private void updateFolder2Db(FolderItem folder, WorkItem workItem) throws EarthException, IOException {
        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qFolderItem.folderItemNo, folder.getFolderItemNo());
        condition.put(qFolderItem.workitemId, workItem.getWorkitemId());
        // instance update map
        Map<Path<?>, Object> updateMap = new HashMap<>();
        updateMap.put(qFolderItem.templateId, folder.getTemplateId());
        updateMap.put(qFolderItem.lastUpdateTime, DateUtil.getCurrentDateString());
        // update folder
        folderItemDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // insert folder template
        folder.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), folder.getTemplateId()));
        templateDao.insertFolderItemTemplateData(workItem.getWorkspaceId(), folder, workItem.getLastHistoryNo());
        // list document in folder
        for (Document document : folder.getDocuments()) {
            // check action is insert, update or delete
            if (document.getDocumentNo() < 0) {
                insertDocument2Db(document, folder, workItem);
            } else if (document.getAction() == null || document.getAction() == 0) {
                updateDocument2Db(document, folder, workItem);
            } else {
                deleteDocument2Db(document, folder, workItem);
            }
        }
    }

    /**
     * delete folder
     *
     * @param folder
     * @param workItem
     * @throws EarthException
     */
    private void deleteFolder2Db(FolderItem folder, WorkItem workItem) throws EarthException {

        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qFolderItem.folderItemNo, folder.getFolderItemNo());
        condition.put(qFolderItem.workitemId, workItem.getWorkitemId());
        // delete folder
        folderItemDao.delete(workItem.getWorkspaceId(), condition);
    }

    /**
     * insert document
     *
     * @param document
     * @param folder
     * @param workItem
     * @throws EarthException
     * @throws IOException
     */
    private void insertDocument2Db(Document document, FolderItem folder, WorkItem workItem)
            throws EarthException, IOException {
        // TODO
        document.setDocumentNo(documentDao.findAll(workItem.getWorkspaceId(), null, null, null).size() + 1);
        document.setWorkitemId(workItem.getWorkitemId());
        document.setFolderItemNo(folder.getFolderItemNo());
        // insert document
        documentDao.add(workItem.getWorkspaceId(), document);
        // insert document template data
        document.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), document.getTemplateId()));
        templateDao.insertDocumentTemplateData(workItem.getWorkspaceId(), document, workItem.getLastHistoryNo());
        StrDataFile dataFile = new StrDataFile();
        dataFile.setWorkitemId(workItem.getWorkitemId());
        dataFile.setDocumentNo(document.getDocumentNo());
        dataFile.setFolderItemNo(folder.getFolderItemNo());
        dataFile.setDocumentDataPath(document.getDocumentPath());
        // insert DataFile
        dataFileDao.add(workItem.getWorkspaceId(), dataFile);
        // TODO
        // FileManagement.writeFile
        StrDataDb dataDb = new StrDataDb();
        dataDb.setWorkitemId(workItem.getWorkitemId());
        dataDb.setDocumentNo(document.getDocumentNo());
        dataDb.setFolderItemNo(folder.getFolderItemNo());
        dataDb.setDocumentData(new String(FileUtil.convertFileToBinary(new File(document.getDocumentPath()))));
        // insert dataDb
        dataDbDao.add(workItem.getWorkspaceId(), dataDb);
        // list layer in document
        for (Layer layer : document.getLayers()) {
            // insert layer
            insertLayer2Db(layer, document, folder, workItem);
        }
    }

    /**
     * update document
     *
     * @param document
     * @param folder
     * @param workItem
     * @throws EarthException
     * @throws IOException
     */
    private void updateDocument2Db(Document document, FolderItem folder, WorkItem workItem)
            throws EarthException, IOException {

        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qDocument.documentNo, document.getDocumentNo());
        condition.put(qDocument.folderItemNo, folder.getFolderItemNo());
        condition.put(qDocument.workitemId, workItem.getWorkitemId());
        // instance update map
        Map<Path<?>, Object> updateMap = new HashMap<>();
        updateMap.put(qDocument.templateId, document.getTemplateId());
        updateMap.put(qDocument.pageCount, document.getPageCount());
        updateMap.put(qDocument.viewInformation, document.getViewInformation());
        updateMap.put(qDocument.documentType, document.getDocumentType());
        updateMap.put(qDocument.lastUpdateTime, DateUtil.getCurrentDateString());
        // update document
        documentDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // insert document template
        document.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), document.getTemplateId()));
        templateDao.insertDocumentTemplateData(workItem.getWorkspaceId(), document, workItem.getLastHistoryNo());

        // instance condition map
        condition = new HashMap<>();
        condition.put(qStrDataFile.documentNo, document.getDocumentNo());
        condition.put(qStrDataFile.folderItemNo, folder.getFolderItemNo());
        condition.put(qStrDataFile.workitemId, workItem.getWorkitemId());
        // instance update map
        updateMap = new HashMap<>();
        updateMap.put(qStrDataFile.documentDataPath, document.getDocumentPath());
        updateMap.put(qStrDataFile.lastUpdateTime, DateUtil.getCurrentDateString());
        // update DataFile
        dataFileDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // TODO
        // FileManagement.deleteFile
        // FileManagement.writeFile

        // instance condition map
        condition = new HashMap<>();
        condition.put(qStrDataDb.documentNo, document.getDocumentNo());
        condition.put(qStrDataDb.folderItemNo, folder.getFolderItemNo());
        condition.put(qStrDataDb.workitemId, workItem.getWorkitemId());
        // instance update map
        updateMap = new HashMap<>();
        updateMap.put(qStrDataDb.documentData,
                new String(FileUtil.convertFileToBinary(new File(document.getDocumentPath()))));
        updateMap.put(qStrDataDb.lastUpdateTime, DateUtil.getCurrentDateString());
        // update DataDb
        dataDbDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // list layer in document
        for (Layer layer : document.getLayers()) {
            // check action is insert, update or delete layer
            if (layer.getLayerNo() < 0) {
                insertLayer2Db(layer, document, folder, workItem);
            } else if (folder.getAction() == null || layer.getAction() == 0) {
                updateLayer2Db(layer, document, folder, workItem);
            } else {
                deleteLayer2Db(layer, document, folder, workItem);
            }
        }
    }

    private void deleteDocument2Db(Document document, FolderItem folder, WorkItem workItem) throws EarthException {
        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qDocument.documentNo, document.getDocumentNo());
        condition.put(qDocument.folderItemNo, folder.getFolderItemNo());
        condition.put(qDocument.workitemId, workItem.getWorkitemId());
        // delete document
        documentDao.delete(workItem.getWorkspaceId(), condition);
    }

    private void insertLayer2Db(Layer layer, Document document, FolderItem folder, WorkItem workItem)
            throws EarthException {
        // TODO
        layer.setLayerNo(layerDao.findAll(workItem.getWorkspaceId(), null, null, null).size() + 1);
        layer.setDocumentNo(document.getDocumentNo());
        layer.setWorkitemId(workItem.getWorkitemId());
        layer.setFolderItemNo(folder.getFolderItemNo());
        // insert layer
        layerDao.add(workItem.getWorkspaceId(), layer);
        // insert layer template
        layer.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), layer.getTemplateId()));
        templateDao.insertLayerTemplateData(workItem.getWorkspaceId(), layer, workItem.getLastHistoryNo());
    }

    private void updateLayer2Db(Layer layer, Document document, FolderItem folder, WorkItem workItem)
            throws EarthException, JsonProcessingException {
        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qLayer.layerNo, layer.getLayerNo());
        condition.put(qLayer.documentNo, document.getDocumentNo());
        condition.put(qLayer.folderItemNo, folder.getFolderItemNo());
        condition.put(qLayer.workitemId, workItem.getWorkitemId());
        // instance update map
        Map<Path<?>, Object> updateMap = new HashMap<>();
        updateMap.put(qLayer.templateId, layer.getTemplateId());
        updateMap.put(qLayer.ownerId, layer.getOwnerId());
        updateMap.put(qLayer.annotations, new ObjectMapper().writeValueAsString(layer.getAnnotations()));
        updateMap.put(qLayer.lastUpdateTime, DateUtil.getCurrentDateString());
        // update layer
        documentDao.update(workItem.getWorkspaceId(), condition, updateMap);
        // insert layer template
        layer.setMgrTemplate(templateDao.getTemplate(workItem.getWorkspaceId(), layer.getTemplateId()));
        templateDao.insertLayerTemplateData(workItem.getWorkspaceId(), layer, workItem.getLastHistoryNo());
    }

    private void deleteLayer2Db(Layer layer, Document document, FolderItem folder, WorkItem workItem)
            throws EarthException {
        // instance condition map
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qLayer.layerNo, layer.getLayerNo());
        condition.put(qLayer.documentNo, document.getDocumentNo());
        condition.put(qLayer.folderItemNo, folder.getFolderItemNo());
        condition.put(qLayer.workitemId, workItem.getWorkitemId());
        // delete layer
        layerDao.delete(workItem.getWorkspaceId(), condition);
    }

}
