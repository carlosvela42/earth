package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Site;

import java.util.List;

public interface SiteService {

    List<Site> getAllSites() throws EarthException;

    List<Integer> getAllSiteIds(String workspaceId) throws EarthException;

    boolean deleteSites(List<Integer> siteIds, String workspaceId) throws EarthException;

    boolean updateSite(String siteId, List<String> directoryIds, String workspaceId) throws EarthException;

    boolean insertOne(String siteId, List<String> directoryIds, String workspaceId) throws EarthException;

}
