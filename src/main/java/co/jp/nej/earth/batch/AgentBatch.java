package co.jp.nej.earth.batch;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.util.ApplicationContextUtil;

@PropertySource("classpath:application.properties")
@Configuration
@Import(JdbcConfig.class)
@ComponentScan(basePackages = { "co.jp.nej.earth" }, excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
public class AgentBatch {

    @Autowired
    private ScheduleService scheduleService;

    // method create scheduleJob

    @SuppressWarnings("unchecked")
    public void createJob(String className, String timeStart, String jobKey, String triggerKey)
            throws SchedulerException, ClassNotFoundException {
        Class<? extends Job> processClass = (Class<? extends Job>) Class.forName(className);

        JobKey jk = new JobKey(jobKey);
        JobDetail job = JobBuilder.newJob(processClass).withIdentity(jk).build();

        TriggerKey tk = new TriggerKey(triggerKey);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tk)
                .withSchedule(CronScheduleBuilder.cronSchedule(timeStart)).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void run(String workspaceId) throws ClassNotFoundException, SchedulerException, EarthException {
        List<MgrSchedule> listSchedule = scheduleService.getSchedules(workspaceId);
        if (!listSchedule.isEmpty()) {
            for (MgrSchedule schedule : listSchedule) {
                createJob(schedule.getTaskId(), schedule.getStartTime(), schedule.getTaskId(), schedule.getTaskId());
            }
        }
    }

    public static void main(String[] args) throws EarthException, ClassNotFoundException, SchedulerException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AgentBatch.class);
        ApplicationContextUtil appUtil = new ApplicationContextUtil();
        appUtil.setApplicationContext(context);

        AgentBatch agentBatch = context.getBean(AgentBatch.class);
        String workspaceId = null;
        if (args.length > 0) {
            workspaceId = args[0];
        } else {
            workspaceId = Constant.EARTH_WORKSPACE_ID;
        }
        appUtil.setWorkspaceId(workspaceId);
        agentBatch.run(workspaceId);
    }

}
