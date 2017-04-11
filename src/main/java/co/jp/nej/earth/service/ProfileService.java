package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;

import java.util.List;
import java.util.Map;

/**
 * Created by minhtv on 3/30/2017.
 */
public interface ProfileService {
    public  List<MgrProfile> getAll()  throws EarthException;
    public Map<String,Object> getDetail(String profileId) throws EarthException;
    public List<Message> validate(MgrProfile mgrProfile, boolean insert) ;
    public boolean insertAndAssignUsers(MgrProfile mgrProfile,List<String> userIds) throws EarthException ;
    public boolean updateAndAssignUsers(MgrProfile mgrProfile,List<String> userIds) throws EarthException  ;
    public boolean deleteList(List<String> profileIds) throws EarthException   ;
}
