/**
 * 
 */
package co.jp.nej.earth.config;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLServer2012Templates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.util.EStringUtil;

/**
 * @author p-tvo-khanhnv
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class JdbcConfig {
    private final String ORACLE_URL_FORMAT = "jdbc:oracle:thin:@{server}:{port}:{schema}";
    private final String SQL_SERVER_URL_FORMAT = "jdbc:sqlserver://{server}:{port};databaseName={schema};integratedSecurity=false;";

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.maximum-pool-size}")
    private String maxPoolSize;

    public DataSource dataSource(MgrWorkspaceConnect mgrConnect) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        if (mgrConnect == null) {
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
        } else {
            config.setJdbcUrl(createDbUrl(mgrConnect));
            config.setUsername(mgrConnect.getDbUser());
            config.setPassword(mgrConnect.getDbPassword());
        }
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        return new HikariDataSource(config);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getPlatformTransactionManager() {
        return new DataSourceTransactionManager(dataSource(null));
    }

    private String createDbUrl(MgrWorkspaceConnect mgrConnect) {
        DatabaseType dbType = databaseType();
        String dbUrl = EStringUtil.EMPTY;
        if (DatabaseType.isOracle(dbType)) {
            dbUrl = url.replace(username, mgrConnect.getSchemaName()).replace("##", "");
        } else {
            dbUrl = url.split(";")[0] + "DatabaseName=" + mgrConnect.getSchemaName() + ";integratedSecurity=false;";
        }

        return dbUrl;
    }
    
//    private String createDbUrl(MgrWorkspaceConnect mgrConnect) {
//        DatabaseType dbType = databaseType();
//        String dbUrl = EStringUtil.EMPTY;
//        if (DatabaseType.isOracle(dbType)) {
//            dbUrl = ORACLE_URL_FORMAT;
//        } else {
//            dbUrl = SQL_SERVER_URL_FORMAT;
//        }
//
//        dbUrl = dbUrl.replace("{server}", mgrConnect.getDbServer())
//                .replace("{port}", String.valueOf(mgrConnect.getPort()))
//                .replace("{schema}", mgrConnect.getSchemaName())
//                .replace("c##", EStringUtil.EMPTY);
//
//        return dbUrl;
//    }

    @Bean
    public DatabaseType databaseType() {
        if (driver.equals(DatabaseType.ORACLE.toString())) {
            return DatabaseType.ORACLE;
        }
        return DatabaseType.SQL_SERVER;
    }

    public com.querydsl.sql.Configuration querydslConfiguration() {
        DatabaseType dbType = databaseType();
        SQLTemplates templates = null;
        if (DatabaseType.isOracle(dbType)) {
            templates = OracleTemplates.builder().build();
        } else {
            templates = SQLServer2012Templates.builder().build();
        }

        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());
        return configuration;
    }

    @PostConstruct
    public void createSystemFactory() throws EarthException {
        // Create System Factory.
        ConnectionManager.addQueryFactory(Constant.EARTH_WORKSPACE_ID, createEarthQueryFactory(dataSource(null)));
    }

    public EarthQueryFactory createEarthQueryFactory(DataSource dataSource) {
        Provider<Connection> sysProvider = new SpringConnectionProvider(dataSource);
        return new EarthQueryFactory(querydslConfiguration(), sysProvider);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
