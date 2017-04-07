package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by minhtv on 3/29/2017.
 */
@Repository
public class EvidenceLogDaoImpl implements EvidenceLogDao {
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QStrLogAccess qStrLogAccess=QStrLogAccess.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qStrLogAccess).where(qStrLogAccess.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
