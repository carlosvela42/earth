package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Site;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface SiteDao extends BaseDao<Site> {

    /**
     * get all file id
     *
     * @param workspaceId
     * @return
     * @throws EarthException
     */
    List<Integer> getAllSiteIds(String workspaceId) throws EarthException;

    long deleteSites(List<Integer> siteIds, String workspaceId) throws EarthException;

    long insertOne(String siteId, List<String> directoryIds, String workspaceId) throws EarthException;

    List<Directory> getDirectorysBySiteId(String siteId, String workspaceId) throws EarthException;
}