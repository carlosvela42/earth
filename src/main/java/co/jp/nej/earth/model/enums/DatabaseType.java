package co.jp.nej.earth.model.enums;

public enum DatabaseType {
	ORACLE("oracle.jdbc.driver.OracleDriver")
    ,SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	
	private String driver;
	
	
	private DatabaseType(String driver) {
		this.driver = driver;
	}

	public static boolean isOracle(DatabaseType type) {
		return type == DatabaseType.ORACLE;
	}
	
	public static boolean isSqlServer(DatabaseType type) {
		return type == DatabaseType.SQL_SERVER;
	}
	
	@Override
	public String toString() {
		return driver;
	}
}
