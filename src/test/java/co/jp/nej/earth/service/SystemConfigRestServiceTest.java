package co.jp.nej.earth.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import co.jp.nej.earth.BaseTest;

public class SystemConfigRestServiceTest extends BaseTest{

    @Autowired
    SystemConfigurationService systemConfigurationService;

    @Test
    public void updateSystemConfig() throws Exception {
        Assert.assertTrue(systemConfigurationService.updateSystemConfig("18/05/2017").isResult());

    }
}
