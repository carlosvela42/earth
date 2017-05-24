package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import co.jp.nej.earth.dao.SiteDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.util.ConversionUtil;

@Service
public class SiteServiceImpl extends BaseService implements SiteService {

    private static final Logger LOG = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private SiteDao siteDao;

    @Override
    public List<Site> getAllSites() throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return siteDao.findAll(Constant.EARTH_WORKSPACE_ID);
        }), Site.class);
    }

    @Override
    public List<Integer> getAllSiteIds(String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
            return siteDao.getAllSiteIds(workspaceId);
        }), Integer.class);
    }

    @Override
    public boolean deleteSites(List<Integer> siteIds, String workspaceId) throws EarthException {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            txStatus = transactionManager.getTransaction(txDef);
            long deleted = siteDao.deleteSites(siteIds, workspaceId);
            if (deleted < siteIds.size()) {
                throw new EarthException("Delete unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException ex) {
            LOG.error("SiteServiceImpl:deleteSites:" + ex.getMessage());
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean insertOne(String siteId, List<String> directoryIds, String workspaceId) throws EarthException {
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            txStatus = transactionManager.getTransaction(txDef);
            long inserted = siteDao.insertOne(siteId, directoryIds, workspaceId);
            if (inserted <= 0) {
                throw new EarthException("Insert Template unsuccessfully!");
            }
            transactionManager.commit(txStatus);
        } catch (EarthException ex) {
            LOG.error("TemplateServiceImpl:insertOne:" + ex.getMessage());
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
            return false;
        }
        return true;
    }

    @Override
    public List<Directory> getDirectorysBySiteId(String siteId, String workspaceId) throws EarthException {
        List<Directory> directoryIds = new ArrayList<>();
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            transactionManager = ConnectionManager.getTransactionManager(Constant.EARTH_WORKSPACE_ID);
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            txStatus = transactionManager.getTransaction(txDef);
            directoryIds = siteDao.getDirectorysBySiteId(siteId, workspaceId);
            transactionManager.commit(txStatus);
        } catch (EarthException ex) {
            LOG.error("SiteServiceImpl:getAllSite:" + ex.getMessage());
        }
        return directoryIds;
    }
}
