package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.model.sql.QMgrTask;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    public List<MgrSchedule> getSchedules() throws EarthException {
        QMgrSchedule qSchedule = QMgrSchedule.newInstance();
        QMgrTask qTask = QMgrTask.newInstance();
        ResultSet resultSet = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                .select(qTask.className, qSchedule.startTime).from(qSchedule).innerJoin(qTask)
                .on(qSchedule.taskId.eq(qTask.taskId)).getResults();
        List<MgrSchedule> listSchedule = new ArrayList<>();
        try {
            while (resultSet.next()) {
                MgrSchedule mgrSchedule = new MgrSchedule();
                mgrSchedule.setStartTime(resultSet.getString(ColumnNames.START_TIME.toString()));
                mgrSchedule.setTaskId(resultSet.getString(ColumnNames.CLASS_NAME.toString()));
                listSchedule.add(mgrSchedule);
            }
        } catch (SQLException e) {
            throw new EarthException(e.getMessage());
        }
        return listSchedule;
    }

}
