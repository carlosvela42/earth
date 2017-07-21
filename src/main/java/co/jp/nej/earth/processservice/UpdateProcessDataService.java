package co.jp.nej.earth.processservice;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.service.ProcessIServiceService;
import co.jp.nej.earth.service.WorkItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Class WorkItemDataUpdateProcessService
 *
 * @author DaoPQ
 */
@Configuration
@PropertySource("classpath:batch_config.properties")
@Import(JdbcConfig.class)
@ComponentScan(basePackages = { "co.jp.nej.earth" }, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co.jp.nej.earth.web.controller.*") })
public class UpdateProcessDataService {

    @Value("${batch.userID}")
    private String userID;

    @Value("${batch.processServiceID}")
    private int processServiceID;

    @Value("${batch.maxThread}")
    private int maxThread;

    @Value("${batch.threadSleepTime}")
    private int threadSleepTime;

    @Value("${batch.coreMaxCoolSize}")
    private int coreMaxCoolSize;

    @Value("${batch.maxPoolSize}")
    private int maxPoolSize;

    @Value("${batch.hostName}")
    private String hostName;

    private static final Logger LOG = LoggerFactory.getLogger(UpdateProcessDataService.class);

    public void execute(String user, int processSId, int maxThread, int threadSleep) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(UpdateProcessDataService.class);
            EventControlService eventControlService = context.getBean(EventControlService.class);
            WorkItemService workItemService = context.getBean(WorkItemService.class);
            ProcessIServiceService processIServiceService = context.getBean(ProcessIServiceService.class);

            userID = user;
            processServiceID = processSId;
            maxPoolSize = maxThread;
            threadSleepTime = threadSleep;

            // Set dummy data
            setDummyData();

            ThreadPoolTaskExecutor taskExecutor = taskExecutor(coreMaxCoolSize, maxPoolSize);
            ExecuteTaskProcessUpdate task1 = new ExecuteTaskProcessUpdate();
            task1.setEventControlService(eventControlService);
            task1.setWorkItemService(workItemService);
            task1.setProcessIServiceService(processIServiceService);
            task1.setUserId(userID);
            task1.setProcessServiceID(processServiceID);
            taskExecutor.execute(task1);

            while (true) {
                int count = taskExecutor.getActiveCount();
                LOG.info("Active Threads : " + count);
                try {
                    Thread.sleep(threadSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == 0) {
                    taskExecutor.shutdown();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDummyData() {
        final int defaultProcessServiceId = 9;
        final int coreMaxCoolSize = 5;
        final int maxPoolSize = 10;
        this.processServiceID = defaultProcessServiceId;
        this.coreMaxCoolSize = coreMaxCoolSize;
        this.maxPoolSize = maxPoolSize;
        this.userID = "admin";
    }


    /**
     * Create Thread Pool
     *
     * @param coreMaxCoolSize Max cool size
     * @param maxPoolSize Max pool size
     * @return ThreadPoolTaskExecutor
     */
    private ThreadPoolTaskExecutor taskExecutor(int coreMaxCoolSize, int maxPoolSize) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.initialize();
        pool.setCorePoolSize(coreMaxCoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

    @Component
    @Scope("prototype")
    private class ExecuteTaskProcessUpdate implements Runnable {

        private EventControlService eventControlService;
        private ProcessIServiceService processIServiceService;
        private String userId;
        private Integer processServiceID;
        private WorkItemService workItemService;

        void setWorkItemService(WorkItemService workItemService) {
            this.workItemService = workItemService;
        }

        void setEventControlService(EventControlService eventControlService) {
            this.eventControlService = eventControlService;
        }

        void setProcessIServiceService(ProcessIServiceService processIServiceService) {
            this.processIServiceService = processIServiceService;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        void setProcessServiceID(Integer processServiceID) {
            this.processServiceID = processServiceID;
        }

        public void run() {
            try {
                String workspaceId =
                    String.valueOf(processIServiceService.getWorkSpaceIdByProcessServiceId(processServiceID));
                CtlEvent event = eventControlService.getOneEventByStatusForUpdate(AgentBatch.STATUS_EDIT,
                    workspaceId, userId);
                if (event == null) {
                    return;
                }
                String hostName = "127001";
                workItemService.insertOrUpdateWorkItemToDbFromEvent(workspaceId,Thread.currentThread().getId(),hostName,
                    processServiceID, userId);
            } catch (Exception ex) {
                LOG.debug(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
