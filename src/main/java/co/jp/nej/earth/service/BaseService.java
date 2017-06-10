package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.manager.connection.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.util.EMessageResource;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.*;
import org.springframework.transaction.support.*;

import java.util.*;

public abstract class BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected EMessageResource messageSource;

    public interface ServiceCaller {
        Object execute() throws EarthException;
    }

    protected Object executeTransaction(ServiceCaller caller) throws EarthException {
        String workspaceId = co.jp.nej.earth.model.constant.Constant.EARTH_WORKSPACE_ID;
        return executeTransaction(workspaceId, caller);
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

    protected boolean processCommitTransactions(List<TransactionManager> transactionManagers) {
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            transactionManagers.get(i).getManager().commit(transactionManagers.get(i).getTxStatus());
        }

        return true;
    }

    protected boolean processRollBackTransactions(List<TransactionManager> transactionManagers) {
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            transactionManagers.get(i).getManager().rollback(transactionManagers.get(i).getTxStatus());
        }
        return true;
    }
}
