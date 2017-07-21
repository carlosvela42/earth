package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
//import co.jp.nej.earth.model.entity.MgrProcessService;

import java.util.List;
import java.util.Map;

public interface ScheduleDao extends BaseDao<MgrSchedule> {
    List<MgrSchedule> getSchedules(String workspaceId) throws EarthException;

    List<MgrSchedule> getSchedulesByProcessServiceId(String workspaceId, int processServiceId) throws EarthException;

    List<MgrSchedule> getSchedulesByWorkspaceId(String workspaceId, Map<String, String> tasks) throws EarthException;

    //List<MgrProcessService> getWorkspaceForProcessService(String workspaceId, int processServiceID)
    //        throws EarthException;

}
