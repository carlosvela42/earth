package co.jp.nej.earth.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrTask;

@Repository
public class TaskDaoImpl extends BaseDaoImpl<MgrTask> implements TaskDao {

    private static final Logger LOG = LoggerFactory.getLogger(TaskDaoImpl.class);

    public TaskDaoImpl() throws Exception {
        super();
    }

    @Override
    public Map<String, String> getAllTasks() throws EarthException {
        try {
            Map<String, String> mapTaks = new HashMap<>();
            List<MgrTask> mgrTasks = this.findAll(Constant.EARTH_WORKSPACE_ID);
            for (MgrTask mgrTask : mgrTasks) {
                mapTaks.put(mgrTask.getTaskId(), mgrTask.getTaskName());
            }
            return mapTaks;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }
}
