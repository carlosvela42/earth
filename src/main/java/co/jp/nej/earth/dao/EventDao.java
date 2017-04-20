package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlEvent;

/**
 * Created by minhtv on 3/29/2017.
 */
public interface EventDao extends BaseDao<CtlEvent> {
    boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    /**
     * get list ctlEvent by status
     *
     * @param status
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    List<String> getListCtlEventIdByStatus(String status, String workSpaceId) throws EarthException;

    /**
     * update bulk event status
     *
     * @param eventIds
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException;
}
