package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;

import java.util.List;

public interface ScheduleService {
    List<MgrSchedule> getSchedules(String workspaceId) throws EarthException;

    List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId) throws EarthException;

    boolean deleteList(String workspaceId, List<String> scheduleIds) throws EarthException;

}
