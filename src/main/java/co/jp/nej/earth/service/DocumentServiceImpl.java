package co.jp.nej.earth.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.DataDbDao;
import co.jp.nej.earth.dao.DataFileDao;
import co.jp.nej.earth.dao.DirectoryDao;
import co.jp.nej.earth.dao.StrageFileDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentSavingInfo;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.StrageFile;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.StrDataDb;
import co.jp.nej.earth.model.entity.StrDataFile;
import co.jp.nej.earth.model.enums.DocumentSavingType;
import co.jp.nej.earth.model.sql.QStrDataDb;
import co.jp.nej.earth.model.sql.QStrDataFile;
import co.jp.nej.earth.model.sql.QStrageFile;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.FileUtil;

@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
@Service
public class DocumentServiceImpl implements DocumentService {

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
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventControlServiceImpl.class);
    // TODO
    private static final String FILE_UNTIL_FULL = "fileUntilFul";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveDocument(Document document, DocumentSavingInfo documentSavingInfo) throws EarthException {
        boolean result = false;
        List<Directory> directories = documentSavingInfo.getDataDef();
        File file = new File(document.getDocumentPath());
        if (DocumentSavingType.FILE_UNTIL_FULL.equals(documentSavingInfo.getSavingType())) {
            // loop directories
            for (Directory directory : directories) {
                File directoryFile = new File(directory.getFolderPath());
                // check size of file input and all file in directory are small
                // than reserved disk volume size or not
                if (Long.parseLong(directory.getReservedDiskVolSize()) > (FileUtil.getFileSize(file)
                        + FileUtil.getDirectorySize(directoryFile))) {
                    if (!result) {
                        result = true;
                    }
                    File fileOutPut = new File(directoryFile.getAbsolutePath() + File.separator + file.getName());
                    copyFileToDirectory(document, file, fileOutPut);
                    saveDocument2DataFile(document, fileOutPut);
                    deleteFile(file);
                    break;
                }
            }
        } else if (DocumentSavingType.FILE_ROUND_ROBIN.equals(documentSavingInfo.getSavingType())) {
            result = true;
            Directory directory = FileUtil.getDirectoryUsedStorageMin(directories);
            File fileOutPut = new File(directory.getFolderPath() + File.separator + file.getName());
            copyFileToDirectory(document, file, fileOutPut);
            saveDocument2DataFile(document, fileOutPut);
            deleteFile(file);
        } else if (DocumentSavingType.DATABASE.equals(documentSavingInfo.getSavingType())) {
            try {
                result = true;
                saveDocumentToDB(file, document);
                deleteFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new EarthException(e.getMessage());

            }
        }
        if (!result) {
            LOG.error(new Message("", messageSource.getMessage(ErrorCode.E1012, new String[] { "" }, Locale.ENGLISH))
                    .getContent());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getDocument(Document document, DocumentSavingInfo documentSavingInfo) throws EarthException {
        if (DocumentSavingType.DATABASE.equals(documentSavingInfo.getSavingType())) {
            QStrDataDb qStrDataDb = QStrDataDb.newInstance();
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qStrDataDb.documentNo, document.getDocumentNo());
            condition.put(qStrDataDb.folderItemNo, document.getFolderItemNo());
            condition.put(qStrDataDb.workitemId, document.getWorkitemId());
            StrDataDb dataDb = dataDbDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
            return dataDb.getDocumentData().getBytes();
        } else {
            QStrDataFile qStrDataFile = QStrDataFile.newInstance();
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qStrDataFile.documentNo, document.getDocumentNo());
            condition.put(qStrDataFile.folderItemNo, document.getFolderItemNo());
            condition.put(qStrDataFile.workitemId, document.getWorkitemId());
            StrDataFile dataFile = dataFileDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
            try {
                return FileUtil.convertFileToBinary(new File(dataFile.getDocumentDataPath()));
            } catch (IOException e) {
                throw new EarthException(e.getMessage());
            }
        }
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
            throw new EarthException(e.getMessage());
        }

    }

    /**
     * save Document to table DataFile in database
     *
     * @param document
     * @param fileOutPut
     * @throws EarthException
     */
    private void saveDocument2DataFile(Document document, File fileOutPut) throws EarthException {
        QStrDataFile qStrDataFile = QStrDataFile.newInstance();
        if (document.getDocumentNo() == -1) {
            // instance object
            StrDataFile dataFile = new StrDataFile();
            dataFile.setWorkitemId(document.getWorkitemId());
            dataFile.setDocumentNo(document.getDocumentNo());
            dataFile.setFolderItemNo(document.getFolderItemNo());
            dataFile.setDocumentDataPath(fileOutPut.getAbsolutePath());
            // insert DataFile
            dataFileDao.add(Constant.EARTH_WORKSPACE_ID, dataFile);
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
            dataFileDao.update(Constant.EARTH_WORKSPACE_ID, condition, updateMap);
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
    private void saveDocumentToDB(File file, Document document) throws EarthException, IOException {
        byte[] dataFile = FileUtil.convertFileToBinary(file);
        if (document.getDocumentNo() == -1) {
            StrDataDb dataDb = new StrDataDb();
            dataDb.setWorkitemId(document.getWorkitemId());
            dataDb.setDocumentNo(document.getDocumentNo());
            dataDb.setFolderItemNo(document.getFolderItemNo());
            dataDb.setDocumentData(new String(dataFile));
            // insert dataDb
            dataDbDao.add(Constant.EARTH_WORKSPACE_ID, dataDb);
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
            dataDbDao.update(Constant.EARTH_WORKSPACE_ID, condition, updateMap);
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
    public DocumentSavingInfo getDocumentSavingInfo(long processId, long siteId) throws EarthException {
        DocumentSavingInfo documentSavingInfo = new DocumentSavingInfo();
        QStrageFile qStrageFile = QStrageFile.newInstance();
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qStrageFile.siteId, siteId);
        condition.put(qStrageFile.processId, processId);
        StrageFile strageFile = strageFileDao.findOne(Constant.EARTH_WORKSPACE_ID, condition);
        // TODO
        if (strageFile != null) {
            if (FILE_UNTIL_FULL.equalsIgnoreCase(strageFile.getSiteManagementType())) {
                documentSavingInfo.setSavingType(DocumentSavingType.FILE_UNTIL_FULL);
            } else {
                documentSavingInfo.setSavingType(DocumentSavingType.FILE_ROUND_ROBIN);
            }
        } else {
            documentSavingInfo.setSavingType(DocumentSavingType.DATABASE);
        }
        documentSavingInfo.setDataDef(directoryDao.getDirectoriesBySite((int)siteId));
        return documentSavingInfo;
    }

}
