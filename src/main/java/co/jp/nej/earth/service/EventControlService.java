package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.CtlEvent;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface EventControlService {

    /**
     * insert event
     *
     * @param workItem
     * @return
     * @throws EarthException
     */
    boolean insertEvent(WorkItem workItem) throws EarthException;

    /**
     * get list event by status
     *
     * @param status
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    List<String> getListEventIdByStatus(String status, String workSpaceId) throws EarthException;

    /**
     * update bulk event status
     *
     * @param eventIds
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException;

    /**
     * get event by eventId
     *
     * @param eventId
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    CtlEvent getEventByEventId(String eventId, String workSpaceId) throws EarthException;

    /**
     * delete event
     * @param event
     * @param workSpaceId
     * @return
     */
    boolean deleteEvent(CtlEvent event, String workSpaceId) throws EarthException;
}
