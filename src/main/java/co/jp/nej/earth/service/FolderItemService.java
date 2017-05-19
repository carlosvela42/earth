package co.jp.nej.earth.service;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.ws.RestResponse;

public interface FolderItemService {
    /**
     * get folderitem from session
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItemNo
     * @return
     * @throws EarthException
     */
    RestResponse getFolderItem(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo)
            throws EarthException;

    /**
     * update folder item data in session
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @param folderItem
     * @return
     * @throws EarthException
     */
    RestResponse updateFolderItem(HttpSession session, String workspaceId, String workitemId, FolderItem folderItem)
            throws EarthException;
}
