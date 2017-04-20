package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;

public interface ScheduleDao {
    List<MgrSchedule> getSchedules() throws EarthException;
}
