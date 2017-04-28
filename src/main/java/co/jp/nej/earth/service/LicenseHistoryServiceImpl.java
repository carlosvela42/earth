package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.LicenseHistoryDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrCal;

@Transactional
@Service
public class LicenseHistoryServiceImpl implements LicenseHistoryService{

    @Autowired
    private LicenseHistoryDao licenseHistoryDao;

    @Override
    public List<StrCal> search(String fromTime, String toTime) throws EarthException {
        return licenseHistoryDao.search(fromTime, toTime);
    }
}
