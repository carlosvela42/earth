package co.jp.nej.earth.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.util.EMessageResource;

@Service
public class FolderItemServiceImpl implements FolderItemService {

    @Autowired
    private EMessageResource eMessageResource;

    /**
     * {@inheritDoc}
     */
    @Override
    public RestResponse getFolderItem(HttpSession session, String workspaceId, String workitemId, Integer folderItemNo)
            throws EarthException {
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
        // from work item data find folder item data
        List<FolderItem> folderItems = workItem.getFolderItems();
        FolderItem folderItem = null;
        if (folderItems != null) {
            for (FolderItem temp : folderItems) {
                if (temp.getWorkitemId().equals(workitemId) && temp.getFolderItemNo().equals(folderItemNo)) {
                    folderItem = temp;
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (folderItem == null) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "folderItem" }));
            return respone;
        }
        // remove information of document
        folderItem.setDocuments(null);
        respone.setData(folderItem);
        return respone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RestResponse updateFolderItem(HttpSession session, String workspaceId, String workitemId,
            FolderItem folderItem) throws EarthException {
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
        // from work item data find folder item data and update value of this
        List<FolderItem> folderItems = workItem.getFolderItems();
        boolean isFolderItemExisted = false;
        if (folderItems != null) {
            for (FolderItem temp : folderItems) {
                if (temp.getWorkitemId().equals(workitemId)
                        && temp.getFolderItemNo().equals(folderItem.getFolderItemNo())) {
                    temp.setFolderItemData(folderItem.getFolderItemData());
                    temp.setLastUpdateTime(folderItem.getLastUpdateTime());
                    temp.setTemplateId(folderItem.getTemplateId());
                    // TODO don't know what is the value of action update
                    temp.setAction(0);
                    isFolderItemExisted = true;
                    break;
                }
            }
        }
        // if don't find any folder item return message
        if (!isFolderItemExisted) {
            respone.setResult(false);
            respone.setData(eMessageResource.get("E0002", new String[] { "folderItem" }));
        }
        // set work item after update to session
        session.setAttribute("ORIGIN" + workspaceId + "&" + workItem.getWorkitemId(), workItem);
        return respone;
    }

}
