package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrProfile;

import java.util.List;

public interface ProfileDao {
    List<MgrProfile> getProfilesByUserId(String userId) throws EarthException;

    MgrProfile getById(String profileId) throws EarthException;

    List<MgrProfile> getAll() throws EarthException;

    boolean deleteList(List<String> profileIds) throws EarthException;

    MgrProfile insertOne(MgrProfile mgrProfile) throws EarthException;

    boolean assignUsers(String profileId, List<String> userIds) throws EarthException;

    boolean unAssignAllUsers(String profileIds) throws EarthException;

    MgrProfile updateOne(MgrProfile mgrProfile) throws EarthException;
}
