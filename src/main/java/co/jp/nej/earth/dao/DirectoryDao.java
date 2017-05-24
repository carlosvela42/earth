package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;

public interface DirectoryDao extends BaseDao<Directory> {
    List<Directory> getDirectoriesBySite(int siteId) throws EarthException;
}
