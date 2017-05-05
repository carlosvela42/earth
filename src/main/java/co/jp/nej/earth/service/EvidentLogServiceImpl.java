package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class EvidentLogServiceImpl implements EvidentLogService {

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId, Long offset, Long limit,
                                                   OrderSpecifier<String> orderByColumn) throws EarthException {
        try {
            return strLogAccessDao.findAll(workspaceId, offset, limit, orderByColumn);
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
