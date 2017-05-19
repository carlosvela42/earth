package co.jp.nej.earth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.sql.QCtlEvent;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class EventControlServiceImpl implements EventControlService {

    @Autowired
    private EventDao eventDao;
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventControlServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insertEvent(String workspaceId,WorkItem workItem) throws EarthException {
        // parse object to json
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(workItem);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        CtlEvent event = new CtlEvent();
        // TODO
        // user function increase event id
        // now we user trigger to do that thing
        // event.setEventId("1");
        event.setStatus(AgentBatch.STATUS_EDIT);
        event.setTaskId("1");
        event.setWorkitemData(json);
        event.setWorkitemId(" -1");
        event.setUserId("1");
        return eventDao.add(workspaceId, event) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListEventIdByStatus(String status, String workSpaceId) throws EarthException {
        return eventDao.getListCtlEventIdByStatus(status, workSpaceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException {
        return eventDao.updateBulkEventStatus(eventIds, workSpaceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CtlEvent getEventByEventId(String eventId, String workSpaceId) throws EarthException {
        Map<Path<?>, Object> mapCondition = new HashMap<>();
        mapCondition.put(QCtlEvent.newInstance().eventId, eventId);
        return eventDao.findOne(workSpaceId, mapCondition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEvent(CtlEvent event, String workSpaceId) throws EarthException {
        QCtlEvent qCtlEvent = QCtlEvent.newInstance();
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qCtlEvent.eventId, event.getEventId());
        condition.put(qCtlEvent.status, AgentBatch.STATUS_EDITTING);
        return eventDao.delete(workSpaceId, condition) > 0;
    }

}
