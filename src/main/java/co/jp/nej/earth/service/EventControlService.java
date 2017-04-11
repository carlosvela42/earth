package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.xml.WorkItem;

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
    public long insertEvent(WorkItem workItem) throws EarthException;
}
