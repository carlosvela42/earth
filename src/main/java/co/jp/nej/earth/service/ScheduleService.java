package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrSchedule;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    List<MgrSchedule> getSchedules(String workspaceId) throws EarthException;

    List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId) throws EarthException;

    boolean deleteList(String workspaceId, List<String> scheduleIds) throws EarthException;

    boolean insertOne(String workspaceId, MgrSchedule mgrSchedule) throws EarthException;

    boolean updateOne(String workspaceId, MgrSchedule mgrSchedule) throws EarthException;

    List<Message> validate(String workspaceId, MgrSchedule mgrSchedule, boolean insert) throws EarthException;

    Map<String, Object> getInfo(String workspaceId) throws EarthException;

    Map<String, Object> showDetail(String workspaceId, String scheduleId) throws EarthException;
}
