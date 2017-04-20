package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.sql.QCtlEvent;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class EventDaoImpl extends BaseDaoImpl<CtlEvent> implements EventDao {

    public EventDaoImpl() throws Exception {
        super();
    }

    private static final QCtlEvent qCtlEvent = QCtlEvent.newInstance();
    @Autowired
    private WorkspaceDao workspaceDao;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceDao.getAll();
            for (MgrWorkspace mgrWorkspace : mgrWorkspaces) {
                List<Map<Path<?>, Object>> conditions = new ArrayList<>();
                for(String userId: userIds){
                    Map<Path<?>, Object> condition = new HashMap<>();
                    condition.put(qCtlEvent.userId, userId);
                    conditions.add(condition);
                }
                this.deleteList(mgrWorkspace.getWorkspaceId(), conditions);
            }
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    @Override
    public List<String> getListCtlEventIdByStatus(String status, String workSpaceId) throws EarthException {
        QBean<CtlEvent> selectList = Projections.bean(CtlEvent.class, qCtlEvent.all());
        List<CtlEvent> ctlEvents = ConnectionManager.getEarthQueryFactory(workSpaceId).select(selectList)
                .from(qCtlEvent).where(qCtlEvent.status.eq(status)).fetch();
        return ctlEvents.stream().map(event -> event.getEventId()).collect(Collectors.toList());
    }

    @Override
    public boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException {
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workSpaceId);
        earthQueryFactory.update(qCtlEvent).set(qCtlEvent.status, AgentBatch.STATUS_EDITTING)
                .where(qCtlEvent.eventId.in(eventIds)).execute();
        return true;
    }
}