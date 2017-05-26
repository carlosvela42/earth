package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.util.ConversionUtil;

@Service
public class LicenseHistoryServiceImpl extends BaseService implements LicenseHistoryService{

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Override
    public List<StrCal> search(String fromTime, String toTime) throws EarthException {
        return ConversionUtil.castList(executeTransaction(Constant.EARTH_WORKSPACE_ID, () -> {
            return licenseHistoryDao.search(fromTime, toTime);
        }), StrCal.class);
    }
}
