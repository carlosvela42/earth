package co.jp.nej.earth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.MstSystemDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.MstSystem;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    @Autowired
    private MstSystemDao mstSystemDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateSystemConfig() throws EarthException {
        // get system config from db
        MstSystem mstSystem = mstSystemDao.getMstSystemBySectionAndVariable(AgentBatch.OPERATION_DATE,
                AgentBatch.CURRENT_DATE);
        SimpleDateFormat formatter = new SimpleDateFormat(AgentBatch.DATE_FORMAT);
        Calendar configDate = Calendar.getInstance();
        try {
            // set value of configDate by system date
            configDate.setTime(formatter.parse(mstSystem.getConfigValue()));
        } catch (ParseException e) {
            throw new EarthException(e.getMessage());
        }
        // add systemdate to 1 day
        configDate.add(Calendar.DATE, 1);
        // update system date to system config
        return (int) mstSystemDao.updateMstSystem(AgentBatch.OPERATION_DATE,
                AgentBatch.CURRENT_DATE, formatter.format(configDate.getTime()));
    }

}