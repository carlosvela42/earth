package co.jp.nej.earth.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import co.jp.nej.earth.util.EStringUtil;

/**
 * @author longlt
 *
 */

@Repository
public class WorkspaceDaoImpl implements WorkspaceDao {
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
        mgrWorkspaceConnect.setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
        mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
        mgrWorkspace.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
        mgrWorkspace.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());
        mgrWorkspace.setWorkspaceName(mgrWorkspaceConnect.getSchemaName());

        boolean isSuccess;
        try {
            String user = mgrWorkspaceConnect.getSchemaName();
            String password = mgrWorkspaceConnect.getDbPassword();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
            Statement stmt = earthQueryFactory.getConnection().createStatement();
            isSuccess = createSchema(user, password, stmt);
            if (isSuccess) {
                long insertedWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                        .insert(qMgrWorkspaceConnect).populate(mgrWorkspaceConnect).execute();
                long insertedWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                        .insert(qMgrWorkspace).populate(mgrWorkspace).execute();
                if (!(insertedWorkspaceConnect > 0L && insertedWorkspace > 0L)) {
                    isSuccess = false;
                } else {
                    createTable(user, stmt);
                }
            }
        } catch (Exception e) {
            List<String> worksapceIds = new ArrayList<>();
            worksapceIds.add(mgrWorkspaceConnect.getWorkspaceId());
            deleteList(worksapceIds);
            throw new EarthException(e.getMessage());
        }
        return isSuccess;
    }

    public boolean deleteList(List<String> workspaceIds) throws EarthException {
        QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
        QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();

        boolean isSuccess = false;
        try {
            List<String> schemaNames = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                    .select(qMgrWorkspaceConnect.schemaName).from(qMgrWorkspaceConnect)
                    .where(qMgrWorkspaceConnect.workspaceId.in(workspaceIds)).fetch();

            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
            Statement stmt = earthQueryFactory.getConnection().createStatement();
            isSuccess = dropSchema(schemaNames, stmt);
            if (isSuccess) {
                long countWorkspaceConnection = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                        .delete(qMgrWorkspaceConnect).where(qMgrWorkspaceConnect.workspaceId.in(workspaceIds))
                        .execute();

                long countWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                        .delete(qMgrWorkspace).where(qMgrWorkspace.workspaceId.in(workspaceIds)).execute();

                if (!(countWorkspaceConnection > 0L) && !(countWorkspace > 0L)) {
                    isSuccess = false;
                }
            }

        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }

        return isSuccess;

    }

    private boolean createSchema(String user, String password, Statement stmt) throws EarthException {
        boolean status = false;
        try {
            if (Constant.WorkSpace.ORACLE.equals(databaseType.name())) {
                stmt.execute(Constant.WorkSpace.CREATE_USER + user + Constant.WorkSpace.IDENTIFIED_BY + password);
                stmt.execute(Constant.WorkSpace.GRANT_SESSION + user);
                status = true;
            } else {
                stmt.execute("Create Database " + user);
                stmt.execute("Use " + user + ";");
                status = true;
            }

        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return status;
    }

    private boolean dropSchema(List<String> schemaNames, Statement stmt) throws EarthException {
        boolean status = false;
        try {
            if (Constant.WorkSpace.ORACLE.equals(databaseType.name())) {
                for (String schemaName : schemaNames) {
                    stmt.execute(Constant.WorkSpace.DROP_USER + schemaName.toUpperCase() + Constant.WorkSpace.CASCADE);
                }
                status = true;
            } else {
                for (String schemaName : schemaNames) {
                    stmt.execute("Drop DATABASE " + schemaName + ";");
                }
                status = true;
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return status;
    }

    private void createTable(String user, Statement stmt) throws EarthException {

        StringBuffer buffer = new StringBuffer();
        String scriptFilePath;
        BufferedReader reader;
        String line = EStringUtil.EMPTY;
        String[] parts;
        try {
            if (Constant.WorkSpace.ORACLE.equals(databaseType.name())) {
                scriptFilePath = getClass().getClassLoader().getResource(DatabaseType.ORACLE.getSourceFile()).getPath();
                reader = new BufferedReader(new FileReader(scriptFilePath));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                parts = buffer.toString()
                        .replaceAll(Constant.WorkSpace.CHARACTER_REPLACE, user.toUpperCase()).split(";");

            } else {
                scriptFilePath = getClass().getClassLoader().getResource(DatabaseType.SQL_SERVER.getSourceFile())
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

    public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException {

        QMgrWorkspaceConnect qMgrWorkspaceConnect = QMgrWorkspaceConnect.newInstance();
        QMgrWorkspace qMgrWorkspace = QMgrWorkspace.newInstance();
        QBean<MgrWorkspaceConnect> oneWorkspaceConnect = Projections.bean(MgrWorkspaceConnect.class,
                qMgrWorkspaceConnect.all());
        mgrWorkspaceConnect.setSchemaName(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getSchemaName());
        mgrWorkspaceConnect.setDbUser(Constant.WorkSpace.CHARACTER_COMMON + mgrWorkspaceConnect.getDbUser());
        MgrWorkspace mgrWorkspace = new MgrWorkspace();
        mgrWorkspace.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
        mgrWorkspace.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());
        mgrWorkspace.setWorkspaceName(mgrWorkspaceConnect.getSchemaName());

        String user = mgrWorkspaceConnect.getDbUser();
        String password = mgrWorkspaceConnect.getDbPassword();
        boolean isSuccess = false;
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
                isSuccess = createSchema(user, password, stmt);
                if (isSuccess) {
                    long updatedWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                            .update(qMgrWorkspaceConnect)
                            .set(qMgrWorkspaceConnect.schemaName, mgrWorkspaceConnect.getSchemaName())
                            .set(qMgrWorkspaceConnect.dbUser, mgrWorkspaceConnect.getDbUser())
                            .set(qMgrWorkspaceConnect.dbPassword, mgrWorkspaceConnect.getDbPassword())
                            .set(qMgrWorkspaceConnect.owner, mgrWorkspaceConnect.getOwner())
                            .where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();

                    long updatedWorkspace = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                            .update(qMgrWorkspace).set(qMgrWorkspace.workspaceName, mgrWorkspaceConnect.getSchemaName())
                            .where(qMgrWorkspace.workspaceId.eq(mgrWorkspace.getWorkspaceId())).execute();
                    if (!(updatedWorkspaceConnect > 0L) && !(updatedWorkspace > 0L)) {
                        isSuccess = false;
                    } else {
                        createTable(user, stmt);
                    }
                }
            } else if (!(mgrWorkspaceConnectOld.getDbPassword().equals(mgrWorkspaceConnect.getDbPassword()))
                    || !(mgrWorkspaceConnectOld.getOwner().equals(mgrWorkspaceConnect.getOwner()))) {

                EarthQueryFactory earthQueryFactory = ConnectionManager
                        .getEarthQueryFactory(Constant.SYSTEM_WORKSPACE_ID);
                Statement stmt = earthQueryFactory.getConnection().createStatement();
                isSuccess = updateSchema(user, password, stmt);
                if (isSuccess) {
                    long updatedWorkspaceConnect = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID)
                            .update(qMgrWorkspaceConnect)
                            .set(qMgrWorkspaceConnect.schemaName, mgrWorkspaceConnect.getSchemaName())
                            .set(qMgrWorkspaceConnect.dbUser, mgrWorkspaceConnect.getDbUser())
                            .set(qMgrWorkspaceConnect.dbPassword, mgrWorkspaceConnect.getDbPassword())
                            .set(qMgrWorkspaceConnect.owner, mgrWorkspaceConnect.getOwner())
                            .where(qMgrWorkspaceConnect.workspaceId.eq(mgrWorkspaceConnect.getWorkspaceId())).execute();

                    if (!(updatedWorkspaceConnect > 0L)) {
                        isSuccess = false;
                    }
                }
            }
        } catch (Exception ex) {
            List<String> worksapceIds = new ArrayList<>();
            worksapceIds.add(mgrWorkspaceConnect.getWorkspaceId());
            deleteList(worksapceIds);
            throw new EarthException(ex.getMessage());
        }

        return isSuccess;

    }

    private boolean updateSchema(String user, String password, Statement stmt) throws EarthException {
        boolean status = false;
        try {
            if (Constant.WorkSpace.ORACLE.equals(databaseType.name())) {
                stmt.execute(Constant.WorkSpace.ALTER_USER + user + Constant.WorkSpace.IDENTIFIED_BY + password);
                status = true;
            } else {
                stmt.execute("Create Database " + user);
                stmt.execute("Use " + user + ";");
                status = true;
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return status;
    }

}
