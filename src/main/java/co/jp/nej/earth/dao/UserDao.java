package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrUser;

import java.util.List;

public interface UserDao extends BaseDao<MgrUser> {
    MgrUser getById(String userId) throws EarthException;

    List<MgrUser> getAll() throws EarthException;

    MgrUser updateOne(MgrUser mgrUser) throws EarthException;

    boolean deleteList(List<String> userIds) throws EarthException;

    List<MgrUser> getUsersByProfileId(String profileId) throws EarthException;

    List<String> getUserIdsByProfileId(String profileId) throws EarthException;
}