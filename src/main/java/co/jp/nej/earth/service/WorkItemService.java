package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;

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
    WorkItem getWorkItemDataStructure(long workItemId);

    /**
     * insert or update work item
     *
     * @param workItem
     * @return
     * @throws EarthException
     */
    boolean insertOrUpdateWorkItemToDb(WorkItem workItem) throws EarthException;

}
