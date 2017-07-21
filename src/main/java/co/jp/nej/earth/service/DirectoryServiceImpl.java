package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.DirectoryDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryServiceImpl extends BaseService implements DirectoryService {
    @Autowired
    private DirectoryDao directoryDao;

    @Autowired
    private EMessageResource messageSource;

    @Override
    public List<Directory> getAllDirectories() throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return directoryDao.findAll(Constant.EARTH_WORKSPACE_ID);
        }), Directory.class);
    }

    @Override
    public List<Integer> getAllDirectoryIds(String siteId, String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
            return directoryDao.getDirectoryIds(Integer.valueOf(siteId), workspaceId);
        }), Integer.class);
    }

    @Override
    public List<Directory> getAllDirectoriesBySite(String siteId, String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
            return directoryDao.getDirectoriesBySite(Integer.valueOf(siteId));
        }), Directory.class);
    }

    @Override
    public boolean deleteDirectorys(List<Integer> directoryIds, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return directoryDao.deleteDirectorys(directoryIds, workspaceId) > 0L;
        });
    }

    @Override
    public List<Message> validateInsert(Directory directory, String workspaceId) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
            List<Message> messages = new ArrayList<Message>();
            if (!StringUtils.isEmpty(directory.getDataDirectoryId())
                    && !StringUtils.isEmpty(directory.getNewCreateFile())
                    && !StringUtils.isEmpty(directory.getReservedDiskVolSize())
                    && !StringUtils.isEmpty(directory.getDiskVolSize())
                    && !StringUtils.isEmpty(directory.getFolderPath())) {
                Directory directoryTemp = directoryDao.getById(directory.getDataDirectoryId());
                if (!EStringUtil.isEmpty(directoryTemp)) {
                    Message message = new Message(Constant.Directory.IS_EXIT_DIRECTORY,
                            messageSource.get(ErrorCode.E0003, new String[] { "DIRECTORY"}));
                    messages.add(message);
                }

                if (Integer.parseInt(directory.getReservedDiskVolSize()) > Integer
                        .parseInt(directory.getDiskVolSize())) {
                    Message message = new Message(ErrorCode.E0006,
                            messageSource.get(ErrorCode.E0006, new String[] { "secured.disk.space", "disk.space" }));
                    messages.add(message);
                }
            }
            return messages;
        }), Message.class);
    }

    @Override
    public boolean insertOne(Directory directory, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return directoryDao.insertOne(directory, workspaceId) > 0;
        });
    }

    @Override
    public Directory getById(String dataDirectoryId, String workspaceId) throws EarthException {
        return ConversionUtil.castObject(executeTransaction(workspaceId, () -> {
            return directoryDao.getById(Integer.valueOf(dataDirectoryId));
        }), Directory.class);
    }

    @Override
    public boolean updateDirectory(Directory directory, String workspaceId) throws EarthException {
        return (boolean) executeTransaction(workspaceId, () -> {
            return directoryDao.udpateOne(directory, workspaceId) > 0;
        });
    }
}
