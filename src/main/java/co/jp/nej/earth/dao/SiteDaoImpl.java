package co.jp.nej.earth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.dml.SQLInsertClause;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QDirectory;
import co.jp.nej.earth.model.sql.QSite;
import co.jp.nej.earth.util.DateUtil;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Repository
public class SiteDaoImpl extends BaseDaoImpl<Site> implements SiteDao {

    private static final Logger LOG = LoggerFactory.getLogger(SiteDaoImpl.class);

    public SiteDaoImpl() throws Exception {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllSiteIds(String workspaceId) throws EarthException {
        // find all site and find list site id
        try {
            QSite qSite = QSite.newInstance();
            return ConnectionManager.getEarthQueryFactory(workspaceId)
                             .select(qSite.siteId)
                             .from(qSite)
                             .groupBy(qSite.siteId)
                             .fetch();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EarthException(e.getMessage());
        }
    }

    @Override
    public long deleteSites(List<Integer> siteIds, String workspaceId) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        QSite qSite = QSite.newInstance();
        for (Integer siteId : siteIds) {
            Map<Path<?>, Object> condition = new HashMap<>();
            condition.put(qSite.siteId, siteId);
            conditions.add(condition);
        }
        return deleteList(workspaceId, conditions);
    }

    @Override
    public long insertOne(String siteId, List<String> directoryIds, String workspaceId) throws EarthException {
        long inserted = 0L;
        QSite qSite = QSite.newInstance();
        try {
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            SQLInsertClause insert = earthQueryFactory.insert(qSite);
            for (String directoryId : directoryIds) {
                insert.set(qSite.siteId, Integer.valueOf(siteId))
                        .set(qSite.dataDirectoryId, Integer.valueOf(directoryId))
                        .set(qSite.lastUpdateTime, DateUtil.getCurrentDate(
                                Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD))
                        .addBatch();
            }
            inserted = insert.execute();
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
        return inserted;
    }

    @Override
    public List<Directory> getDirectorysBySiteId(String siteId, String workspaceId) throws EarthException {
        QSite qSite = QSite.newInstance();
        QDirectory qDirectory = QDirectory.newInstance();
        QBean<Directory> selectList = Projections.bean(Directory.class, qDirectory.all());
        List<Directory> directorys = new ArrayList<>();
        try{
            EarthQueryFactory queryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            directorys = queryFactory.select(selectList).
            from(qSite).innerJoin(qDirectory).on(qSite.dataDirectoryId.eq(qDirectory.dataDirectoryId))
            .where(qSite.siteId.eq(Integer.parseInt(siteId))).fetch();
        }catch(Exception ex){
            throw new EarthException(ex.getMessage());
        }
        return directorys;
    }
}
