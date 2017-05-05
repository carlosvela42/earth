//package co.jp.nej.earth.web.controller;
//
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import co.jp.nej.earth.BaseTest;
//import co.jp.nej.earth.exception.EarthException;
//import co.jp.nej.earth.model.entity.StrCal;
//import co.jp.nej.earth.service.LicenseHistoryService;
//import co.jp.nej.earth.service.LicenseHistoryServiceTest;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class LicenseHistoryControllerTest extends BaseTest {
//
//    @Autowired
//    private LicenseHistoryService licenseHistoryService;
//
//    @Autowired
//    private LicenseHistoryServiceTest licenseHistoryServiceTest;
//
//    private static boolean setUpfinished = false;
//
//    private static final int AVAILABLE_LICENSE_COUNT = 20;
//    private static final int USE_LICENSE_COUNT = 20;
//    private static final int TEST_CASE_1_RESULT = 15;
//    private static final int TEST_CASE_2_RESULT = 11;
//    private static final int TEST_CASE_34_RESULT = 8;
//
//    @Before
//    public void setUp() throws EarthException {
//        if (setUpfinished){
//            return;
//        }
//
//        licenseHistoryServiceTest.deleteAll();
//
//        StrCal strCal1 = new StrCal();
//        strCal1.setDivision("division1");
//        strCal1.setProcessTime("2017/04/19 12:33:44.182");
//        strCal1.setProfileId("profile_1");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal1);
//
//        StrCal strCal2 = new StrCal();
//        strCal2.setDivision("division1");
//        strCal2.setProcessTime("2017/04/19 12:33:44.182");
//        strCal2.setProfileId("profile_2");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal2);
//
//        StrCal strCal3 = new StrCal();
//        strCal3.setDivision("division1");
//        strCal3.setProcessTime("2017/04/19 12:33:44.182");
//        strCal3.setProfileId("profile_3");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal3);
//
//        StrCal strCal4 = new StrCal();
//        strCal4.setDivision("division1");
//        strCal4.setProcessTime("2017/04/19 12:33:44.182");
//        strCal4.setProfileId("profile_4");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal4);
//
//        StrCal strCal5 = new StrCal();
//        strCal5.setDivision("division1");
//        strCal5.setProcessTime("2017/04/19 12:33:44.585");
//        strCal5.setProfileId("profile_1");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal5);
//
//        StrCal strCal6 = new StrCal();
//        strCal6.setDivision("division1");
//        strCal6.setProcessTime("2017/04/19 12:33:44.585");
//        strCal6.setProfileId("profile_2");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal6);
//
//        StrCal strCal7 = new StrCal();
//        strCal7.setDivision("division1");
//        strCal7.setProcessTime("2017/04/19 12:33:44.585");
//        strCal7.setProfileId("profile_3");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal7);
//
//        StrCal strCal8 = new StrCal();
//        strCal8.setDivision("division1");
//        strCal8.setProcessTime("2017/04/19 12:33:44.585");
//        strCal8.setProfileId("profile_4");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal8);
//
//        StrCal strCal9 = new StrCal();
//        strCal9.setDivision("division1");
//        strCal9.setProcessTime("2017/04/19 12:33:53.720");
//        strCal9.setProfileId("profile_1");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal9);
//
//        StrCal strCal10 = new StrCal();
//        strCal10.setDivision("division1");
//        strCal10.setProcessTime("2017/04/19 12:33:53.720");
//        strCal10.setProfileId("profile_2");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal10);
//
//        StrCal strCal11 = new StrCal();
//        strCal11.setDivision("division1");
//        strCal11.setProcessTime("2017/04/19 12:33:53.720");
//        strCal11.setProfileId("profile_3");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal11);
//
//        StrCal strCal12 = new StrCal();
//        strCal12.setDivision("division1");
//        strCal12.setProcessTime("2017/04/19 12:33:53.720");
//        strCal12.setProfileId("profile_4");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal12);
//
//        StrCal strCal13 = new StrCal();
//        strCal13.setDivision("division1");
//        strCal13.setProcessTime("2017/04/19 12:33:54.349");
//        strCal13.setProfileId("profile_1");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal13);
//
//        StrCal strCal14 = new StrCal();
//        strCal14.setDivision("division1");
//        strCal14.setProcessTime("2017/04/19 12:33:54.349");
//        strCal14.setProfileId("profile_2");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal14);
//
//        StrCal strCal15 = new StrCal();
//        strCal15.setDivision("division1");
//        strCal15.setProcessTime("2017/04/19 12:33:54.349");
//        strCal15.setProfileId("profile_3");
//        strCal1.setAvailableLicenseCount(AVAILABLE_LICENSE_COUNT);
//        strCal1.setUseLicenseCount(USE_LICENSE_COUNT);
//        licenseHistoryServiceTest.insertStrCals(strCal15);
//
//        setUpfinished = true;
//    }
//
//    @Test
//    public void testSearch1() throws EarthException {
//        List<StrCal> strCals = licenseHistoryService.search("", "");
//        Assert.assertTrue(strCals != null && strCals.size() == TEST_CASE_1_RESULT);
//    }
//
//    @Test
//    public void testSearch2() throws EarthException {
//        List<StrCal> strCals = licenseHistoryService.search("2017/04/19 12:33:44.585", "");
//        Assert.assertTrue(strCals != null && strCals.size() == TEST_CASE_2_RESULT);
//    }
//
//    @Test
//    public void testSearch3() throws EarthException {
//        List<StrCal> strCals = licenseHistoryService.search("", "2017/04/19 12:33:44.585");
//        Assert.assertTrue(strCals != null && strCals.size() == TEST_CASE_34_RESULT);
//    }
//
//    @Test
//    public void testSearch4() throws EarthException {
//        List<StrCal> strCals = licenseHistoryService.search("2017/04/19 12:33:44.585", "2017/04/19 12:33:53.720");
//        Assert.assertTrue(strCals != null && strCals.size() == TEST_CASE_34_RESULT);
//    }
//
//    @Test
//    public void testXDelete() throws EarthException{
//        licenseHistoryServiceTest.deleteAll();
//    }
//}