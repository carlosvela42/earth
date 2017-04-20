package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.ScheduleDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    public List<MgrSchedule> getSchedules() throws EarthException {

        return scheduleDao.getSchedules();
    }

}
