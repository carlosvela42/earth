package co.jp.nej.earth.model.enums;

public enum DatabaseType {
    ORACLE("oracle.jdbc.driver.OracleDriver", "oracle_1.0.sql"),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver","SQLServer_1.0.sql");

    private String driver;
    private String sourceFile;

    DatabaseType(String driver,String sourceFile){
        this.driver = driver;
        this.sourceFile = sourceFile;
    }

    public static boolean isOracle(DatabaseType type) {
        return type == DatabaseType.ORACLE;
    }

    public static boolean isSqlServer(DatabaseType type) {
        return type == DatabaseType.SQL_SERVER;
    }

    public String getSourceFile() {
        return sourceFile;
    }
    @Override
    public String toString() {
        return driver;
    }
}
