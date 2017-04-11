package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrUserProfile;

import java.util.List;

/**
 * Created by minhtv on 3/29/2017.
 */
public interface UserProfileDao {
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;

    public List<MgrUserProfile> getListByProfileIds(List<String> profileIds) throws EarthException;

}
