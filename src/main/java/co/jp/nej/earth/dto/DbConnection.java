package co.jp.nej.earth.dto;

import co.jp.nej.earth.model.enums.DatabaseType;

public class DbConnection {
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getJdbcConnection() {
		return jdbcConnection;
	}
	public void setJdbcConnection(String jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
	public DatabaseType getDbType() {
		return dbType;
	}
	public void setDbType(DatabaseType dbType) {
		this.dbType = dbType;
	}
	String schemaName;
	String dbUser;
	String dbPassword;
	String owner;
	String jdbcConnection;
	String dbServer;
	public String getDbServer() {
		return dbServer;
	}
	public void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}
	public DbConnection() {
		super();
	}
	public DbConnection(String schemaName, String dbUser, String dbPassword, String owner, String jdbcConnection,
			DatabaseType dbType) {
		super();
		this.schemaName = schemaName;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.owner = owner;
		this.jdbcConnection = jdbcConnection;
		this.dbType = dbType;
	}
	DatabaseType dbType;
	
}
