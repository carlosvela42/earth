package co.jp.nej.earth.dao;

import java.sql.NClob;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.sql.QCtlEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventDaoImpl implements EventDao {

    @Autowired
    private WorkspaceDao workspaceDao;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlEvent qCtlEvent = QCtlEvent.newInstance();
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                EarthQueryFactory earthQueryFactory = ConnectionManager
                        .getEarthQueryFactory(mgrWorkspace.getWorkspaceId());
                earthQueryFactory.delete(qCtlEvent).where(qCtlEvent.userId.in(userIds)).execute();
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public long insertEvent(CtlEvent event) throws EarthException {
        try {
            QCtlEvent qCtlEvent = QCtlEvent.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            NClob workItemDataClob = earthQueryFactory.getConnection().createNClob();
            workItemDataClob.setString(1, event.getWorkitemData());
            return earthQueryFactory.insert(qCtlEvent).set(qCtlEvent.status, event.getStatus())
                    .set(qCtlEvent.workitemId, event.getWorkitemId()).set(qCtlEvent.userId, event.getUserId())
                    .set(qCtlEvent.taskId, event.getTaskId()).set(qCtlEvent.workitemData, workItemDataClob).execute();
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}