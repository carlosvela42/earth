package co.jp.nej.earth.service;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.ws.RestResponse;

public interface LayerService {
    /**
     * get layer from session
     *
     * @param session
     * @param workspaceId
     * @param workItemId
     * @param folderItemNo
     * @param documentNo
     * @param layerNo
     * @return
     * @throws EarthException
     */
    RestResponse getLayer(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo, Integer layerNo) throws EarthException;

    /**
     * update layer data in session
     *
     * @param session
     * @param workspaceId
     * @param workItemId
     * @param folderItemNo
     * @param documentNo
     * @param layerNo
     * @return
     * @throws EarthException
     */
    RestResponse updateLayer(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo, Layer layer) throws EarthException;
}
