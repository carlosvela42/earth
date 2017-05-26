package co.jp.nej.earth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.ScreenItem;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EMessageResource;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
public class EventControlServiceImpl extends BaseService implements EventControlService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private EMessageResource eMessageResource;
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventControlServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insertEvent(String workspaceId, WorkItem workItem) throws EarthException {
        return (boolean) this.executeTransaction(workspaceId, () -> {
            try {
                // parse object to json
                String json = null;
                try {
                    json = new ObjectMapper().writeValueAsString(workItem);
                } catch (JsonProcessingException e) {
                    LOG.error(e.getMessage());
                    throw new EarthException(e);
                }
                CtlEvent event = new CtlEvent();
                // TODO
                // user function increase event id
                event.setEventId(Integer.toString((eventDao.findAll(workspaceId).size() + 1)));
                event.setStatus(AgentBatch.STATUS_EDIT);
                event.setTaskId("1");
                event.setWorkitemData(json);
                event.setWorkitemId(" -1");
                event.setUserId("1");
                return eventDao.add(workspaceId, event) > 0;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListEventIdByStatus(String status, String workSpaceId) throws EarthException {
        return ConversionUtil.castList(this.executeTransaction(workSpaceId, () -> {
            try {
                return eventDao.getListCtlEventIdByStatus(status, workSpaceId);
            } catch (Exception e) {
                throw new EarthException(e);
            }
        }), String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException {
        return (boolean) this.executeTransaction(workSpaceId, () -> {
            try {
                return eventDao.updateBulkEventStatus(eventIds, workSpaceId);
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CtlEvent getEventByEventId(String eventId, String workSpaceId) throws EarthException {
        return (CtlEvent) this.executeTransaction(workSpaceId, () -> {
            try {
                Map<Path<?>, Object> mapCondition = new HashMap<>();
                mapCondition.put(QCtlEvent.newInstance().eventId, eventId);
                return eventDao.findOne(workSpaceId, mapCondition);
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEvent(CtlEvent event, String workSpaceId) throws EarthException {
        return (boolean) this.executeTransaction(workSpaceId, () -> {
            try {
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.eventId, event.getEventId());
                condition.put(qCtlEvent.status, AgentBatch.STATUS_EDITTING);
                return eventDao.delete(workSpaceId, condition) > 0;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RestResponse unlockEventControl(String workspaceId, String eventId) throws EarthException {
        return (RestResponse) this.executeTransaction(workspaceId, () -> {
            try {
                RestResponse response = new RestResponse();
                QCtlEvent qCtlEvent = QCtlEvent.newInstance();
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlEvent.eventId, eventId);
                CtlEvent event = eventDao.findOne(workspaceId, condition);
                if (event == null) {
                    response.setResult(false);
                    response.setData(eMessageResource.get(ErrorCode.E0002, new String[] { ScreenItem.EVEN_TID }));
                    return response;
                }
                String eventStatus = event.getStatus();
                boolean result = false;
                if (AgentBatch.STATUS_OPEN.equalsIgnoreCase(eventStatus)) {
                    result = eventDao.delete(workspaceId, condition) > 0;
                } else if (AgentBatch.STATUS_EDITTING.equalsIgnoreCase(eventStatus)) {
                    Map<Path<?>, Object> updateMap = new HashMap<>();
                    updateMap.put(qCtlEvent.status, AgentBatch.STATUS_EDIT);
                    result = eventDao.update(workspaceId, condition, updateMap) > 0;
                }
                response.setResult(result);
                return response;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

}
