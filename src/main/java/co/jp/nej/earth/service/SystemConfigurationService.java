package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ws.RestResponse;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface SystemConfigurationService {
    /**
     * update operation time of system config
     *
     * @return number of records is changed
     * @throws EarthException
     */
    int updateSystemConfig() throws EarthException;

    /**
     * update operation time of system config
     *
     * @param inputDate
     * @return
     * @throws EarthException
     */
    RestResponse updateSystemConfig(String inputDate) throws EarthException;
}
