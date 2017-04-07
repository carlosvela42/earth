package co.jp.nej.earth.processservice;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class ImportThemScanService implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logic();
        System.out.println("Import themscan service is processing .............      at " + new Date().toString());
    }
    
    public void logic(){
        
    }

}
