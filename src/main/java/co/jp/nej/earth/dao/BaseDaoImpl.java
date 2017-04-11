package co.jp.nej.earth.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.util.EModelUtil;

public abstract class BaseDaoImpl<T extends BaseModel<T>> implements BaseDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDaoImpl.class);

    private T instance;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() throws Exception {

        this.instance = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]).newInstance();
    }


    /**
     * Find all entity record
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll(String workspaceId, Long offset, Long limit, OrderSpecifier<String> orderByColumn)
            throws EarthException {

        if (offset == null || offset < 0)
            offset = 0L;

        if (limit == null || limit < 0)
            limit = 100L;

        LOG.debug("Call {}.findAll with workspace: {}", instance.getClass().getSimpleName(), workspaceId);

        RelationalPathBase<T> qObj = this.instance.getqObj();

        EarthQueryFactory queryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);

        @SuppressWarnings("rawtypes")
        QBean<T> selectList = (QBean) Projections.bean(instance.getClass(), qObj.all());

        if (orderByColumn != null) {
            LOG.debug(EModelUtil.cleanQuery(queryFactory.select(selectList).from(qObj).offset(offset).limit(limit)
                    .orderBy(orderByColumn).getSQL()));
            
            return queryFactory.select(selectList).from(qObj).offset(offset).limit(limit).orderBy(orderByColumn)
                    .fetch();
        }

        else
            return queryFactory.select(selectList).from(qObj).offset(offset).limit(limit).fetch();

    }

    @SuppressWarnings("unchecked")
    public T findOne(String workspaceId, Map<Path<?>, Object> keyMap) throws EarthException {

        LOG.info("Call {}.findAll with workspace: {}", instance.getClass().getSimpleName(), workspaceId);

        EarthQueryFactory queryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
        RelationalPathBase<T> qObj = this.instance.getqObj();

        @SuppressWarnings("rawtypes")
        QBean<T> selectList = (QBean) Projections.bean(instance.getClass(), qObj.all());

        Predicate predicate = EModelUtil.buildCondition(keyMap, qObj, false);

        // LOG.info(EModelUtil.cleanQuery(queryFactory.select(selectList).from(qObj).where(predicate).getSQL()));

        T result = queryFactory.select(selectList).from(qObj).where(predicate).fetchOne();

        return result;
    }

    public boolean delete(String workspaceId, Map<Path<?>, Object> keyMap) throws EarthException {

        LOG.info("Call {}.findAll with workspace: {}", instance.getClass().getSimpleName(), workspaceId);

        EarthQueryFactory queryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);

        RelationalPathBase<T> qObj = this.instance.getqObj();

        Predicate predicate = EModelUtil.buildCondition(keyMap, qObj, true);

        return queryFactory.delete(qObj).where(predicate).execute() > 0;
    }

    @Override
    public boolean deleteList(String workspaceId, List<Map<Path<?>, Object>> keyMaps) throws EarthException {

        // if nothing to delete then consider as delete success
        if (keyMaps == null || keyMaps.size() == 0)
            return true;

        boolean deleteSuccess = true;
        for (Map<Path<?>, Object> keyMap : keyMaps) {
            deleteSuccess = delete(workspaceId, keyMap);

            // in case one can not be delete then return false
            if (!deleteSuccess)
                throw new EarthException("Can not delete item with " + keyMap.toString());
        }

        return true;
    }

    @Override
    public boolean add(String workspaceId, T t) throws EarthException {

        RelationalPathBase<T> qObj = this.instance.getqObj();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
        long inserted = earthQueryFactory.insert(qObj).populate(t).execute();
        return inserted > 0 ? true : false;
    }

    @Override
    public boolean update(String workspaceId, Map<Path<?>, Object> keyMap, Map<Path<?>, Object> updateMap)
            throws EarthException {

        RelationalPathBase<T> qObj = this.instance.getqObj();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);

        Predicate predicate = EModelUtil.buildCondition(keyMap, qObj, true);
        List<Path<?>> paths = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        Iterator<Entry<Path<?>, Object>> it = updateMap.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry<Path<?>, Object> pair = (Map.Entry<Path<?>, Object>) it.next();
            paths.add(pair.getKey());
            values.add(pair.getValue());
        }
        long result = earthQueryFactory.update(qObj).set(paths, values).where(predicate).execute();

        return result > 0;
    }
}
