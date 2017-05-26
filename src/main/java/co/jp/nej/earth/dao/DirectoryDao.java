package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;

public interface DirectoryDao extends BaseDao<Directory> {
  List<Directory> getDirectoriesBySite(int siteId) throws EarthException;

  List<Directory> getAll(String workspaceId) throws EarthException;

  List<Integer> getDirectoryIds(int siteId, String workspaceId) throws EarthException;

  long deleteDirectorys(List<Integer> directoryIds, String workspaceId) throws EarthException;

  Directory getById(int directoryId) throws EarthException;

  long insertOne(Directory directory, String workspaceId) throws EarthException;

  long udpateOne(Directory directory, String workspaceId) throws EarthException;
}
