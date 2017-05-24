package co.jp.nej.earth.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;

public class SiteServiceTest extends BaseTest {

    @Autowired
    private SiteService siteService;

    @Test
    public void getAllSiteIdsTest() throws EarthException {
        Assert.assertTrue(siteService.getAllSiteIds(Constant.EARTH_WORKSPACE_ID).size() > 0);
    }

    @Test
    public void getAllSitesTest() throws EarthException {
        Assert.assertTrue(siteService.getAllSites().size() > 0);
    }
}
