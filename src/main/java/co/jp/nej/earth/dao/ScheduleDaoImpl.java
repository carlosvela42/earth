package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrCustomTask;
//import co.jp.nej.earth.model.entity.MgrProcessService;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QMgrCustomTask;
import co.jp.nej.earth.model.sql.QMgrProcess;
//import co.jp.nej.earth.model.sql.QMgrProcessService;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.model.sql.QMgrTask;
import co.jp.nej.earth.util.DateUtil;

@Repository
public class ScheduleDaoImpl extends BaseDaoImpl<MgrSchedule> implements ScheduleDao {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleDaoImpl.class);

    private static final String ENABLE = "enable";

    @Autowired
    private CustomTaskDao customTaskDao;

    public ScheduleDaoImpl() throws Exception {
        super();
    }


    @Override
    public List<MgrSchedule> getSchedulesByProcessServiceId(String workspaceId, int processServiceId)
        throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        QMgrTask qMgrTask = QMgrTask.newInstance();

        List<MgrSchedule> listSchedule = new ArrayList<>();
        Map<String,MgrCustomTask> customTaskMap = customTaskDao.getCustomTasksMap();
        if(customTaskMap.size() == 0) {
            return listSchedule;
        }
        try {

            BooleanBuilder condition = new BooleanBuilder();
            Predicate predicate = qMgrSchedule.processIServiceId.eq(Integer.toString(processServiceId));
            condition.and(predicate);

            String currentDate = DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);
            //Predicate predicateStartTime = qMgrSchedule.startTime.loe(currentDate);
            //condition.and(predicateStartTime);
//
            Predicate predicateEndTime = qMgrSchedule.endTime.goe(currentDate);
            condition.and(predicateEndTime);

            Predicate predicateEnabled = qMgrSchedule.enableDisable.eq(ENABLE);
            condition.and(predicateEnabled);

            Predicate predicateCustomTaskId = qMgrTask.customTaskId.isNotNull();
            condition.and(predicateCustomTaskId);

            /*LOG.info(ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qMgrSchedule.scheduleId, qTask.taskId, qMgrSchedule.startTime, qMgrSchedule.endTime,
                            qMgrSchedule.enableDisable, qMgrSchedule.nextRunDate, qMgrSchedule.runIntervalDay,
                            qMgrSchedule.runIntervalDay, qMgrSchedule.runIntervalHour, qMgrSchedule.runIntervalMinute,
                            qMgrSchedule.runIntervalSecond, qTask.customTaskId)
                    .from(qMgrSchedule).innerJoin(qTask).on(qMgrSchedule.taskId.eq(qTask.taskId))
                    .where(condition).getSQL().getSQL());*/

            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qMgrSchedule.scheduleId, qMgrTask.taskId, qMgrSchedule.startTime, qMgrSchedule.endTime,
                            qMgrSchedule.enableDisable, qMgrSchedule.nextRunDate, qMgrSchedule.runIntervalDay,
                            qMgrSchedule.runIntervalDay, qMgrSchedule.runIntervalHour, qMgrSchedule.runIntervalMinute,
                            qMgrSchedule.runIntervalSecond,qMgrSchedule.processIServiceId, qMgrTask.customTaskId)
                    .from(qMgrSchedule).innerJoin(qMgrTask).on(qMgrSchedule.taskId.eq(qMgrTask.taskId))
                    .where(condition).getResults();

            while (resultSet.next()) {
                MgrSchedule mgrSchedule = new MgrSchedule();
                mgrSchedule.setScheduleId(resultSet.getString(ColumnNames.SCHEDULE_ID.toString()));
                mgrSchedule.setProcessIServiceId(resultSet.getString(ColumnNames.PROCESS_ISERVICEID.toString()));
                mgrSchedule.setTaskId(resultSet.getString(ColumnNames.TASK_ID.toString()));
                mgrSchedule.setStartTime(resultSet.getString(ColumnNames.START_TIME.toString()));
                mgrSchedule.setEndTime(resultSet.getString(ColumnNames.END_TIME.toString()));
                mgrSchedule.setEnableDisable(resultSet.getString(ColumnNames.ENABLE_DISABLE.toString()));
                mgrSchedule.setNextRunDate(resultSet.getString(ColumnNames.NEXT_RUN_DATE.toString()));
                mgrSchedule.setRunIntervalDay(resultSet.getString(ColumnNames.RUN_INTERVAL_DAY.toString()));
                mgrSchedule.setRunIntervalHour(resultSet.getString(ColumnNames.RUN_INTERVAL_HOUR.toString()));
                mgrSchedule.setRunIntervalMinute(resultSet.getString(ColumnNames.RUN_INTERVAL_MINUTE.toString()));
                mgrSchedule.setRunIntervalSecond(resultSet.getString(ColumnNames.RUN_INTERVAL_SECOND.toString()));
                mgrSchedule.setClassName(
                    customTaskMap.get(resultSet.getString(ColumnNames.CUSTOM_TASK_ID.toString())).getClassName());

                listSchedule.add(mgrSchedule);

            }
            return listSchedule;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EarthException(e);
        }
    }

    @Override
    public List<MgrSchedule> getSchedules(String workspaceId) throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        QMgrTask qTask = QMgrTask.newInstance();
        QMgrCustomTask qCustomTask = QMgrCustomTask.newInstance();

        try {
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qMgrSchedule.scheduleId, qTask.taskId, qMgrSchedule.startTime, qMgrSchedule.endTime,
                            qMgrSchedule.enableDisable, qMgrSchedule.nextRunDate, qMgrSchedule.runIntervalDay,
                            qMgrSchedule.runIntervalDay, qMgrSchedule.runIntervalHour, qMgrSchedule.runIntervalMinute,
                            qMgrSchedule.runIntervalSecond, qCustomTask.className)
                    .from(qMgrSchedule).innerJoin(qTask).on(qMgrSchedule.taskId.eq(qTask.taskId)).innerJoin(qCustomTask)
                    .on(qTask.customTaskId.eq(qCustomTask.customTaskId)).getResults();
            List<MgrSchedule> listSchedule = new ArrayList<>();
            while (resultSet.next()) {
                MgrSchedule mgrSchedule = new MgrSchedule();
                mgrSchedule.setScheduleId(resultSet.getString(ColumnNames.SCHEDULE_ID.toString()));
                mgrSchedule.setTaskId(resultSet.getString(ColumnNames.TASK_ID.toString()));
                mgrSchedule.setStartTime(resultSet.getString(ColumnNames.START_TIME.toString()));
                mgrSchedule.setEndTime(resultSet.getString(ColumnNames.END_TIME.toString()));
                mgrSchedule.setEnableDisable(resultSet.getString(ColumnNames.ENABLE_DISABLE.toString()));
                mgrSchedule.setNextRunDate(resultSet.getString(ColumnNames.NEXT_RUN_DATE.toString()));
                mgrSchedule.setRunIntervalDay(resultSet.getString(ColumnNames.RUN_INTERVAL_DAY.toString()));
                mgrSchedule.setRunIntervalHour(resultSet.getString(ColumnNames.RUN_INTERVAL_HOUR.toString()));
                mgrSchedule.setRunIntervalMinute(resultSet.getString(ColumnNames.RUN_INTERVAL_MINUTE.toString()));
                mgrSchedule.setRunIntervalSecond(resultSet.getString(ColumnNames.RUN_INTERVAL_SECOND.toString()));
                mgrSchedule.setClassName(resultSet.getString(ColumnNames.CLASS_NAME.toString()));
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
                            qMgrSchedule.endTime, qMgrSchedule.enableDisable, qMgrProcess.processName)
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
                        resultSet.getString(ColumnNames.ENABLE_DISABLE.toString()));
                mgrSchedules.add(mgrSchedule);
            }
            return mgrSchedules;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }

    /*@Override
    public List<MgrProcessService> getWorkspaceForProcessService(String workspaceId, int processServiceID)
            throws EarthException {
        QMgrProcessService qMgrProcessService = QMgrProcessService.newInstance();
        try {
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qMgrProcessService.workspaceId).from(qMgrProcessService)
                    .where(qMgrProcessService.processIServiceId.eq(processServiceID)).getResults();
            List<MgrProcessService> listProcessService = new ArrayList<>();
            while (resultSet.next()) {
                MgrProcessService mgrProcessService = new MgrProcessService();
                mgrProcessService
                        .setWorkspaceId(Integer.parseInt(resultSet.getString(ColumnNames.WORKSPACE_ID.toString())));
                listProcessService.add(mgrProcessService);
            }
            return listProcessService;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EarthException(e);
        }
    }*/
}
