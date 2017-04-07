package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.StrCal;

public interface LoginControlDao {
    public boolean insertOne(CtlLogin ctlLogin) throws EarthException;
    
    public List<StrCal> getNumberOnlineUserByProfile(String userId) throws EarthException;
    
    public boolean updateOutTime(String sessionId, String outTime) throws EarthException;

    public boolean deleteListByUserIds(List<String> userIds) throws Exception;
}