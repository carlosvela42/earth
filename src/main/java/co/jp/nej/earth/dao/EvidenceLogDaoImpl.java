package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.sql.QStrLogAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by minhtv on 3/29/2017.
 */
@Repository
public class EvidenceLogDaoImpl implements EvidenceLogDao {

    @Autowired
    private WorkspaceDao workspaceDao;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
                earthQueryFactory.delete(qStrLogAccess).where(qStrLogAccess.userId.in(userIds)).execute();
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException {
        try {
            QStrLogAccess qStrLogAccess = QStrLogAccess.newInstance();
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
//                earthQueryFactory.delete(qStrLogAccess).where(qStrLogAccess.processId.in(profileIds)).execute();
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}