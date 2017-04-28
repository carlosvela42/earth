package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrCal;

public interface LicenseHistoryService {
    List<StrCal> search(String fromTime, String toTime) throws EarthException;
}
