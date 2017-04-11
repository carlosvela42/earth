package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.entity.MgrUserProfile;

public interface UserDao {
	public MgrUser getById(String userId) throws EarthException;

	public List<MgrUser> getAll() throws EarthException;

	public MgrUser insertOne(MgrUser mgrUser) throws EarthException;

	public MgrUser updateOne(MgrUser mgrUser) throws EarthException;

	public boolean deleteList(List<String> userIds) throws EarthException;

	public List<MgrUser> getUsersByProfileId(String profileId) throws EarthException;

	public List<String> getUserIdsByProfileId(String profileId) throws EarthException;
}