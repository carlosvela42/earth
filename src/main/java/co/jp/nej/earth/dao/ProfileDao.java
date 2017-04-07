package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrProfile;

import java.util.List;

public interface ProfileDao {
    public List<MgrProfile> getProfilesByUserId(String userId) throws EarthException;

    public MgrProfile getById(String profileId) throws EarthException;

    public List<MgrProfile> getAll() throws EarthException;

    public boolean deleteList(List<String> profileId) throws EarthException;

    public MgrProfile insertOne(MgrProfile mgrProfile) throws EarthException;

    public boolean assignUsers(String profileId, List<String> userIds) throws EarthException;

    public boolean unAssignAllUsers(String profileIds) throws EarthException;

    public MgrProfile updateOne(MgrProfile mgrProfile) throws EarthException;
}
