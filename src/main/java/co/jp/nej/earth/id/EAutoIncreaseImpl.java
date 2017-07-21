package co.jp.nej.earth.id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.dao.IncrementDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.EarthId;
import co.jp.nej.earth.model.entity.MgrIncrement;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.sql.QMgrIncrement;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;

@Service
public class EAutoIncreaseImpl implements EAutoIncrease{

    static final int INDEX_OF_TYPE = 1;
    static final int INDEX_OF_DATETIME = 2;
    static final int INDEX_OF_SESSION = 3;
    static final int INDEX_OF_LASTUPDATE = 4;
    static final int INDEX_OF_ROWID = 1;

    @Autowired
    private DatabaseType databaseType;

    @Autowired
    private IncrementDao incrementDao;

    private static final QMgrIncrement qMgrIncrement = QMgrIncrement.newInstance();
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EAutoIncreaseImpl.class);

    public int getAutoId(EarthId type, String sessionID) throws EarthException{

        int newId = 0;

        TransactionManager transactionManager = null;

        try {

            transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);

            String updateTime = DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);

            Integer incrementMax = incrementDao.getByType(type);

            if(incrementMax == null){
                incrementMax = 1;
            } else {
                incrementMax ++;
            }

            MgrIncrement increment = new MgrIncrement();
            increment.setIncrementType(type.getValue());
            increment.setIncrementDatetime(updateTime);
            increment.setIncrementData(incrementMax);
            increment.setSessionId(sessionID);
            increment.setLastUpdateTime(updateTime);

            if(databaseType.name().equals(DatabaseType.ORACLE.name())) {
                newId = insertOracle(ConnectionManager.getEarthQueryFactory().getConnection(),increment);
            } else if(databaseType.name().equals(DatabaseType.SQL_SERVER.name())) {
                newId = insertMSSQL(ConnectionManager.getEarthQueryFactory().getConnection(),increment);
            }

            transactionManager.getManager().commit(transactionManager.getTxStatus());

        } catch (EarthException ex) {
            if (transactionManager != null) {
                transactionManager.getManager().rollback(transactionManager.getTxStatus());
            }

            LOG.error(ex.getMessage());

        }
        return newId;
    }

    private String genTSQLInsert(String sType) {
        String tableName = qMgrIncrement.getTableName();
        String columnIdTypeName = qMgrIncrement.incrementType.getMetadata().getName();
        String columnIdDataName = qMgrIncrement.incrementData.getMetadata().getName();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append("(");
        sql.append(columnIdTypeName).append(",");
        sql.append(columnIdDataName).append(",");
        sql.append(qMgrIncrement.incrementDateTime.getMetadata().getName()).append(",");
        sql.append(qMgrIncrement.sessionId.getMetadata().getName()).append(",");
        sql.append(qMgrIncrement.lastUpdateTime.getMetadata().getName());
        sql.append(") ");
        sql.append(" OUTPUT INSERTED.").append(columnIdDataName);
        sql.append(" VALUES (?, ");
        sql.append("(SELECT COALESCE(MAX(").append(columnIdDataName).append("),0) + 1 FROM ");
        sql.append(tableName).append(" WHERE ").append(columnIdTypeName).append("='").append(sType).append("')");
        sql.append(", ?, ?, ?)");
        return sql.toString();
    }

    private String genPLSQLInsert(String sType) {
        String tableName = qMgrIncrement.getTableName();
        String columnIdTypeName = qMgrIncrement.incrementType.getMetadata().getName();
        String columnIdDataName = qMgrIncrement.incrementData.getMetadata().getName();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append("(");
        sql.append(columnIdTypeName).append(",");
        sql.append(columnIdDataName).append(",");
        sql.append(qMgrIncrement.incrementDateTime.getMetadata().getName()).append(",");
        sql.append(qMgrIncrement.sessionId.getMetadata().getName()).append(",");
        sql.append(qMgrIncrement.lastUpdateTime.getMetadata().getName());
        sql.append(") VALUES (?, ");
        sql.append("(SELECT COALESCE(MAX(").append(columnIdDataName).append("),0) + 1 FROM ");
        sql.append(tableName).append(" WHERE ").append(columnIdTypeName).append("='").append(sType).append("')");
        sql.append(", ?, ?, ?)");
        return sql.toString();
    }

    private String genPLSQLGetRowId() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(qMgrIncrement.incrementData.getMetadata().getName());
        sql.append(" FROM ").append(qMgrIncrement.getTableName());
        sql.append(" WHERE ROWID = ? ");

        return sql.toString();
    }

    private Integer insertOracle(Connection connection, MgrIncrement mgrIncrement) {

        Integer valueMax = 0;

        try {

            String sql = genPLSQLInsert(mgrIncrement.getIncrementType());
            PreparedStatement preparedStatement = connection.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(INDEX_OF_TYPE, mgrIncrement.getIncrementType());
            preparedStatement.setString(INDEX_OF_DATETIME, mgrIncrement.getIncrementDateTime());
            preparedStatement.setString(INDEX_OF_SESSION, mgrIncrement.getSessionId());
            preparedStatement.setString(INDEX_OF_LASTUPDATE, mgrIncrement.getLastUpdateTime());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            String rowId = "";
            if (rs != null && rs.next()) {
                rowId = rs.getString(1);
            }
            if(!EStringUtil.isEmpty(rowId)) {
                sql = genPLSQLGetRowId();
                PreparedStatement prepared = connection.prepareStatement(sql);

                prepared.setString(INDEX_OF_ROWID, rowId);

                ResultSet rsInsertedItem = prepared.executeQuery();
                if (rsInsertedItem != null && rsInsertedItem.next()) {
                    valueMax = rsInsertedItem.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valueMax;
    }

    private Integer insertMSSQL(Connection connection, MgrIncrement mgrIncrement) {
        Integer valueMax = 0;

        try {

            String sql = genTSQLInsert(mgrIncrement.getIncrementType());
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(INDEX_OF_TYPE, mgrIncrement.getIncrementType());
            preparedStatement.setString(INDEX_OF_DATETIME, mgrIncrement.getIncrementDateTime());
            preparedStatement.setString(INDEX_OF_SESSION, mgrIncrement.getSessionId());
            preparedStatement.setString(INDEX_OF_LASTUPDATE, mgrIncrement.getLastUpdateTime());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs != null && rs.next()) {
                valueMax = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valueMax;
    }

}
