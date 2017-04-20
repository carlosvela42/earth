package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;

public interface ScheduleService {
    List<MgrSchedule> getSchedules() throws EarthException;
}
