package co.jp.nej.earth.manager.connection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.service.WorkspaceService;

@Component
public class ConnectionManager {

    private static Map<String, EarthQueryFactory> earthQueryFactories = new HashMap<String, EarthQueryFactory>();
    
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
    
    public static void addQueryFactory(String factoryId, EarthQueryFactory eqf) {
    	earthQueryFactories.put(factoryId, eqf);
    }
    
    public static EarthQueryFactory getEarthQueryFactory(String workspaceId) throws EarthException {
    	
    	if (StringUtils.isEmpty(workspaceId)) {
    		throw new EarthException(messges.getMessage("E0001", new String[] { "workspaceId" }, Locale.ENGLISH));
    	}
    	
    	if (earthQueryFactories.get(workspaceId) == null) {
    		// Get Connection from Db.
    		MgrWorkspaceConnect connection = wkService.getMgrConnectionByWorkspaceId(workspaceId);
    		if (connection == null) {
    			throw new EarthException(messges.getMessage("connection.notfound", new String[] { "workspaceId" }, Locale.ENGLISH));
    		} else {
    			EarthQueryFactory earthQueryFactory = config.createEarthQueryFactory(config.dataSource(connection));
    			addQueryFactory(workspaceId, earthQueryFactory);
    		}
    	}
    	
    	return earthQueryFactories.get(workspaceId);
    }
}
