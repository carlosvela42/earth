//package co.jp.nej.earth.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import co.jp.nej.earth.dao.LicenseHistoryDaoTest;
//import co.jp.nej.earth.exception.EarthException;
//import co.jp.nej.earth.model.constant.Constant;
//import co.jp.nej.earth.model.entity.StrCal;
//
//@Transactional
//@Service
//public class LicenseHistoryServiceImplTest implements LicenseHistoryServiceTest{
//
//    @Autowired
//    private LicenseHistoryDaoTest licenseHistoryDaoTest;
//
//    @Override
//    public boolean insertStrCals(StrCal strCal) throws EarthException {
//        return licenseHistoryDaoTest.add(Constant.EARTH_WORKSPACE_ID, strCal) > 0;
//    }
//
//    @Override
//    public boolean deleteAll() throws EarthException {
//        return licenseHistoryDaoTest.deleteAll();
//    }
//}
