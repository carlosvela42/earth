package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrCustomTask;
import co.jp.nej.earth.model.sql.QMgrCustomTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manager custom task
 *
 * @author DaoPQ
 * @version  1.0
 */
@Repository
public class CustomTaskDaoImpl extends BaseDaoImpl<MgrCustomTask> implements CustomTaskDao {

    private static final Logger LOG = LoggerFactory.getLogger(CustomTaskDaoImpl.class);

    public CustomTaskDaoImpl() throws Exception {
        super();
    }

    @Override
    public List<MgrCustomTask> getAllCustomTasks() throws EarthException {
        try {
            return this.findAll(Constant.EARTH_WORKSPACE_ID);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }

    @Override
    public Map<String, MgrCustomTask> getCustomTasksMap() throws EarthException {
        try {
            Map<String, MgrCustomTask> dataMap = new LinkedHashMap<>();

            BooleanBuilder condition = new BooleanBuilder();
            Predicate predicate = QMgrCustomTask.newInstance().className.isNotEmpty();
            condition.and(predicate);

            List<MgrCustomTask> listAll = this.search(Constant.EARTH_WORKSPACE_ID, condition);
            for(MgrCustomTask mgrCustomTask:listAll) {
                dataMap.put(mgrCustomTask.getCustomTaskId(), mgrCustomTask);
            }
            return dataMap;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }
}
