package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.manager.connection.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.*;
import co.jp.nej.earth.util.*;
import com.querydsl.core.types.*;
import com.querydsl.sql.dml.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

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
    public List<Integer> getAllSiteIds() throws EarthException {
        // find all site and find list site id
        try {
            QSite qSite = QSite.newInstance();
            return ConnectionManager.getEarthQueryFactory().select(qSite.siteId).from(qSite)
                    .groupBy(qSite.siteId).fetch();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EarthException(e);
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
        QSite qSite = QSite.newInstance();
        try {
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
            SQLInsertClause insert = earthQueryFactory.insert(qSite);
            for (String directoryId : directoryIds) {
                insert.set(qSite.siteId, Integer.valueOf(siteId))
                        .set(qSite.dataDirectoryId, Integer.valueOf(directoryId))
                        .set(qSite.lastUpdateTime, DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD))
                        .addBatch();
            }
            return insert.execute();
        } catch (Exception ex) {
            throw new EarthException(ex);
        }
    }

    @Override
    public long deleteSite(String siteId, String workspaceId) throws EarthException {
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();
        QSite qSite = QSite.newInstance();
        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qSite.siteId, Integer.valueOf(siteId));
        conditions.add(condition);
        return deleteList(workspaceId, conditions);
    }
}
