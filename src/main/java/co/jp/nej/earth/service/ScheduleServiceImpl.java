package co.jp.nej.earth.service;

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
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EMessageResource;
import com.querydsl.core.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private ProcessServiceDao processServiceDao;

    @Autowired
    private EMessageResource eMessageResource;

    @Autowired
    private EScheduleId eScheduleId;

    @Override
    public List<MgrSchedule> getSchedules(String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
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
        if (insert) {
            QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
            boolean isExist = (boolean) executeTransaction(workspaceId, () -> {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qMgrSchedule.scheduleId, mgrSchedule.getScheduleId());
                return scheduleDao.findOne(workspaceId, condition) != null;
            });
            if (isExist) {
                Message message = new Message(Constant.MessageUser.USR_EXIST, eMessageResource.get(
                        Constant.ErrorCode.E0005, new String[]{mgrSchedule.getScheduleId(),
                                Constant.ScreenItem.SCHEDULE_ID}));
                messages.add(message);
                return messages;
            }
        }

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtil.convertStringSimpleDateFormat(mgrSchedule.getStartTime(),
                    Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            endTime = DateUtil.convertStringSimpleDateFormat(mgrSchedule.getEndTime(),
                    Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

        if (startTime != null && endTime != null) {
            if (!startTime.before(endTime)) {
                Message message = new Message(Constant.MessageUser.USR_EXIST, eMessageResource.get(
                        Constant.ErrorCode.E0005, new String[]{mgrSchedule.getScheduleId(),
                                Constant.ScreenItem.SCHEDULE_ID}));
                messages.add(message);
                return messages;
            }
        } else {
            Message message = new Message(Constant.MessageUser.USR_EXIST, eMessageResource.get(
                    Constant.ErrorCode.E0005, new String[]{mgrSchedule.getScheduleId(),
                            Constant.ScreenItem.SCHEDULE_ID}));
            messages.add(message);
            return messages;
        }
        return messages;
    }

    @Override
    public Map<String, Object> getInfo(String workspaceId) throws EarthException {
        List<TransactionManager> transactionManagers = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        try {
            transactionManagers.add(new TransactionManager(Constant.EARTH_WORKSPACE_ID));
            maps.put("mgrTasks", taskDao.findAll(Constant.EARTH_WORKSPACE_ID));
            transactionManagers.add(new TransactionManager(workspaceId));
            maps.put("mgrProcesses", processDao.findAll(workspaceId));
            maps.put("mgrProcessServices", processServiceDao.findAll(workspaceId));
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
}
