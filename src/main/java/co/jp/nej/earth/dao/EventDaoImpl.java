package co.jp.nej.earth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.util.ConversionUtil;


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
            throw new EarthException(ex);
        }
    }

    @Override
    public List<String> getListCtlEventIdByStatus(String status, String workSpaceId) throws EarthException {
        QBean<CtlEvent> selectList = Projections.bean(CtlEvent.class, qCtlEvent.all());
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workSpaceId);
        return ConversionUtil.castList(executeWithException(() -> {
            List<CtlEvent> ctlEvents = earthQueryFactory.select(selectList)
                    .from(qCtlEvent).where(qCtlEvent.status.eq(status)).fetch();
            return ctlEvents.stream().map(event -> event.getEventId()).collect(Collectors.toList());
        }), String.class);
    }

    @Override
    public boolean updateBulkEventStatus(List<String> eventIds, String workSpaceId) throws EarthException {
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workSpaceId);
        return (boolean) executeWithException(() -> {
            earthQueryFactory.update(qCtlEvent).set(qCtlEvent.status, AgentBatch.STATUS_EDITTING)
                    .where(qCtlEvent.eventId.in(eventIds)).execute();
            return true;
        });
    }

    @Override
    public long countEventByUserIds(List<String> userIds, String workspaceId) throws EarthException {
        BooleanBuilder condition = new BooleanBuilder();
        Predicate pre1 = qCtlEvent.userId.in(userIds);
        condition.and(pre1);

        return this.search(workspaceId,condition).size();
    }
}