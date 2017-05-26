package co.jp.nej.earth.processservice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.service.LogAccessService;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.service.WorkspaceService;
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

    private JdbcConfig config;

    private WorkspaceService workspaceService;

    private MessageSource messageSource;

    private String workspaceId;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final int NUM_3 = 3;
    private static final int NUM_4 = 4;
    private static final int NUM_5 = 5;
    private static final int NUM_6 = 6;
    private static final int NUM_7 = 7;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
            workspaceId = ApplicationContextUtil.getWorkspaceId();
            service = applicationContext.getBean(EventControlService.class);
            workItemService = applicationContext.getBean(WorkItemService.class);
            logAccessService = applicationContext.getBean(LogAccessService.class);
            config = applicationContext.getBean(JdbcConfig.class);
            messageSource = applicationContext.getBean(MessageSource.class);
            workspaceService = applicationContext.getBean(WorkspaceService.class);
            handlingEventData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlingEventData() throws Exception {
        CtlEvent event = getAndUpdateEvent();
        if (event == null) {
            return;
        }
        WorkItem workItem = MAPPER.readValue(event.getWorkitemData(), WorkItem.class);
        workItem.setWorkspaceId(workspaceId);
        // TODO
        // workItemService.receiveNextTask
        // insert or update work item
        workItemService.insertOrUpdateWorkItemToDb(workItem);
        // TODO
        // workItemService.insertOrUpdateWorkItemToDb
        // delete event;
        service.deleteEvent(event, workspaceId);
        // write log
        StrLogAccess logAccess = new StrLogAccess();
        // logAccess.setWorkitemId(workItem.getWorkitemId());
        logAccess.setEventId(event.getEventId());
        logAccess.setProcessTime(DateUtil.getCurrentDateString());
        logAccess.setTaskId(event.getTaskId());
        // TODO
        // set value userId, history No, processId
        logAccess.setUserId(event.getUserId());
        // auto increment
        logAccess.setHistoryNo(1);
        logAccess.setProcessId(1);
        logAccess.setProcessVersion(1);
        logAccessService.addLogAccess(logAccess, workspaceId);
        handlingEventData();
    }

    private CtlEvent getAndUpdateEvent() throws Exception {
        DataSource dataSource = null;
        // get connection form workspaceService by workspaceId
        MgrWorkspaceConnect mgrWorkspaceConnect = workspaceService.getMgrConnectionByWorkspaceId(workspaceId);
        if (mgrWorkspaceConnect == null) {
            throw new EarthException(messageSource.getMessage("connection.notfound", new String[] { "workspaceId" },
                    Locale.getDefault()));
        } else {
            dataSource = config.dataSource(mgrWorkspaceConnect.getWorkspaceId(), mgrWorkspaceConnect);
        }
        CallableStatement callableStatement = null;
        Connection dbConnection = null;
        CtlEvent event = null;
        try {
            // call store procedure with 7 parameters
            dbConnection = dataSource.getConnection();
            callableStatement = dbConnection.prepareCall("{call UpdateEventProc(?,?,?,?,?,?,?)}");
            callableStatement.registerOutParameter(1, java.sql.Types.NVARCHAR);
            callableStatement.registerOutParameter(2, java.sql.Types.NVARCHAR);
            callableStatement.registerOutParameter(NUM_3, java.sql.Types.NVARCHAR);
            callableStatement.registerOutParameter(NUM_4, java.sql.Types.NVARCHAR);
            callableStatement.registerOutParameter(NUM_5, java.sql.Types.NVARCHAR);
            // TODO check final db to see the workitemdata column type
            callableStatement.registerOutParameter(NUM_6, java.sql.Types.NCLOB);
            callableStatement.registerOutParameter(NUM_7, java.sql.Types.NUMERIC);
            // execute store procedure
            callableStatement.executeUpdate();
            // if can't find any event
            if (callableStatement.getInt(NUM_7) != 1) {
                event = new CtlEvent();
                event.setEventId(callableStatement.getString(1));
                event.setUserId(callableStatement.getString(2));
                event.setWorkitemId(callableStatement.getString(NUM_3));
                event.setStatus(callableStatement.getString(NUM_4));
                event.setTaskId(callableStatement.getString(NUM_5));
                // TODO check final db to see the workitemdata column type
                NClob workItemDataClob = callableStatement.getNClob(NUM_6);
                if (workItemDataClob != null) {
                    event.setWorkitemData(CharStreams.toString(workItemDataClob.getCharacterStream()));
                }
            }
            return event;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (callableStatement != null) {
                callableStatement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }

    }

}
