package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.StrCal;

public interface LoginControlDao extends BaseDao<CtlLogin> {

    boolean insertOne(CtlLogin ctlLogin) throws EarthException;

    List<StrCal> getNumberOnlineUserByProfile(String userId) throws EarthException;

    boolean updateOutTime(String sessionId, String outTime) throws EarthException;

    boolean deleteListByUserIds(List<String> userIds) throws EarthException;
}