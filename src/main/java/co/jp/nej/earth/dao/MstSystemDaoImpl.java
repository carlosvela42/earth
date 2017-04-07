package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QMstSystem;
import co.jp.nej.earth.service.MstSystem;

@Repository
public class MstSystemDaoImpl implements MstSystemDao {

	public List<MstSystem> getMstSystem() throws EarthException {
		QMstSystem qMstSystem = QMstSystem.newInstance();

		QBean<MstSystem> selectList = Projections.bean(MstSystem.class, qMstSystem.all());

		List<MstSystem> listMstSystem = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
				.select(selectList).from(qMstSystem).fetch();
		return listMstSystem;
	}

}
