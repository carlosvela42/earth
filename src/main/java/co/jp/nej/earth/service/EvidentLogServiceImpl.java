package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class EvidentLogServiceImpl implements EvidentLogService {

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException {
        try{
            return strLogAccessDao.getListByWorkspaceId(workspaceId);
        }catch (Exception ex){
            throw  new EarthException(ex.getMessage());
        }
    }
}
