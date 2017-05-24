package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QDirectory;
import co.jp.nej.earth.model.sql.QSite;

@Repository
public class DirectoryDaoImpl extends BaseDaoImpl<Directory> implements DirectoryDao {

    public DirectoryDaoImpl() throws Exception {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Directory> getDirectoriesBySite(int siteId) throws EarthException {
        QDirectory qDirectory = QDirectory.newInstance();
        QSite qSite = QSite.newInstance();
        EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
        try {
            ResultSet resultSet = earthQueryFactory
                    .select(qDirectory.dataDirectoryId, qDirectory.diskVolSize, qDirectory.folderPath,
                            qDirectory.reservedDiskVolSize, qDirectory.reservedDiskVolSize, qDirectory.lastUpdateTime)
                    .from(qDirectory).innerJoin(qSite).on(qDirectory.dataDirectoryId.eq(qSite.dataDirectoryId))
                    .where(qSite.siteId.eq(siteId)).getResults();
            List<Directory> directories = new ArrayList<>();

            while (resultSet.next()) {
                Directory directory = new Directory();
                directory.setDataDirectoryId(resultSet.getInt(ColumnNames.DATA_DIRECTORY_ID.toString()));
                directory.setDiskVolSize(resultSet.getString(ColumnNames.DISK_VOL_SIZE.toString()));
                directory.setFolderPath(resultSet.getString(ColumnNames.FOLDER_PATH.toString()));
                directory.setLastUpdateTime(resultSet.getString(ColumnNames.LAST_UPDATE_TIME.toString()));
                directory.setNewCreateFile(resultSet.getInt(ColumnNames.DATA_DIRECTORY_ID.toString()));
                directory.setReservedDiskVolSize(resultSet.getString(ColumnNames.RESERVED_DISK_VOL_SIZE.toString()));
                directories.add(directory);
            }
            return directories;
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
    }
}
