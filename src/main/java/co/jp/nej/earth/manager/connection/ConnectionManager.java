package co.jp.nej.earth.manager.connection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.config.SpringConnectionProvider;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.service.WorkspaceService;

@Component
public class ConnectionManager {

    private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    private static Map<String, PlatformTransactionManager> mgrTransactions
                                    = new HashMap<String, PlatformTransactionManager>();

    private static JdbcConfig config;
    private static WorkspaceService wkService;
    private static MessageSource messges;

    @Autowired
    private JdbcConfig jdbcConfig;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private MessageSource messageSource;

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

    public static PlatformTransactionManager getTransactionManager(String id) throws EarthException {
        if (!exists(id)) {
            // Get DataSource from Database.
            getDataSourceFromDb(id);
        }

        return mgrTransactions.get(id);
    }

    public static EarthQueryFactory getEarthQueryFactory(String id) throws EarthException {

        if (StringUtils.isEmpty(id)) {
            throw new EarthException(messges.getMessage("E0001", new String[] { "workspaceId" }, Locale.ENGLISH));
        }

        if (!exists(id)) {
            // Get DataSource from Database.
            getDataSourceFromDb(id);
        }

        EarthQueryFactory earthQueryFactory = createEarthQueryFactory(dataSources.get(id));
        return earthQueryFactory;
    }

    private static void getDataSourceFromDb(String id) throws EarthException {
        MgrWorkspaceConnect connection = wkService.getMgrConnectionByWorkspaceId(id);
        if (connection == null) {
            throw new EarthException(
                    messges.getMessage("connection.notfound", new String[] { "workspaceId" }, Locale.ENGLISH));
        } else {
            DataSource dataSource = config.dataSource(connection.getWorkspaceId(), connection);
            addDataSource(id, dataSource);
        }
    }

    public static boolean exists(String id) {
        return (dataSources.containsKey(id));
    }

    public static EarthQueryFactory createEarthQueryFactory(DataSource dataSource) {
        Provider<Connection> sysProvider = new SpringConnectionProvider(dataSource);
        return new EarthQueryFactory(config.querydslConfiguration(), sysProvider);
    }

    public static void remove(String id) {
        if (exists(id)) {
            dataSources.remove(id);
            mgrTransactions.remove(id);
        }
    }
}
