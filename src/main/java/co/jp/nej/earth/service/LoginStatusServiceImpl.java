package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.LoginControlDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.sql.QCtlLogin;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by p-dcv-minhtv on 5/8/2017.
 */
@Service
public class LoginStatusServiceImpl implements LoginStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginStatusServiceImpl.class);

    @Autowired
    private LoginControlDao loginControlDao;

    @Override
    public List<CtlLogin> getAll(Long offset, Long limit, OrderSpecifier<String> orderByColumn)
            throws EarthException {
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        List<CtlLogin> ctlLogins = new ArrayList<CtlLogin>();
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        try {
            OrderSpecifier<String> stringOrderSpecifier = new OrderSpecifier<String>(Order.ASC, qCtlLogin.userId);
            List<OrderSpecifier<?>> orderBys = new ArrayList<>();
            orderBys.add(stringOrderSpecifier);

            ctlLogins = loginControlDao.findAll(Constant.EARTH_WORKSPACE_ID, offset, limit, orderBys, null);

            for(CtlLogin ctlLogin:ctlLogins) {
                ctlLogin.setLoginTime(DateUtil.convertStringToDateFormat(ctlLogin.getLoginTime()));
            }
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return ctlLogins;
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            return ctlLogins;
        }
    }

    @Override
    public List<CtlLogin> getAllByColumn(SearchByColumnsForm searchByColumnsForm, OrderSpecifier<String> orderByColumn)
        throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        SearchColumn searchColumn = new SearchColumn();
        BooleanBuilder condition =
            searchColumn.searchColumns(qCtlLogin, searchByColumnsForm.getValid(),
                searchByColumnsForm.getSearchByColumnForms());
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        List<CtlLogin> ctlLogins = new ArrayList<CtlLogin>();
        try {
            OrderSpecifier<String> stringOrderSpecifier = new OrderSpecifier<String>(Order.ASC, qCtlLogin.userId);
            List<OrderSpecifier<?>> orderBys = new ArrayList<>();
            orderBys.add(stringOrderSpecifier);

            ctlLogins = loginControlDao.search(Constant.EARTH_WORKSPACE_ID,condition,
                searchByColumnsForm.getSkip(), searchByColumnsForm.getLimit(), orderBys, null);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return ctlLogins;
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
            LOG.error(ex.getMessage());
            return ctlLogins;
        }
    }

    @Override
    public boolean deleteList(List<String> sessionIds) throws EarthException {
        QCtlLogin qCtlLogin = QCtlLogin.newInstance();
        TransactionManager transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
        long del = 0L;
        try {
            List<Map<Path<?>, Object>> conditionLogAccesses = new ArrayList<>();
            for (String sessionId : sessionIds) {
                Map<Path<?>, Object> condition = new HashMap<>();
                condition.put(qCtlLogin.sessionId, sessionId);
                conditionLogAccesses.add(condition);
            }
            del = loginControlDao.deleteList(Constant.EARTH_WORKSPACE_ID, conditionLogAccesses);
            transactionManager.getManager().commit(transactionManager.getTxStatus());
        } catch (Exception ex) {
            transactionManager.getManager().rollback(transactionManager.getTxStatus());
        }
        return del == sessionIds.size();

    }
}
