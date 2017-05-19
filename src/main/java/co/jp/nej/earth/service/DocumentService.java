package co.jp.nej.earth.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentSavingInfo;
import co.jp.nej.earth.model.ws.RestResponse;

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

    List<Document> getDocumentListInfo(String workspaceId, String workitemId, int folderItemNo, String documentNo)
            throws EarthException;

    /**
     * get document from session
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @param documentNo
     * @return
     * @throws EarthException
     */
    RestResponse getDocument(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException;

    /**
     * update document in session
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @param document
     * @return
     * @throws EarthException
     */
    RestResponse updateDocument(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Document document) throws EarthException;
}