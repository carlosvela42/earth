package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.ScheduleDao;
import co.jp.nej.earth.dao.TaskDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.util.ConversionUtil;
import com.querydsl.core.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl extends BaseService implements ScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public List<MgrSchedule> getSchedules(String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return scheduleDao.getSchedules(workspaceId);
        }), MgrSchedule.class);
    }

    @Override
    public List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId) throws EarthException {

        List<TransactionManager> transactionManagers = new ArrayList<>();
        try {
            transactionManagers.add(new TransactionManager(Constant.EARTH_WORKSPACE_ID));
            Map<String, String> mapTasks = taskDao.getAllTasks();
            transactionManagers.add(new TransactionManager(workspaceId));
            List<MgrSchedule> mgrSchedules = scheduleDao.getSchedulesByWorkspaceId(workspaceId, mapTasks);

            if (transactionManagers.size() > 0) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    transactionManagers.get(i).getManager().commit(transactionManagers.get(i).getTxStatus());
                }
            }
            return mgrSchedules;

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            if (transactionManagers.size() > 0) {
                for (int i = transactionManagers.size() - 1; i >= 0; i--) {
                    transactionManagers.get(i).getManager().rollback(transactionManagers.get(i).getTxStatus());
                }
            }
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteList(String workspaceId, List<String> scheduleIds) throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        return (boolean) executeTransaction(workspaceId, () -> {
            List<Map<Path<?>, Object>> conditions = new ArrayList<>();
            for (String scheduleId : scheduleIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrSchedule.scheduleId, scheduleId);
                conditions.add(condition);
            }
            return scheduleDao.deleteList(workspaceId, conditions) == scheduleIds.size();
        });
    }
}
