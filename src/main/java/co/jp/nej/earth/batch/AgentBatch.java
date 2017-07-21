package co.jp.nej.earth.batch;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.processservice.UpdateProcessDataService;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.ApplicationContextUtil;
import co.jp.nej.earth.util.DateUtil;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.PropertySource;

import java.util.Date;
import java.util.List;

@Configuration
// @PropertySource("classpath:application.properties")
//@PropertySource("classpath:batch_config.properties")
@Import(JdbcConfig.class)
@ComponentScan(basePackages = { "co.jp.nej.earth" }, excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Configuration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co.jp.nej.earth.web.controller.*") })
public class AgentBatch {

    private static final int MAX_PARA_NEED = 4;

    private static final int PARA_PROCESS_SEVICE_ID =0;
    private static final int PARA_USER_ID =1;
    private static final int PARA_PASSWORD = 2;
    private static final int PARA_MAX_THREAD = 3;
    private static final int PARA_THREAD_SLEEP = 4;

    @Autowired
    private ScheduleService scheduleService;

    //@Autowired
    //@Value("${batch.processServiceID}")
    //private int processServiceID;

    //@Value("${batch.userID}")
    private String userID;

    //@Value("${batch.pwd}")
    private String pwd;



    @Autowired
    private UserService userService;

    // method create scheduleJob

    @SuppressWarnings("unchecked")
    public void createCronJob(String className, String schedule, String jobKey, String triggerKey, int wpID)
            throws SchedulerException, ClassNotFoundException {
        Class<? extends Job> processClass = (Class<? extends Job>) Class.forName(className);

        JobKey jk = new JobKey(jobKey);
        JobDetail job = JobBuilder.newJob(processClass).withIdentity(jk).build();

        TriggerKey tk = new TriggerKey(triggerKey);

        //Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tk)
        //        .withSchedule(CronScheduleBuilder.cronSchedule(schedule)).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tk)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()).build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void run(int processServiceId) throws ClassNotFoundException, SchedulerException, EarthException {

        // Check login batch
        List<co.jp.nej.earth.model.Message> messages = userService.loginBatch(userID, pwd);

        // Get schedule if userID pass validate
        if (messages == null || messages.size() == 0) {
            // Get list schedule
            List<MgrSchedule> listSchedule = scheduleService.getScheduleByProcessServiceId(processServiceId);
            if (listSchedule != null)  {
                if(listSchedule.size()>0) {
                    Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                    //scheduler.getListenerManager().addTriggerListener(new AgentTriggerListener());
                    scheduler.getListenerManager().addJobListener(new AgentJobListener());

                    scheduler.start();

                    for (MgrSchedule schedule : listSchedule) {
                        try {
                            ScheduleTimeHelper helper = new ScheduleTimeHelper(schedule);

                            String workspaceId = ApplicationContextUtil.getWorkspaceId();
                            String className = schedule.getClassName();
                            String jobId = schedule.getScheduleId();
                            int interval = helper.getTotalSecond();

                            Date startTime = helper.getStartTime();
                            Date endTime = helper.getEndTime();
                            Date nextTime = helper.getNextTime();
                            Date currentTime = DateUtil.getCurrentDate();

                            @SuppressWarnings("unchecked")
                            Class<? extends Job> processClass = (Class<? extends Job>) Class.forName(className);


                            JobKey jobKey = new JobKey(Constant.AgentBatch.PRE_JOB_KEY + jobId);
                            JobDetail job = JobBuilder.newJob(processClass).withIdentity(jobKey).build();
                            job.getJobDataMap().put(Constant.AgentBatch.P_WORKSPACE_ID, workspaceId);
                            job.getJobDataMap().put(Constant.AgentBatch.P_USER_ID, userID);
                            job.getJobDataMap().put(Constant.AgentBatch.P_SCHEDULE_ID, schedule.getScheduleId());
                            job.getJobDataMap().put(Constant.AgentBatch.P_END_TIME, schedule.getEndTime());
                            job.getJobDataMap().put(Constant.AgentBatch.P_INTERVAL_SECOND, interval);

                            TriggerKey triggerKey = new TriggerKey(Constant.AgentBatch.PRE_TRIGER_KEY + jobId);

                            SimpleScheduleBuilder jobSchedule = SimpleScheduleBuilder.simpleSchedule();
                            jobSchedule.withIntervalInSeconds(interval).withRepeatCount(1);
                            //jobSchedule.withIntervalInSeconds(interval).repeatForever();

                            Date triggerStartTime = startTime;
                            // If time is over then run job immediately
                            if(nextTime.after(currentTime)) {
                                triggerStartTime = nextTime;
                            }

                            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                                .startAt(triggerStartTime)
                                .withSchedule(jobSchedule)
                                .endAt(endTime).build();

                            scheduler.scheduleJob(job, trigger);

                            //createSimpleJob(workspaceId, className, jobId, interval, repeatCount );

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                }
            }
        }
        // else{
        // // Write some thing log
        // }

    }

    private final class ScheduleTimeHelper {

        private static final int HOUR_PER_DAY = 24;
        private static final int MINUTE_IN_HOUR = 60;
        private static final int SECOND_IN_MINUTE = 60;

        //private MgrSchedule schedule;

        private int dayInterval;
        private int hourInterval;
        private int minuteInterval;
        private int secondInterval;
        private int total;
        private Date nextTime;
        private Date startTime;
        private Date endTime;

        ScheduleTimeHelper(MgrSchedule schedule) throws Exception{
            //this.schedule = schedule;
            dayInterval = Integer.parseInt(schedule.getRunIntervalDay());
            hourInterval = Integer.parseInt(schedule.getRunIntervalHour());
            minuteInterval = Integer.parseInt(schedule.getRunIntervalMinute());
            secondInterval = Integer.parseInt(schedule.getRunIntervalSecond());
            int total = dayInterval;
            total = total * HOUR_PER_DAY + hourInterval;
            total = total * MINUTE_IN_HOUR + minuteInterval;
            this.total = total * SECOND_IN_MINUTE + secondInterval;

            this.nextTime = DateUtil.convertStringSimpleDateFormat(schedule.getNextRunDate(),
                     Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSSSSS);
            this.startTime = DateUtil.convertStringSimpleDateFormat(schedule.getStartTime(),
                    Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSSSSS);
            this.endTime = DateUtil.convertStringSimpleDateFormat(schedule.getEndTime(),
                    Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSSSSS);
        }
        public int getTotalSecond(){
            return total;
        }

        public Date getStartTime() throws Exception {
            return startTime;
        };
        public Date getEndTime() throws Exception {
            return endTime;
        };

        public Date getNextTime() throws Exception {
            return nextTime;
        };

    }

    public static void main(String[] args) throws EarthException, ClassNotFoundException, SchedulerException {
        /*System.out.print("ARGS= ");
        for (String arg: args){
            System.out.print(arg + " ");
        }
        System.out.println();*/

        ApplicationContext context = new AnnotationConfigApplicationContext(AgentBatch.class);
        ApplicationContextUtil appUtil = new ApplicationContextUtil();
        appUtil.setApplicationContext(context);

        AgentBatch agentBatch = context.getBean(AgentBatch.class);

        if (args.length > MAX_PARA_NEED) {
            try {
                int processServiceId = Integer.parseInt(args[PARA_PROCESS_SEVICE_ID]);
                String userID = args[PARA_USER_ID];
                String pass = args[PARA_PASSWORD];
                int maxThread = Integer.parseInt(args[PARA_MAX_THREAD]);
                int threadSleep = Integer.parseInt(args[PARA_THREAD_SLEEP]);

                int workspaceId = agentBatch.scheduleService.getWorkspaceByProcessServiceId(processServiceId);
                if (workspaceId>0) {
                    appUtil.setWorkspaceId(Integer.toString(workspaceId));
                    agentBatch.userID = userID;
                    agentBatch.pwd = pass;
                    agentBatch.run(processServiceId);
                    //System.out.
                    //println(agentBatch.userID + "-" + agentBatch.pwd + "-" + processServiceId + "-" + workspaceId);
                }
                startUpdateProcess(userID, processServiceId, maxThread, threadSleep);
            } catch(NumberFormatException numberFormatError) {
                System.out.println("The processServiceId need to be a integer.");
            }
        } else {
            System.out.println("Need parameters: processServiceId, userID, pass, max_thread, thread_sleep");
        }

    }

    private static void startUpdateProcess(String user, int processSId, int maxThread, int threadSleep) {
        new UpdateProcessDataService().execute(user, processSId, maxThread, threadSleep);
    }

}
