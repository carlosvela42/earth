package co.jp.nej.earth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import co.jp.nej.earth.config.AppConfig;
import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.DatabaseType;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class BaseTest {
    @Autowired
    private JdbcConfig config;

    public void resetSystemDataSource() {
        // Create System Query Factory.
        MgrWorkspaceConnect sysConnection = getSystemConnection();
        if (ConnectionManager.exists(Constant.EARTH_WORKSPACE_ID)) {
            ConnectionManager.remove(Constant.EARTH_WORKSPACE_ID);
        }

        ConnectionManager.addDataSource(Constant.EARTH_WORKSPACE_ID,
                config.dataSource(sysConnection.getWorkspaceId(), sysConnection));
    }

    public MgrWorkspaceConnect getSystemConnection() {

        Properties prop = new Properties();
        InputStream input = null;
        MgrWorkspaceConnect earthConn = new MgrWorkspaceConnect();
        try {

            input = getClass().getResourceAsStream("resource/config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            earthConn.setSchemaName(prop.getProperty("datasource.schema-name"));
            earthConn.setDbPassword(prop.getProperty("datasource.password"));
            earthConn.setDbServer(prop.getProperty("datasource.server"));
            earthConn.setDbUser(prop.getProperty("datasource.user"));
            earthConn.setPort(Integer.parseInt(prop.getProperty("datasource.port")));
            String strDbType = prop.getProperty("datasource.db-type");
            if ("ORACLE".equals(strDbType)) {
                earthConn.setDbType(DatabaseType.ORACLE);
            } else if ("SQLSERVER".equals(strDbType)) {
                earthConn.setDbType(DatabaseType.SQL_SERVER);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return earthConn;
    }

    @Test
    public void testResetSystemDataSource() {
        // resetSystemDataSource();
        Assert.assertTrue(ConnectionManager.exists(Constant.EARTH_WORKSPACE_ID));
    }
}
