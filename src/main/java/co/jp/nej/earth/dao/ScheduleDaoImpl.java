package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QMgrProcess;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.model.sql.QMgrTask;

@Repository
public class ScheduleDaoImpl extends BaseDaoImpl<MgrSchedule> implements ScheduleDao {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleDaoImpl.class);

    public ScheduleDaoImpl() throws Exception {
        super();
    }

    @Override
    public List<MgrSchedule> getSchedules(String workspaceId) throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        QMgrTask qTask = QMgrTask.newInstance();

        try {
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qTask.className, qMgrSchedule.startTime).from(qMgrSchedule).innerJoin(qTask)
                    .on(qMgrSchedule.taskId.eq(qTask.taskId)).getResults();
            List<MgrSchedule> listSchedule = new ArrayList<>();
            while (resultSet.next()) {
                MgrSchedule mgrSchedule = new MgrSchedule();
                mgrSchedule.setStartTime(resultSet.getString(ColumnNames.START_TIME.toString()));
                mgrSchedule.setTaskId(resultSet.getString(ColumnNames.CLASS_NAME.toString()));
                listSchedule.add(mgrSchedule);
            }
            return listSchedule;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EarthException(e);
        }
    }

    @Override
    public List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId, Map<String, String> tasks)
            throws EarthException {
        try {
            QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
            QMgrProcess qMgrProcess = QMgrProcess.newInstance();
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qMgrSchedule.scheduleId, qMgrSchedule.processId, qMgrSchedule.taskId,
                            qMgrSchedule.processIServiceId, qMgrSchedule.hostName, qMgrSchedule.startTime,
                            qMgrSchedule.endTime, qMgrSchedule.enableDisable ,qMgrProcess.processName)
                    .from(qMgrSchedule).innerJoin(qMgrProcess).on(qMgrSchedule.processId.eq(qMgrProcess.processId))
                    .getResults();
            List<MgrSchedule> mgrSchedules = new ArrayList<>();
            while (resultSet.next()) {
                MgrSchedule mgrSchedule = new MgrSchedule(resultSet.getString(ColumnNames.SCHEDULE_ID.toString()),
                        Integer.parseInt(resultSet.getString(ColumnNames.PROCESS_ID.toString())),
                        resultSet.getString(ColumnNames.PROCESS_NAME.toString()),
                        resultSet.getString(ColumnNames.TASK_ID.toString()),
                        tasks.get(resultSet.getString(ColumnNames.TASK_ID.toString())),
                        resultSet.getString(ColumnNames.HOST_NAME.toString()),
                        resultSet.getString(ColumnNames.PROCESS_ISERVICEID.toString()),
                        resultSet.getString(ColumnNames.START_TIME.toString()),
                        resultSet.getString(ColumnNames.END_TIME.toString()),
                        resultSet.getString(ColumnNames.ENABLE_DISABLE.toString())
                        );
                mgrSchedules.add(mgrSchedule);
            }
            return mgrSchedules;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }
}
