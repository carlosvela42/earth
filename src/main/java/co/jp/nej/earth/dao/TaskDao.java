package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrTask;

import java.util.Map;

public interface TaskDao extends BaseDao<MgrTask> {
    Map<String, String> getAllTasks(String workspaceId) throws EarthException;
}
