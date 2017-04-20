package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrCal;

public interface LicenseHistoryDao extends BaseDao<StrCal> {
    List<StrCal> search(String fromTime, String toTime) throws EarthException;
}
