package co.jp.nej.earth.processservice;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.service.SystemConfigurationService;
import co.jp.nej.earth.util.ApplicationContextUtil;

@Component
public class OperationDateProcessService implements Job {

    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(OperationDateProcessService.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            changeOperationDate();
        } catch (EarthException e) {
            LOG.error(e.getMessage());
        }
    }

    private void changeOperationDate() throws EarthException {
        ApplicationContext context = ApplicationContextUtil.getApplicationContext();
        SystemConfigurationService service = context.getBean(SystemConfigurationService.class);
        service.updateSystemConfig();
    }
}
