package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.sql.QStrCal;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LicenseHistoryServiceImpl extends BaseService implements LicenseHistoryService {

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Override
    public List<StrCal> search(Long limit, Long offset) throws EarthException {
        QStrCal qStrCal = QStrCal.newInstance();
        List<OrderSpecifier<?>> orderBys = new ArrayList<>();
        orderBys.add(new OrderSpecifier<String>(Order.DESC, qStrCal.processTime));
        orderBys.add(new OrderSpecifier<String>(Order.ASC, qStrCal.division));
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return licenseHistoryDao.findAll(Constant.EARTH_WORKSPACE_ID, offset, limit, orderBys, null);
        }), StrCal.class);
    }

    @Override
    public List<StrCal> search(SearchByColumnsForm searchByColumnsForm) throws EarthException {
        QStrCal qStrCal = QStrCal.newInstance();
        List<OrderSpecifier<?>> orderBys = new ArrayList<>();
        orderBys.add(new OrderSpecifier<String>(Order.DESC, qStrCal.processTime));
        orderBys.add(new OrderSpecifier<String>(Order.ASC, qStrCal.division));
        SearchColumn searchColumn = new SearchColumn();
        BooleanBuilder condition =
            searchColumn.searchColumns(qStrCal, searchByColumnsForm.getValid(),
                searchByColumnsForm.getSearchByColumnForms());
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return licenseHistoryDao.search(Constant.EARTH_WORKSPACE_ID, condition,searchByColumnsForm.getSkip(),
                searchByColumnsForm.getLimit(), orderBys, null);
        }), StrCal.class);
    }
}
