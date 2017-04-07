package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QCtlEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventDaoImpl implements EventDao {
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlEvent qCtlEvent=QCtlEvent.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qCtlEvent).where(qCtlEvent.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
