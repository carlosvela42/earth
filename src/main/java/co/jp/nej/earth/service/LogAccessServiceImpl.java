package co.jp.nej.earth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
public class LogAccessServiceImpl extends BaseService implements LogAccessService {

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    /**
     * {@inheritDoc}
     * @throws EarthException
     */
    @Override
    public boolean addLogAccess(StrLogAccess logAccess, String workspaceId) throws EarthException {
        return (boolean)executeTransaction(workspaceId, () -> {
            return strLogAccessDao.add(workspaceId, logAccess) > 0;
        });
    }

}
