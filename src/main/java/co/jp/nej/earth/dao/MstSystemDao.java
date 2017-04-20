package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MstSystem;

public interface MstSystemDao {
    List<MstSystem> getMstSystem() throws EarthException;

    List<MstSystem> getMstSystemBySection(String section) throws EarthException;

    /**
     * get system config by section and variable name
     *
     * @param section
     * @param variable
     * @return system config
     * @throws EarthException
     */
    MstSystem getMstSystemBySectionAndVariable(String section, String variable) throws EarthException;

    /**
     * update system date by section, variable name
     *
     * @param section
     * @param variable
     * @param configValue
     * @return
     * @throws EarthException
     */
    int updateMstSystem(String section, String variable, String configValue) throws EarthException;

}
