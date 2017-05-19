package co.jp.nej.earth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;

public abstract class BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

    public interface ServiceCaller {
        Object execute() throws EarthException;
    }

    protected Object executeTransaction(String workspaceId, ServiceCaller caller) throws EarthException {
        Object result = null;
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            LOG.info("Start New Transaction with workspaceId=" + workspaceId);
            TransactionDefinition txDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            result = caller.execute();
            transactionManager.commit(txStatus);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
            throw ex;
        } finally {
            LOG.info("Finish Transaction.");
        }
        return result;
    }

}
