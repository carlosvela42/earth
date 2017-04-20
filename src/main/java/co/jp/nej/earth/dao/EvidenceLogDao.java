package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;

/**
 * Created by minhtv on 3/29/2017.
 */
public interface EvidenceLogDao {
    boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;
}
