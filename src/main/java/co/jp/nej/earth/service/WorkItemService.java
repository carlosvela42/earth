package co.jp.nej.earth.service;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.ws.RestResponse;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface WorkItemService {
    /**
     * get work item data structure
     *
     * @param workItemId
     * @return
     */
    WorkItem getWorkItemDataStructure(String workItemId, String workspaceId) throws EarthException;

    /**
     * insert or update work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     */
    boolean insertOrUpdateWorkItemToDb(WorkItem workItem) throws EarthException;

    /**
     * get workitem data from session
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @return
     * @throws EarthException
     */
    RestResponse getWorkItem(HttpSession session, String workspaceId, String workitemId) throws EarthException;

    /**
     * update workitem data to session
     *
     * @param session
     * @param workspaceId
     * @param workItem
     * @return
     * @throws EarthException
     */
    RestResponse updateWorkItem(HttpSession session, String workspaceId, WorkItem workItem) throws EarthException;

    /**
     * get workitem structure from session (remove template data from workitem)
     *
     * @param session
     * @param workspaceId
     * @param workitemId
     * @return
     * @throws EarthException
     */
    RestResponse getWorkItemStructure(HttpSession session, String workspaceId, String workitemId) throws EarthException;

    /**
     * search workitems data by search condition
     *
     * @param session
     * @param workspaceId
     * @param searchCondition
     * @return
     * @throws EarthException
     */
    RestResponse searchWorkItems(HttpSession session, String workspaceId, WorkItem searchCondition)
            throws EarthException;

    /**
     * receiveNextTask
     *
     * @param taskId
     * @return
     */
    String receiveNextTask(String taskId);

}
