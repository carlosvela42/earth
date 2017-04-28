package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrCal;

public interface LicenseHistoryServiceTest {
    boolean insertStrCals(StrCal strCal) throws EarthException;

    boolean deleteAll() throws EarthException;
}
