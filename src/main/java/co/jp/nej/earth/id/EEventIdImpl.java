package co.jp.nej.earth.id;

import co.jp.nej.earth.dao.EventIdDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrEventId;
import co.jp.nej.earth.model.sql.QMgrEventId;
import co.jp.nej.earth.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Path;

@Service
public class EEventIdImpl implements EEventId {

    private static final int ID_LENGHT = 20;
    private static final String FORMAT_COUNT_PAD = "%012d";

    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EEventIdImpl.class);

    @Autowired
    private EventIdDao evenIdDao;

    private static final QMgrEventId qMgrEventId = QMgrEventId.newInstance();

    @Override
    public String getAutoId() throws EarthException {

        TransactionManager transactionManager = null;
        try {
            StringBuilder newId = new StringBuilder(ID_LENGHT);
            transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);

            String updateTime = DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);

            MgrEventId evendIdToday = evenIdDao.getByIssueToday();
            if(evendIdToday!=null) {

                evendIdToday.setCount(evendIdToday.getCount() + 1);
                evendIdToday.setLastUpdateTime(updateTime);

                Map<Path<?>, Object> keys = new HashMap<>();
                keys.put(qMgrEventId.issueDate, evendIdToday.getIssueDate());
                Map<Path<?>, Object> values = new HashMap<>();
                values.put(qMgrEventId.count, evendIdToday.getCount());
                values.put(qMgrEventId.lastUpdateTime, evendIdToday.getLastUpdateTime());

                evenIdDao.update(Constant.EARTH_WORKSPACE_ID,keys, values);

            } else {

                evendIdToday = new MgrEventId();
                evendIdToday.setIssueDate(DateUtil.getCurrentShortDateString());
                evendIdToday.setCount(1);
                evendIdToday.setLastUpdateTime(updateTime);

                evenIdDao.add(evendIdToday);

            }

            newId.append(evendIdToday.getIssueDate().trim());
            newId.append(String.format(FORMAT_COUNT_PAD, evendIdToday.getCount()));

            transactionManager.getManager().commit(transactionManager.getTxStatus());

            return newId.toString();

        } catch (EarthException ex) {
            if (transactionManager != null) {
                transactionManager.getManager().rollback(transactionManager.getTxStatus());
            }

            LOG.error(ex.getMessage());
            return "";

        }

    }

}
