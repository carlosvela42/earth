package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentSavingInfo;

public interface DocumentService {
    /**
     * save document
     *
     * @param document
     * @param documentSavingInfo
     * @return
     * @throws EarthException
     */
    boolean saveDocument(Document document, DocumentSavingInfo documentSavingInfo) throws EarthException;

    /**
     * get document
     *
     * @param document
     * @param documentSavingInfo
     * @return Binary
     * @throws EarthException
     */
    byte[] getDocument(Document document, DocumentSavingInfo documentSavingInfo) throws EarthException;

    DocumentSavingInfo getDocumentSavingInfo(long processId, long siteId) throws EarthException;

    boolean getDocumentListInfo(String workspaceId, String workitemId, String folderItemNo, String documentNo)
            throws EarthException;

}