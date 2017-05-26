package co.jp.nej.earth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.dao.MstSystemDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.AgentBatch;
import co.jp.nej.earth.model.entity.MstSystem;
import co.jp.nej.earth.model.ws.RestResponse;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Service
public class SystemConfigurationServiceImpl extends BaseService implements SystemConfigurationService {

    @Autowired
    private MstSystemDao mstSystemDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateSystemConfig() throws EarthException {
        return (int) this.executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            try {
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
                return (int) mstSystemDao.updateMstSystem(AgentBatch.OPERATION_DATE, AgentBatch.CURRENT_DATE,
                        formatter.format(configDate.getTime()));
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }

    @Override
    public RestResponse updateSystemConfig(String inputDate) throws EarthException {
        return (RestResponse) this.executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            try {
                RestResponse respone = new RestResponse();
                // update system date to system config
                respone.setResult(mstSystemDao.updateMstSystem(AgentBatch.OPERATION_DATE, AgentBatch.CURRENT_DATE,
                        inputDate) > 0);
                return respone;
            } catch (Exception e) {
                throw new EarthException(e);
            }
        });
    }
}