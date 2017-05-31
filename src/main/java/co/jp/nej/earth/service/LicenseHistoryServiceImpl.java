package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.sql.QStrCal;
import co.jp.nej.earth.util.ConversionUtil;

@Service
public class LicenseHistoryServiceImpl extends BaseService implements LicenseHistoryService{

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Override
    public List<StrCal> search(Long limit, Long offset) throws EarthException {
        QStrCal qStrCal = QStrCal.newInstance();
        OrderSpecifier<String> stringOrderSpecifier = new OrderSpecifier<String>(Order.ASC, qStrCal.processTime);
        List<OrderSpecifier<?>> orderBys = new ArrayList<>();
        orderBys.add(stringOrderSpecifier);
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return licenseHistoryDao.findAll(Constant.EARTH_WORKSPACE_ID, offset, limit, orderBys, null);
        }), StrCal.class);
    }
}
