package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.DocumentSavingInfo;
import co.jp.nej.earth.model.ws.DisplayImageResponse;

import javax.servlet.http.HttpSession;

public interface DocumentService {
    /**
     * save document
     *
     * @param document
     * @param documentSavingInfo
     * @return
     * @throws EarthException
     */
    boolean saveDocument(String workspaceId, Document document, DocumentSavingInfo documentSavingInfo)
            throws EarthException;

    /**
     * get document
     *
     * @param document
     * @param documentSavingInfo
     * @return Binary
     * @throws EarthException
     */
    byte[] getDocument(String workspaceId, Document document, DocumentSavingInfo documentSavingInfo)
            throws EarthException;

    DocumentSavingInfo getDocumentSavingInfo(String workspaceId, Integer processId) throws EarthException;

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
    Document getDocumentSession(HttpSession session, String workspaceId, String workitemId, String folderItemNo,
                                String documentNo) throws EarthException;

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
    boolean updateDocumentSession(HttpSession session, String workspaceId, String workitemId, String folderItemNo,
                                Document document) throws EarthException;

    /**
     * display image (return the byte array)
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @return
     * @throws EarthException
     */
    DisplayImageResponse displayImage(HttpSession session, String workspaceId, String workitemId,
                                      String folderItemNo, String currentDocumentNo) throws EarthException;

    /**
     * get binary data of document
     *
     * @param document
     * @return
     */
    byte[] getBinaryDataOfDocument(String workspaceId, Document document) throws EarthException;

    /**
     * save image
     *
     * @param session
     * @param workspaceId
     * @param document
     * @return
     */
    boolean saveImageSession(HttpSession session, String workspaceId, Document document) throws EarthException;

    /**
     * close Image
     *
     * @param session
     * @param workspaceId
     * @param workItemId
     * @return
     * @throws EarthException
     */
    boolean closeImage(HttpSession session, String workspaceId, String workItemId, String folderItemNo,
                       String documentNo) throws EarthException;

    /**
     * save and close images
     *
     * @param session
     * @param workspaceId
     * @return
     * @throws EarthException
     */
    boolean saveAndCloseImages(HttpSession session, String workspaceId, String workitemId,
                               String folderItemNo) throws EarthException;

    /**
     * close without saving image
     *
     * @param session
     * @param workspaceId
     * @return
     * @throws EarthException
     */
    boolean closeWithoutSavingImage(HttpSession session, String workspaceId, String workitemId)
            throws EarthException;

    /**
     * get thumbnail
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @param documentNo
     * @return
     * @throws EarthException
     */
    String getThumbnail(HttpSession session, String workspaceId, String workitemId, String folderItemNo,
                        String documentNo) throws EarthException;
}