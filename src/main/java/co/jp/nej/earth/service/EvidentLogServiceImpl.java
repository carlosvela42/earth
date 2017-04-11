package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EvidentLogServiceImpl implements EvidentLogService {

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException {
        try{
            return strLogAccessDao.getListByWorkspaceId(workspaceId);
        }
        catch (Exception ex){
            throw  new EarthException(ex.getMessage());
        }
    }
}
