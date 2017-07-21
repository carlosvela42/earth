package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.SiteDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.util.ConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl extends BaseService implements SiteService {

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
            return siteDao.getAllSiteIds();
        }), Integer.class);
    }

    @Override
    public boolean deleteSites(List<Integer> siteIds, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return siteDao.deleteSites(siteIds, workspaceId) > 0;
        });
    }

    @Override
    public boolean insertOne(String siteId, List<String> directoryIds, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return siteDao.insertOne(siteId, directoryIds, workspaceId) > 0;
        });
    }

    @Override
    public boolean updateSite(String siteId, List<String> directoryIds, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            siteDao.deleteSite(siteId, workspaceId);
            siteDao.insertOne(siteId, directoryIds, workspaceId);
            return true;
        });
    }

}
