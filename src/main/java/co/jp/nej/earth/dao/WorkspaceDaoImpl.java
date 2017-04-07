package co.jp.nej.earth.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.sql.QMgrWorkspace;
import co.jp.nej.earth.model.sql.QMgrWorkspaceConnect;

/**
 * @author longlt
 *
 */

@Repository
public class WorkspaceDaoImpl implements WorkspaceDao {
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DatabaseType databaseType;

	public List<MgrWorkspaceConnect> getAllMgrConnections() throws EarthException {
		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();

		QBean<MgrWorkspaceConnect> selectList = Projections.bean(MgrWorkspaceConnect.class, qMgrWorkspaceConnect.all());

		List<MgrWorkspaceConnect> mgrWorkspaceConnects = ConnectionManager
				.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList).from(qMgrWorkspaceConnect)
				.fetch();
		return mgrWorkspaceConnects;
	}

	public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException {
		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();

		QBean<MgrWorkspaceConnect> selectList = Projections.bean(MgrWorkspaceConnect.class, qMgrWorkspaceConnect.all());

		MgrWorkspaceConnect mgrWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
				.select(selectList).from(qMgrWorkspaceConnect).where(qMgrWorkspaceConnect.workspaceId.eq(workspaceId))
				.fetchOne();
		return mgrWorkspaceConnect;
	}

	public List<MgrWorkspace> getAll() throws EarthException {
		QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

		QBean<MgrWorkspace> selectList = Projections.bean(MgrWorkspace.class, qMgrWorkspace.all());

		List<MgrWorkspace> mgrWorkspaces = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
				.select(selectList).from(qMgrWorkspace).fetch();
		return mgrWorkspaces;
	}

	public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {

		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
		QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

		MgrWorkspace mgrWorkspace = new MgrWorkspace();
		mgrWorkspace.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
		mgrWorkspace.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());
		mgrWorkspace.setWorkspaceName(mgrWorkspaceConnect.getSchemaName());

		boolean isSuccess = true;
		try {
			String user = mgrWorkspaceConnect.getSchemaName();
			String password = mgrWorkspaceConnect.getDbPassword();
			EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
			Statement stmt = earthQueryFactory.getConnection().createStatement();
			createSchema(user, password, stmt);
			createTable(user, stmt);
			long insertedWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.insert(qMgrWorkspaceConnect).populate(mgrWorkspaceConnect).execute();
			long insertedWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.insert(qMgrWorkspace).populate(mgrWorkspace).execute();

			if (!(insertedWorkspaceConnect > 0L && insertedWorkspace > 0L)) {
				isSuccess = false;
			}

		} catch (Exception e) {
			throw new EarthException(e.getMessage());
		}
		return isSuccess;
	}

	public boolean deleteList(List<String> workspaceIds) throws EarthException {
		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
		QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

		boolean isSuccess = true;
		try {
			List<String> schemaNames = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.select(qMgrWorkspaceConnect.schemaName).from(qMgrWorkspaceConnect)
					.where(qMgrWorkspaceConnect.workspaceId.in(workspaceIds)).fetch();

			EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
			Statement stmt = earthQueryFactory.getConnection().createStatement();
			//dropSchema(schemaNames, stmt);
			long countWorkspaceConnection = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.delete(qMgrWorkspaceConnect).where(qMgrWorkspaceConnect.workspaceId.in(workspaceIds)).execute();

			long countWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.delete(qMgrWorkspace).where(qMgrWorkspace.workspaceId.in(workspaceIds)).execute();

		} catch (Exception e) {
			isSuccess = false;
			throw new EarthException(e.getMessage());
		}

		return isSuccess;

	}

	private void createSchema(String user, String password, Statement stmt) throws EarthException {
		try {
			if ("ORACLE".equals(databaseType.name())) {
				stmt.execute("Create User C##" + user + " identified By " + password);
				stmt.execute("Grant Create Session to C##" + user);
			} else {
				stmt.execute("Create Database " + user);
				stmt.execute("Use " + user + ";");
			}

		} catch (Exception e) {
			throw new EarthException(e.getMessage());
		}
	}

	private void dropSchema(List<String> schemaNames, Statement stmt) throws EarthException {
		try {
			if ("ORACLE".equals(databaseType.name())) {
				for (String schemaName : schemaNames) {
					stmt.execute("Drop User C##" + schemaName.toUpperCase() + " Cascade ");
				}
			} else {
				for (String schemaName : schemaNames) {
					stmt.execute("Drop DATABASE " + schemaName + ";");
				}
			}
		} catch (Exception e) {
			throw new EarthException(e.getMessage());
		}
	}

	private void createTable(String user, Statement stmt) throws EarthException {

		StringBuffer buffer = new StringBuffer();
		String scriptFilePath;
		BufferedReader reader;
		String line = "";
		String[] parts;
		try {
			if ("ORACLE".equals(databaseType.name())) {
				scriptFilePath = getClass().getClassLoader().getResource("oracle_1.0.sql").getPath();
				reader = new BufferedReader(new FileReader(scriptFilePath));
				while ((line = reader.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				String Temp = buffer.toString().replaceAll("##", "C##" + user.toUpperCase());
				parts = Temp.split(";");
			} else {
				scriptFilePath = getClass().getClassLoader().getResource("SQLServer_1.0.sql").getPath();
				reader = new BufferedReader(new FileReader(scriptFilePath));
				while ((line = reader.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				parts = buffer.toString().split(";");
			}
			for (String part : parts) {
				stmt.addBatch(part);
			}
			stmt.executeBatch();
			reader.close();
		} catch (Exception ex) {
			throw new EarthException(ex.getMessage());
		}
	}

	public MgrWorkspaceConnect getOne(String workspaceId) throws EarthException {

		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();

		QBean<MgrWorkspaceConnect> selectList = Projections.bean(MgrWorkspaceConnect.class, qMgrWorkspaceConnect.all());

		MgrWorkspaceConnect mgrWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
				.select(selectList).from(qMgrWorkspaceConnect).where(qMgrWorkspaceConnect.workspaceId.eq(workspaceId))
				.fetchOne();

		return mgrWorkspaceConnect;
	}

	public boolean getById(String workspaceId) throws EarthException {
		boolean isExit = false;
		try {
			QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();
			QBean<MgrWorkspace> selectList = Projections.bean(MgrWorkspace.class, qMgrWorkspace.all());
			List<MgrWorkspace> mgrWorkspaces = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
					.select(selectList).from(qMgrWorkspace).where(qMgrWorkspace.workspaceId.eq(workspaceId)).fetch();
			if (!mgrWorkspaces.isEmpty()) {
				isExit = true;
			}
		} catch (Exception ex) {
			throw new EarthException(ex.getMessage());
		}
		return isExit;
	}

	public boolean UpdateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {

		QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
		QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();
		QBean<MgrWorkspaceConnect> oneWorkspaceConnect = Projections.bean(MgrWorkspaceConnect.class,
				qMgrWorkspaceConnect.all());

		MgrWorkspace mgrWorkspace = new MgrWorkspace();
		mgrWorkspace.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
		mgrWorkspace.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());
		mgrWorkspace.setWorkspaceName(mgrWorkspaceConnect.getSchemaName());

		String user = mgrWorkspaceConnect.getDbUser();
		String password = mgrWorkspaceConnect.getDbPassword();
		boolean status = true;
		try {
			MgrWorkspaceConnect mgrWorkspaceConnectOld = ConnectionManager
					.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(oneWorkspaceConnect)
					.from(qMgrWorkspaceConnect)
					.where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).fetchOne();

			if (!(mgrWorkspaceConnectOld.getSchemaName().equals(mgrWorkspaceConnect.getSchemaName()))
					|| !(mgrWorkspaceConnectOld.getDbUser().equals(mgrWorkspaceConnect.getDbUser()))) {

				EarthQueryFactory earthQueryFactory = ConnectionManager
						.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
				Statement stmt = earthQueryFactory.getConnection().createStatement();
				createSchema(user, password, stmt);
				createTable(user, stmt);

				long updatedWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
						.update(qMgrWorkspaceConnect)
						.set(qMgrWorkspaceConnect.schemaName, mgrWorkspaceConnect.getSchemaName())
						.set(qMgrWorkspaceConnect.dbUser, mgrWorkspaceConnect.getDbUser())
						.where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();

				long updatedWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
						.update(qMgrWorkspace).set(qMgrWorkspace.workspaceName, mgrWorkspaceConnect.getSchemaName())
						.where(qMgrWorkspace.workspaceId.eq(mgrWorkspace.getWorkspaceId())).execute();
				if (!(updatedWorkspaceConnect > 0L) && !(updatedWorkspace > 0L)) {
					status = false;
				}
			} else if (!(mgrWorkspaceConnectOld.getDbPassword().equals(mgrWorkspaceConnect.getDbPassword()))
					|| !(mgrWorkspaceConnectOld.getOwner().equals(mgrWorkspaceConnect.getOwner()))) {

				EarthQueryFactory earthQueryFactory = ConnectionManager
						.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
				Statement stmt = earthQueryFactory.getConnection().createStatement();
				updateSchema(user, password, stmt);
				long updatedPassword = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
						.update(qMgrWorkspaceConnect)
						.set(qMgrWorkspaceConnect.dbPassword, mgrWorkspaceConnect.getDbPassword())
						.where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();
				
				long updatedOwner= ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
						.update(qMgrWorkspaceConnect)
						.set(qMgrWorkspaceConnect.owner, mgrWorkspaceConnect.getOwner())
						.where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();
			}
		} catch (Exception ex) {
			status = false;
			throw new EarthException(ex.getMessage());
		}

		return status;

	}

	private void updateSchema(String user, String password, Statement stmt) throws EarthException {
		try {
			if ("ORACLE".equals(databaseType.name())) {
				stmt.execute("ALTER USER C##" + user + " IDENTIFIED BY " + password);
			} else {
				stmt.execute("Create Database " + user);
				stmt.execute("Use " + user + ";");
			}
		} catch (Exception e) {
			throw new EarthException(e.getMessage());
		}
	}

	/*
	 * private void updateTable(String user, Statement stmt) throws
	 * EarthException {
	 * 
	 * StringBuffer buffer = new StringBuffer(); try { String aSQLScriptFilePath
	 * = getClass().getClassLoader().getResource("oracle_1.0.sql").getPath();
	 * BufferedReader reader = new BufferedReader(new
	 * FileReader(aSQLScriptFilePath)); String line = ""; while ((line =
	 * reader.readLine()) != null) { buffer.append(line).append("\n"); }
	 * reader.close(); String Temp = buffer.toString().replaceAll("##", "C##" +
	 * user.toUpperCase()); String[] parts = Temp.split(";"); for (String part :
	 * parts) { stmt.addBatch(part); } stmt.executeBatch(); } catch (Exception
	 * ex) { throw new EarthException(ex.getMessage()); } }
	 */

}
