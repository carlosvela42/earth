package co.jp.nej.earth.dao;


import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StrLogAccessDaoImpl implements StrLogAccessDao {
    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException {
        try {
            List<StrLogAccess> strLogAccesses = new ArrayList<StrLogAccess>();
            QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
            QBean<StrLogAccess> selectList = Projections.bean(StrLogAccess.class, qStrLogAccess.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            strLogAccesses = earthQueryFactory.select(selectList).from(qStrLogAccess).fetch();
            return strLogAccesses;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
