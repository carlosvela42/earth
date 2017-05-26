package co.jp.nej.earth.id;

import co.jp.nej.earth.dao.ScheduleDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EScheduleIdImpl implements EScheduleId {

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    public String getAutoId(String workspaceId) throws EarthException {
        List<MgrSchedule> mgrSchedules = scheduleDao.findAll(workspaceId);

        String scheduleId;
        if (mgrSchedules.size() > 0) {
            List<String> scheduleIds = new ArrayList<>();
            for (MgrSchedule mgrSchedule : mgrSchedules) {
                scheduleIds.add(mgrSchedule.getScheduleId());
            }
            scheduleId = String.valueOf(Integer.parseInt(Collections.max(scheduleIds)) + 1);
        } else {
            scheduleId = String.valueOf(1);
        }
        return scheduleId;
    }
}
