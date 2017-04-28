package co.jp.nej.earth.processservice;

import java.io.IOException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.service.LogAccessService;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.util.ApplicationContextUtil;
import co.jp.nej.earth.util.DateUtil;

/**
 * @author p-tvo-sonta
 *
 */
@Component
public class WorkItemDataUpdateProcessService implements Job {

    /**
     * service
     */
    private EventControlService service;
    /**
     * workItemService
     */
    private WorkItemService workItemService;
    /**
     * LogAccessService
     */
    private LogAccessService logAccessService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logic();
        } catch (EarthException e) {
            e.printStackTrace();
        }

    }

    private void logic() throws EarthException {
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        service = applicationContext.getBean(EventControlService.class);
        workItemService = applicationContext.getBean(WorkItemService.class);
        logAccessService = applicationContext.getBean(LogAccessService.class);
        loopingGetEditEvent();
    }

    private void loopingGetEditEvent() throws EarthException {
        List<String> eventIds = service.getListEventIdByStatus(AgentBatch.STATUS_EDIT, Constant.EARTH_WORKSPACE_ID);
        if (eventIds.isEmpty()) {
            loopingGetEditEvent();
        }
        // update event to editting
        service.updateBulkEventStatus(eventIds, Constant.EARTH_WORKSPACE_ID);
        // lop eventIds
        for (String eventId : eventIds) {
            Thread t = new Thread(new WorkItemDataUpdateThread(eventId, service, Constant.EARTH_WORKSPACE_ID,
                    workItemService, logAccessService));
            t.start();
        }
    }

}

class WorkItemDataUpdateThread implements Runnable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String eventId;
    private EventControlService service;
    private String workSpaceId;
    private WorkItemService workItemService;
    private LogAccessService logAccessService;

    WorkItemDataUpdateThread() {
        super();
    }

    WorkItemDataUpdateThread(String eventId, EventControlService service, String workSpaceId,
            WorkItemService workItemService, LogAccessService logAccessService) {
        super();
        this.eventId = eventId;
        this.service = service;
        this.workSpaceId = workSpaceId;
        this.workItemService = workItemService;
        this.logAccessService = logAccessService;
    }

    @Override
    public void run() {
        try {
            CtlEvent event = service.getEventByEventId(eventId, workSpaceId);
            // read work item data form json
            WorkItem workItem = MAPPER.readValue(event.getWorkitemData(), WorkItem.class);
            // insert or update work item
            workItemService.insertOrUpdateWorkItemToDb(workItem);
            // delete event;
            service.deleteEvent(event, workSpaceId);
            // write log
            StrLogAccess logAccess = new StrLogAccess();
            logAccess.setWorkitemId(workItem.getWorkitemId());
            logAccess.setEventId(eventId);
            logAccess.setProcessTime(DateUtil.getCurrentDateString());
            logAccess.setTaskId(workItem.getTaskId());
            // TODO
            // set value userId, history No, process version
            logAccess.setUserId("1");
            logAccess.setHistoryNo(1);
            logAccess.setProcessId(1);
            logAccess.setProcessVersion(1);
            logAccessService.addLogAccess(logAccess, workSpaceId);
        } catch (EarthException | IOException e) {
            // TODO Auto-generated catch block
            // write log and rollback
            e.printStackTrace();
        }

    }

}
