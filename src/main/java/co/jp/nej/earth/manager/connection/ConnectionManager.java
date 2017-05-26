package co.jp.nej.earth.manager.connection;

import co.jp.nej.earth.config.*;
import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.*;
import co.jp.nej.earth.model.constant.Constant.*;
import co.jp.nej.earth.service.*;
import co.jp.nej.earth.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.*;
import org.springframework.util.*;

import javax.annotation.*;
import javax.inject.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;

@Component
public class ConnectionManager {

    private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    private static Map<String, PlatformTransactionManager> mgrTransactions
                                                                = new HashMap<String, PlatformTransactionManager>();

    private static JdbcConfig config;
    private static WorkspaceService wkService;
    private static EMessageResource messges;

    @Autowired
    private JdbcConfig jdbcConfig;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private EMessageResource messageSource;

    @PostConstruct
    public void initConnectionManager() {
        config = jdbcConfig;
        wkService = workspaceService;
        messges = messageSource;
    }

    public static void addDataSource(String id, DataSource dataSource) {
        dataSources.put(id, dataSource);
        mgrTransactions.put(id, new DataSourceTransactionManager(dataSource));
    }

    public static DataSource getDataSource(String id) {
        return dataSources.get(id);
    }

    public static void remove(String id) {
        if (exists(id)) {
            dataSources.remove(id);
            mgrTransactions.remove(id);
        }
    }

    public static EarthQueryFactory getEarthQueryFactory() throws EarthException {
        return getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
    }

    public static EarthQueryFactory getEarthQueryFactory(String id) throws EarthException {

        if (StringUtils.isEmpty(id)) {
            throw new EarthException(messges.get(ErrorCode.E0001, new String[] { ScreenItem.WORKSPACE_ID }));
        }

        if (!exists(id)) {
            // Get DataSource from Database.
            getDataSourceFromDb(id);
        }

        EarthQueryFactory earthQueryFactory = createEarthQueryFactory(dataSources.get(id));
        return earthQueryFactory;
    }

    public static boolean exists(String id) {
        return (dataSources.containsKey(id));
    }

    public static EarthQueryFactory createEarthQueryFactory(DataSource dataSource) {
        Provider<Connection> sysProvider = new SpringConnectionProvider(dataSource);
        return new EarthQueryFactory(config.querydslConfiguration(), sysProvider);
    }

    public static PlatformTransactionManager getTransactionManager(String id) throws EarthException {
        if (!exists(id)) {
            // Get DataSource from Database.
            getDataSourceFromDb(id);
        }

        return mgrTransactions.get(id);
    }

    private static void getDataSourceFromDb(String id) throws EarthException {
        MgrWorkspaceConnect connection = wkService.getMgrConnectionByWorkspaceId(id);
        if (connection == null) {
            throw new EarthException(
                    messges.get("connection.notfound", new String[] { ScreenItem.WORKSPACE_ID }));
        } else {
            DataSource dataSource = config.dataSource(connection.getWorkspaceId(), connection);
            addDataSource(id, dataSource);
        }
    }
}
