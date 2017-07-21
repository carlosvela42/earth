package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.FolderItemDao;
import co.jp.nej.earth.dao.TemplateDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.enums.Action;
import co.jp.nej.earth.model.enums.Operation;
import co.jp.nej.earth.util.EModelUtil;
import co.jp.nej.earth.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
public class FolderItemServiceImpl extends BaseService implements FolderItemService {

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private TemplateDao templateDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public FolderItem getFolderItemSession(HttpSession session, String workspaceId, String workItemId,
                                           String folderItemNo) throws EarthException {
        // Get FolderItem data from session
        FolderItem folderItemSession = (FolderItem) getDataItemFromSession(session,
            SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
            EModelUtil.getFolderItemIndex(workItemId, folderItemNo));
        checkPermission(folderItemSession, Operation.GET_DATA);

        FolderItem folderItem = EModelUtil.clone(folderItemSession, FolderItem.class);
        folderItem.setDocuments(new ArrayList<Document>());
        return folderItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateFolderItemSession(HttpSession session, String workspaceId, String workItemId,
                                           FolderItem folderItem) throws EarthException {
        // Get FolderItem data from session
        FolderItem folderItemSession = (FolderItem) getDataItemFromSession(session,
            SessionUtil.getOriginWorkItemDictionaryKey(workspaceId, workItemId),
            EModelUtil.getFolderItemIndex(workItemId, String.valueOf(folderItem.getFolderItemNo())));
        checkPermission(folderItemSession, Operation.UPDATE_DATA);

        folderItemSession.setFolderItemData(folderItem.getFolderItemData());
        folderItemSession.setMgrTemplate(folderItem.getMgrTemplate());
        folderItemSession.setLastUpdateTime(folderItem.getLastUpdateTime());
        folderItemSession.setTemplateId(folderItem.getTemplateId());
        folderItemSession.setAction(Action.UPDATE.getAction());
        return true;
    }
}
