/**
 * 
 */
package co.jp.nej.earth.config;

import java.sql.Connection;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * @author p-dcv-khanhnv
 *
 */
public class SpringConnectionProvider implements Provider<Connection> {

    private final DataSource dataSource;

    public SpringConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection get() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (!DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
            throw new IllegalStateException("Connection is not transactional");
        }
        return connection;
    }

}
