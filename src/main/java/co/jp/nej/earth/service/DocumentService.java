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

    /**
     * display image (return the byte array)
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @param documentNo
     * @return
     * @throws EarthException
     */
    RestResponse displayImage(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException;

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
    RestResponse saveImage(HttpSession session, String workspaceId, Document document) throws EarthException;

    /**
     * close Image
     *
     * @param session
     * @param workspaceId
     * @param workItemId
     * @return
     * @throws EarthException
     */
    RestResponse closeImage(HttpSession session, String workspaceId, String workitemId) throws EarthException;

    /**
     * save and close images
     *
     * @param session
     * @param workspaceId
     * @param docImages
     * @return
     * @throws EarthException
     */
    RestResponse saveAndCloseImages(HttpSession session, String workspaceId, String workitemId,
            List<Document> docImages) throws EarthException;

    /**
     * close without saving image
     *
     * @param session
     * @param workspaceId
     * @return
     * @throws EarthException
     */
    RestResponse closeWithoutSavingImage(HttpSession session, String workspaceId, String workitemId)
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
    RestResponse getThumbnail(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo) throws EarthException;
}