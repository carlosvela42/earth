package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.sql.QMgrWorkspace;
import co.jp.nej.earth.model.sql.QMgrWorkspaceConnect;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;
import java.util.List;

/**
 * @author longlt
 *
 */

@Repository
public class WorkspaceDaoImpl implements WorkspaceDao {
  @Autowired
  private DatabaseType databaseType;

  public List<MgrWorkspaceConnect> getAllMgrConnections() throws EarthException {
    try {
      QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
      QBean<MgrWorkspaceConnect> selectList = Projections.bean(MgrWorkspaceConnect.class, qMgrWorkspaceConnect.all());
      List<MgrWorkspaceConnect> mgrWorkspaceConnects = ConnectionManager
          .getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).select(selectList).from(qMgrWorkspaceConnect).fetch();
      return mgrWorkspaceConnects;
    } catch (Exception ex) {
      throw new EarthException(ex);
    }
  }

  public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException {
    try {
      QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
      QBean<MgrWorkspaceConnect> selectList = Projections.bean(MgrWorkspaceConnect.class, qMgrWorkspaceConnect.all());
      MgrWorkspaceConnect mgrWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
          .select(selectList).from(qMgrWorkspaceConnect).where(qMgrWorkspaceConnect.workspaceId.eq(workspaceId))
          .fetchOne();
      return mgrWorkspaceConnect;
    } catch (Exception ex) {
      throw new EarthException(ex);
    }
  }

  public List<MgrWorkspace> getAll() throws EarthException {
    try {
      QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();
      QBean<MgrWorkspace> selectList = Projections.bean(MgrWorkspace.class, qMgrWorkspace.all());
      List<MgrWorkspace> mgrWorkspaces = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
          .select(selectList).from(qMgrWorkspace).orderBy(qMgrWorkspace.workspaceName.asc()).fetch();
      return mgrWorkspaces;
    } catch (Exception ex) {
      throw new EarthException(ex);
    }
  }

  public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
    try {
      QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
      QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

      if (DatabaseType.isOracle(databaseType)) {
        mgrWorkspaceConnect.setDbType(Constant.WorkSpace.ORACLE);
      } else {
        mgrWorkspaceConnect.setDbType(Constant.WorkSpace.SQL);
      }
      mgrWorkspaceConnect.setLastUpdateTime(DateUtil.getCurrentDateString());

      MgrWorkspace mgrWorkspace = new MgrWorkspace();
      mgrWorkspace.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
      mgrWorkspace.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());
      mgrWorkspace.setWorkspaceName(mgrWorkspaceConnect.getSchemaName());
      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).insert(qMgrWorkspaceConnect)
          .populate(mgrWorkspaceConnect).execute();
      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).insert(qMgrWorkspace).populate(mgrWorkspace)
          .execute();
    } catch (Exception e) {
      throw new EarthException(e);
    }
    return true;
  }

  public boolean deleteList(List<String> workspaceIds) throws EarthException {
    QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
    QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();
    try {
      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).delete(qMgrWorkspaceConnect)
          .where(qMgrWorkspaceConnect.workspaceId.in(workspaceIds)).execute();
      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).delete(qMgrWorkspace)
          .where(qMgrWorkspace.workspaceId.in(workspaceIds)).execute();
    } catch (Exception e) {
      throw new EarthException(e);
    }
    return true;
  }

  public boolean createSchema(MgrWorkspaceConnect mgrWorkspaceConnect, String workspaceId) throws EarthException {
    try {
      String user = mgrWorkspaceConnect.getSchemaName();
      String password = mgrWorkspaceConnect.getDbPassword();
      EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
      Statement stmt = earthQueryFactory.getConnection().createStatement();
      if (DatabaseType.isOracle(databaseType)) {
        stmt.execute(Constant.WorkSpace.CREATE_USER + user + Constant.WorkSpace.IDENTIFIED_BY + password);
        stmt.execute(Constant.WorkSpace.GRANT_SESSION + user);
        stmt.execute(Constant.WorkSpace.GRANT_DBA + user);
      } else {
        stmt.execute("Create Database " + user);
        stmt.execute("Use " + user + ";");
      }
    } catch (Exception e) {
      throw new EarthException(e);
    }
    return true;
  }

  public void createTable(MgrWorkspaceConnect mgrWorkspaceConnect, String workspaceId) throws EarthException {
    StringBuffer buffer = new StringBuffer();
    BufferedReader reader;
    String line = EStringUtil.EMPTY;
    String[] parts;
    try {
      EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
      Statement stmt = earthQueryFactory.getConnection().createStatement();
      if (DatabaseType.isOracle(databaseType)) {
        String scriptFilePath = getClass().getClassLoader().getResource(DatabaseType.ORACLE.getSourceFile()).getPath();
        reader = new BufferedReader(new FileReader(scriptFilePath));
        while ((line = reader.readLine()) != null) {
          buffer.append(line).append("\n");
        }
        parts = buffer.toString()
            .replaceAll(Constant.WorkSpace.CHARACTER_REPLACE, mgrWorkspaceConnect.getDbUser().toUpperCase()).split(";");
      } else {
        String scriptFilePath = getClass().getClassLoader().getResource(DatabaseType.SQL_SERVER.getSourceFile())
            .getPath();
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
      throw new EarthException(ex);
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
      throw new EarthException(ex);
    }
    return isExit;
  }

  public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {
    try {
      QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
      QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).update(qMgrWorkspaceConnect)
          .set(qMgrWorkspaceConnect.schemaName, mgrWorkspaceConnect.getSchemaName())
          .set(qMgrWorkspaceConnect.dbUser, mgrWorkspaceConnect.getDbUser())
          .set(qMgrWorkspaceConnect.dbPassword, mgrWorkspaceConnect.getDbPassword())
          .set(qMgrWorkspaceConnect.owner, mgrWorkspaceConnect.getOwner())
          .where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();

      ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID).update(qMgrWorkspace)
          .set(qMgrWorkspace.workspaceName, mgrWorkspaceConnect.getSchemaName())
          .where(qMgrWorkspace.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();
    } catch (Exception ex) {
      throw new EarthException(ex);
    }
    return true;
  }
}
