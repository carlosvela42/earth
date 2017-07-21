package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;

/**
 * @author p-tvo-sonta
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
    boolean updateSystemConfig(String inputDate) throws EarthException;
}
