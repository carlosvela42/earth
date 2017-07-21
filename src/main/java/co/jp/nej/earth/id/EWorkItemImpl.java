package co.jp.nej.earth.id;

import co.jp.nej.earth.dao.WorkItemIdDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrWorkItemId;
import co.jp.nej.earth.model.sql.QMgrEventId;
import co.jp.nej.earth.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class EWorkItemImpl
 *
 * @author daopq
 * @version 1.0
 */
@Service
public class EWorkItemImpl implements EWorkItemId {
    private static final int ID_LENGTH = 20;
    private static final int SUB_DATE_FROM = 0;
    private static final int SUB_DATE_TO = 8;
    private static final int FIRST_COUNT = 1;
    private static final String FORMAT_COUNT_PAD = "%012d";

    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(EWorkItemImpl.class);

    @Autowired
    private WorkItemIdDao workItemIdDao;

    private static final QMgrEventId qMgrEventId = QMgrEventId.newInstance();

    /**
     * Get auto id of work item
     *
     * @return
     * @throws EarthException
     */
    @Override
    public String getAutoId() throws EarthException {

        TransactionManager transactionManager = null;
        try {
            transactionManager = new TransactionManager(Constant.EARTH_WORKSPACE_ID);
            String updateTime = DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);
            MgrWorkItemId workItemId = workItemIdDao.getByIssueToday();
            if(workItemId != null) {
                workItemId.setCount(workItemId.getCount() + 1);
                workItemId.setLastUpdateTime(updateTime);
                workItemIdDao.update(workItemId);
            } else {
                workItemId = new MgrWorkItemId();
                workItemId.setIssueDate(DateUtil.getCurrentShortDateString());
                workItemId.setCount(FIRST_COUNT);
                workItemId.setLastUpdateTime(updateTime);
                workItemIdDao.add(workItemId);
            }

            StringBuilder newId = new StringBuilder(ID_LENGTH);
            newId.append(workItemId.getIssueDate().trim().substring(SUB_DATE_FROM, SUB_DATE_TO));
            newId.append(String.format(FORMAT_COUNT_PAD, workItemId.getCount()));

            transactionManager.getManager().commit(transactionManager.getTxStatus());
            return newId.toString();
        } catch (EarthException ex) {
            if (transactionManager != null) {
                transactionManager.getManager().rollback(transactionManager.getTxStatus());
            }
            LOG.error(ex.getMessage());
            throw new EarthException(ex);
        }
    }
}
