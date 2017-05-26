package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.OrderSpecifier;

import co.jp.nej.earth.dao.StrLogAccessDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.util.ConversionUtil;

@Service
public class EvidentLogServiceImpl extends BaseService implements EvidentLogService {

    @Autowired
    private StrLogAccessDao strLogAccessDao;

    @Override
    public List<StrLogAccess> getListByWorkspaceId(String workspaceId, Long offset, Long limit,
                                                   List<OrderSpecifier<?>> orderBys) throws EarthException {
        return ConversionUtil.castList(executeTransaction(workspaceId, () -> {
            return strLogAccessDao.findAll(workspaceId, offset, limit, orderBys, null);
        }), StrLogAccess.class);
    }
}
