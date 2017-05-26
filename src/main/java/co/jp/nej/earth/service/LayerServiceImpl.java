package co.jp.nej.earth.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.util.EMessageResource;

@Service
public class LayerServiceImpl implements LayerService {

    @Autowired
    private EMessageResource eMessageResource;

    @Override
    public RestResponse getLayer(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo, Integer layerNo) throws EarthException {
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
        // from work item data find layer data
        List<FolderItem> folderItems = workItem.getFolderItems();
        Layer result = null;
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                if (folderItem.getWorkitemId().equals(workitemId)
                        && folderItem.getFolderItemNo().equals(folderItemNo)) {
                    List<Document> documents = folderItem.getDocuments();
                    if (documents != null) {
                        for (Document document : documents) {
                            if (document.getWorkitemId().equals(workitemId)
                                    && document.getFolderItemNo().equals(folderItemNo)
                                    && document.getDocumentNo().equals(documentNo)) {
                                List<Layer> layers = document.getLayers();
                                if (layers != null) {
                                    for (Layer layer : layers) {
                                        if (layer.getWorkitemId().equals(workitemId)
                                                && layer.getFolderItemNo().equals(folderItemNo)
                                                && layer.getDocumentNo().equals(documentNo)
                                                && layer.getLayerNo().equals(layerNo)) {
                                            result = layer;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (result == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "result" }));
        }
        // remove information of document
        respone.setData(result);
        return respone;
    }

    @Override
    public RestResponse updateLayer(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo,
            Integer documentNo, Layer layer) throws EarthException {
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
        // from work item data find layer data and update it
        List<FolderItem> folderItems = workItem.getFolderItems();
        boolean islayerExisted = false;
        if (folderItems != null) {
            for (FolderItem folderItem : folderItems) {
                if (folderItem.getWorkitemId().equals(workitemId)
                        && folderItem.getFolderItemNo().equals(folderItemNo)) {
                    List<Document> documents = folderItem.getDocuments();
                    if (documents != null) {
                        for (Document document : documents) {
                            if (document.getWorkitemId().equals(workitemId)
                                    && document.getFolderItemNo().equals(folderItemNo)
                                    && document.getDocumentNo().equals(documentNo)) {
                                List<Layer> layers = document.getLayers();
                                if (layers != null) {
                                    for (Layer temp : layers) {
                                        if (temp.getWorkitemId().equals(workitemId)
                                                && temp.getFolderItemNo().equals(folderItemNo)
                                                && temp.getDocumentNo().equals(documentNo)
                                                && temp.getLayerNo().equals(layer.getLayerNo())) {
                                            temp.setAnnotations(layer.getAnnotations());
                                            temp.setLastUpdateTime(layer.getLastUpdateTime());
                                            temp.setLayerData(layer.getLayerData());
                                            temp.setOwnerId(layer.getOwnerId());
                                            temp.setTemplateId(layer.getTemplateId());
                                            // TODO don't know what is the value of action update
                                            temp.setAction(0);
                                            islayerExisted = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (!islayerExisted) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "layer" }));
            return respone;
        }
        // set work item after update to session
        session.setAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        return respone;
    }

}
