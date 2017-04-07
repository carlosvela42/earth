package co.jp.nej.earth.processservice;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class OperationDate implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Operation system date service is processing .......      at " + new Date().toString());
    }
}
