package co.jp.nej.earth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.dao.EventDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.xml.WorkItem;

/**
 * 
 * @author p-tvo-sonta
 *
 */
@Transactional
@Service
public class EventControlServiceImpl implements EventControlService {

    @Autowired
    EventDao eventDao;
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EventControlServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertEvent(WorkItem workItem) throws EarthException {
        // parse object to json
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(workItem.getAttributes());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            LOG.error(e.getMessage());
        }
        // Gson gson = new JsonObject(workItem.getAttributes());
        CtlEvent event = new CtlEvent();
        // TODO
        event.setStatus("edit");
        event.setTaskId(Long.toString(workItem.getTaskId()));
        event.setWorkitemData(json);
        event.setWorkitemId(Long.toString(workItem.getWorkItemId()));
        event.setUserId("1");
        return eventDao.insertEvent(event);
    }

}
