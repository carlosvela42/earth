package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Path;

import co.jp.nej.earth.dao.ProcessDao;
import co.jp.nej.earth.dao.ProcessServiceDao;
import co.jp.nej.earth.dao.ScheduleDao;
import co.jp.nej.earth.dao.TaskDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EScheduleId;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.MgrProcessService;
import co.jp.nej.earth.model.sql.QMgrProcessService;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;

@Service
public class ScheduleServiceImpl extends BaseService implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private ProcessServiceDao processServiceDao;

    @Autowired
    private EMessageResource eMessageResource;

    @Autowired
    private EScheduleId eScheduleId;

    private static final QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
    private static final QMgrProcessService qMgrProcessService = QMgrProcessService.newInstance();

    @Override
    public List<MgrSchedule> getSchedules(String workspaceId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(workspaceId);
        List<MgrSchedule> mgrSchedule = null;
        try {
            mgrSchedule = scheduleDao.getSchedules(workspaceId);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            throw new EarthException(ex);
        }
        return mgrSchedule;
    }

    @Override
    public List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId) throws EarthException {
        List<TransactionManager> transactionManagers = new ArrayList<>();
        try {
            transactionManagers.add(new TransactionManager(workspaceId));
            Map<String, String> mapTasks = taskDao.getAllTasks(workspaceId);
            transactionManagers.add(new TransactionManager(workspaceId));
            List<MgrSchedule> mgrSchedules = scheduleDao.getSchedulesByWorkspaceId(workspaceId, mapTasks);

            processCommitTransactions(transactionManagers);
            return mgrSchedules;

        } catch (Exception ex) {
            processRollBackTransactions(transactionManagers);
            throw new EarthException(ex);
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

    @Override
    public boolean insertOne(String workspaceId, MgrSchedule mgrSchedule) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return scheduleDao.add(workspaceId, mgrSchedule) > 0L;
        });
    }

    @Override
    public boolean updateOne(String workspaceId, MgrSchedule mgrSchedule) throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        return (boolean) executeTransaction(workspaceId, () -> {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrSchedule.scheduleId, mgrSchedule.getScheduleId());
            Map<Path<?>, Object> valueMap = new HashMap<>();
            valueMap.put(qMgrSchedule.hostName, mgrSchedule.getHostName());
            valueMap.put(qMgrSchedule.processIServiceId, mgrSchedule.getProcessIServiceId());
            valueMap.put(qMgrSchedule.processId, mgrSchedule.getProcessId());
            valueMap.put(qMgrSchedule.taskId, mgrSchedule.getTaskId());
            valueMap.put(qMgrSchedule.enableDisable, mgrSchedule.getEnableDisable());
            valueMap.put(qMgrSchedule.startTime, mgrSchedule.getStartTime());
            valueMap.put(qMgrSchedule.endTime, mgrSchedule.getEndTime());
            valueMap.put(qMgrSchedule.runIntervalDay, mgrSchedule.getRunIntervalDay());
            valueMap.put(qMgrSchedule.runIntervalHour, mgrSchedule.getRunIntervalHour());
            valueMap.put(qMgrSchedule.runIntervalMinute, mgrSchedule.getRunIntervalMinute());
            valueMap.put(qMgrSchedule.runIntervalSecond, mgrSchedule.getRunIntervalSecond());

            return scheduleDao.update(workspaceId, condition, valueMap) > 0L;
        });
    }

    @Override
    public List<Message> validate(String workspaceId, MgrSchedule mgrSchedule, boolean insert) throws EarthException {
        List<Message> messages = new ArrayList<>();
        if (0 == mgrSchedule.getProcessId()) {
            messages.add(new Message(ErrorCode.E0001,
                    eMessageResource.get(ErrorCode.E0001, new String[] { "process.name" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getProcessId())
                && !EStringUtil.checkNumber(String.valueOf(mgrSchedule.getProcessId()))) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "process.name" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getProcessIServiceId())
                && !EStringUtil.checkNumber(mgrSchedule.getProcessIServiceId())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "process.service" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getTaskId()) && !EStringUtil.checkNumber(mgrSchedule.getTaskId())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "task.name" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getRunIntervalDay())
                && !EStringUtil.checkNumber(mgrSchedule.getRunIntervalDay())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.day" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getRunIntervalHour())
                && !EStringUtil.checkNumber(mgrSchedule.getRunIntervalHour())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.hour" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getRunIntervalMinute())
                && !EStringUtil.checkNumber(mgrSchedule.getRunIntervalMinute())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.minute" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getRunIntervalSecond())
                && !EStringUtil.checkNumber(mgrSchedule.getRunIntervalSecond())) {
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.second" })));
        }
        boolean isFormatSuccess = true;
        if (!EStringUtil.isEmpty(mgrSchedule.getStartTime()) && !DateUtil.isDate(mgrSchedule.getStartTime())) {
            isFormatSuccess = false;
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.startDateTime" })));
        }
        if (!EStringUtil.isEmpty(mgrSchedule.getEndTime()) && !DateUtil.isDate(mgrSchedule.getEndTime())) {
            isFormatSuccess = false;
            messages.add(new Message(ErrorCode.E0011,
                    eMessageResource.get(ErrorCode.E0011, new String[] { "schedule.endDateTime" })));
        }
        try {
            if (isFormatSuccess) {
                if (!EStringUtil.isEmpty(mgrSchedule.getStartTime()) && !EStringUtil.isEmpty(mgrSchedule.getEndTime())
                        && !DateUtil
                                .convertStringSimpleDateFormat(mgrSchedule.getStartTime(),
                                        Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)
                                .before(DateUtil.convertStringSimpleDateFormat(mgrSchedule.getEndTime(),
                                        Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS))) {
                    messages.add(new Message(ErrorCode.E0006, eMessageResource.get(ErrorCode.E0006,
                            new String[] { "schedule.startDateTime", "schedule.endDateTime" })));
                }
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        if (insert) {
            QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
            boolean isExist = (boolean) executeTransaction(workspaceId, () -> {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrSchedule.scheduleId, mgrSchedule.getScheduleId());
                return scheduleDao.findOne(workspaceId, condition) != null;
            });
            if (isExist) {
                Message message = new Message(Constant.MessageUser.USR_EXIST,
                        eMessageResource.get(Constant.ErrorCode.E0003,
                                new String[] { mgrSchedule.getScheduleId(), "schedule.id" }));
                messages.add(message);
                return messages;
            }
        }
        return messages;
    }

    @Override
    public Map<String, Object> getInfo(String workspaceId) throws EarthException {
        List<TransactionManager> transactionManagers = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        try {
            transactionManagers.add(new TransactionManager(workspaceId));
            maps.put("mgrTasks", taskDao.findAll(workspaceId));
            transactionManagers.add(new TransactionManager(workspaceId));
            maps.put("mgrProcesses", processDao.findAll(workspaceId));
            maps.put("mgrProcessServices", processServiceDao.findAll(Constant.EARTH_WORKSPACE_ID));
            maps.put("scheduleId", eScheduleId.getAutoId(workspaceId));
            processCommitTransactions(transactionManagers);
            return maps;
        } catch (Exception ex) {
            processRollBackTransactions(transactionManagers);
            throw new EarthException(ex);
        }

    }

    @Override
    public Map<String, Object> showDetail(String workspaceId, String scheduleId) throws EarthException {
        List<TransactionManager> transactionManagers = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        try {
            maps = getInfo(workspaceId);
            transactionManagers.add(new TransactionManager(workspaceId));
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrSchedule.scheduleId, scheduleId);
            MgrSchedule mgrSchedule = scheduleDao.findOne(workspaceId, condition);
            maps.put("mgrSchedule", mgrSchedule);
            processCommitTransactions(transactionManagers);
            return maps;
        } catch (Exception ex) {
            processRollBackTransactions(transactionManagers);
            throw new EarthException(ex);
        }
    }

    @Override
    public List<MgrSchedule> getScheduleByProcessServiceId(int processServiceId) throws EarthException {
        int workspaceId = getWorkspaceByProcessServiceId(processServiceId);
        List<MgrSchedule> scheduleList = null;
        if(workspaceId>0) {
            String workspaceIdInString = Integer.toString(workspaceId);
            TransactionManager transactionManager = new TransactionManager(workspaceIdInString);

            try {
                scheduleList = scheduleDao.getSchedulesByProcessServiceId(workspaceIdInString,processServiceId);

                transactionManager.getManager().commit(transactionManager.getTxStatus());
            } catch (Exception ex) {
                transactionManager.getManager().rollback(transactionManager.getTxStatus());
                throw new EarthException(ex);
            }
        }
        return scheduleList;
    }


    @Override
    public int getWorkspaceByProcessServiceId(int processServiceId) throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        int workspaceId = 0;
        try {

            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrProcessService.processIServiceId, processServiceId);
            MgrProcessService mgrProcessService = processServiceDao.findOne(condition);
            workspaceId = mgrProcessService.getWorkspaceId();
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            throw new EarthException(ex);
        }
        return workspaceId;
    }

    /*@Override
    public List<MgrProcessService> getWorkspaceForProcessService(String workspaceId, int processServiceID)
            throws EarthException {
        TransactionManager transactionManager = new TransactionManager(workspaceId);
        List<MgrProcessService> mgrProcessService = null;
        try {
            mgrProcessService = scheduleDao.getWorkspaceForProcessService(workspaceId, processServiceID);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            throw new EarthException(ex);
        }
        return mgrProcessService;
    }*/

    @Override
    public boolean updateNextRunDateByScheduleId(String workspaceId, String scheduleId,
        String nextTime) throws EarthException {

        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        return (boolean) executeTransaction(workspaceId, () -> {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrSchedule.scheduleId,scheduleId);
            Map<Path<?>, Object> valueMap = new HashMap<>();
            valueMap.put(qMgrSchedule.nextRunDate, nextTime);

            return scheduleDao.update(workspaceId, condition, valueMap) > 0L;
        });
    }

    @Override
    public boolean updateNextRunDateByID(String workspaceId, MgrSchedule mgrSchedule) throws EarthException {
        QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
        return (boolean) executeTransaction(workspaceId, () -> {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qMgrSchedule.scheduleId, mgrSchedule.getScheduleId());
            Map<Path<?>, Object> valueMap = new HashMap<>();
            valueMap.put(qMgrSchedule.nextRunDate, mgrSchedule.getNextRunDate());
            valueMap.put(qMgrSchedule.enableDisable, mgrSchedule.getEnableDisable());

            return scheduleDao.update(workspaceId, condition, valueMap) > 0L;
        });
    }
}
