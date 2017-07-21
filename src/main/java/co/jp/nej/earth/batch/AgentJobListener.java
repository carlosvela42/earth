package co.jp.nej.earth.batch;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.listeners.JobListenerSupport;

import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.util.ApplicationContextUtil;
import co.jp.nej.earth.util.DateUtil;

public class AgentJobListener extends JobListenerSupport {

    private static final String NAME_LISTENER = "AgentJobListener";

    private String name = NAME_LISTENER;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        super.jobToBeExecuted(context);
        ScheduleService scheduleService = ApplicationContextUtil.getApplicationContext().getBean(ScheduleService.class);

        System.out.println("go AgentJobListener:" + context.getFireTime() + "-" + context.getNextFireTime());

        // Retrieve parameters to calculate state of job
        String workspaceId = context.getJobDetail().getJobDataMap().getString(Constant.AgentBatch.P_WORKSPACE_ID);
        String scheduleId = context.getJobDetail().getJobDataMap().getString(Constant.AgentBatch.P_SCHEDULE_ID);
        //String endTime = context.getJobDetail().getJobDataMap().getString(Constant.AgentBatch.P_END_TIME);
        int intervalSecond = context.getJobDetail().getJobDataMap().getInt(Constant.AgentBatch.P_INTERVAL_SECOND);

        // Calculate the value of the next_run_time to update into the database
        Date nextFireTime = context.getNextFireTime();
        try {
            String nextTime = "";
            if(nextFireTime != null) {

                // Update next_run_time
                nextTime = DateUtil.convertSimpleDateFormat(nextFireTime,
                    Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);

            } else {
                // Update the next_run_time at the last fire
                DateTime fireTime =new DateTime(context.getFireTime());
                fireTime.plusSeconds(intervalSecond);
                nextTime = fireTime.toString(Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);
            }
            scheduleService.updateNextRunDateByScheduleId(workspaceId, scheduleId, nextTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
