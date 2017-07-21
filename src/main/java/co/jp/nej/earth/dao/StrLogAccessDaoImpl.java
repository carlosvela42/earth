package co.jp.nej.earth.dao;


import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StrLogAccessDaoImpl extends BaseDaoImpl<StrLogAccess> implements StrLogAccessDao {

    @Autowired
    private WorkspaceDao workspaceDao;

    private static final QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();

    public StrLogAccessDaoImpl() throws Exception {
        super();
    }

    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException {
        try {
            List<StrLogAccess> strLogAccesses = new ArrayList<StrLogAccess>();
            QBean<StrLogAccess> selectList = Projections.bean(StrLogAccess.class, qStrLogAccess.all());
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            strLogAccesses = earthQueryFactory.select(selectList).from(qStrLogAccess).fetch();
            return strLogAccesses;
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                List<Map<Path<?>, Object>> conditions = new ArrayList<>();
                for (String userId : userIds) {
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qStrLogAccess.userId, userId);
                    conditions.add(condition);
                }
                this.deleteList(mgrWorkspace.getWorkspaceId(), conditions);
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public Integer getMaxHistoryNo(String workspaceId, String workItemId) throws EarthException {
        try {
            QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
            Integer maxHistoryNo = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qStrLogAccess.historyNo.max())
                    .from(qStrLogAccess).where(qStrLogAccess.workitemId.eq(workItemId)).fetchOne();
            return maxHistoryNo==null?0:maxHistoryNo;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }


    @Override
    public String getMaxId(String workspaceId) throws EarthException {
        try {
            QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
            return ConnectionManager.getEarthQueryFactory(workspaceId)
                .select(qStrLogAccess.eventId.max())
                .from(qStrLogAccess).fetchOne() ;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
