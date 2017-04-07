package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QMgrSchedule;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

	public List<MgrSchedule> getSchedules() throws EarthException {
		QMgrSchedule qSchedule = QMgrSchedule.newInstance();

		QBean<MgrSchedule> selectList = Projections.bean(MgrSchedule.class, qSchedule.all());

		List<MgrSchedule> listSchedule = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
				.select(selectList).from(qSchedule).fetch();
		return listSchedule;
	}

}
