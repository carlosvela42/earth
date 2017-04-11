package co.jp.nej.earth.service;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileServiceImplTest extends BaseTest {

    @Autowired
    ProfileService profileService;

    @Test
    public void getAllTest() {
        List<MgrProfile> mgrProfiles=null;
        try {
             mgrProfiles = profileService.getAll();
            Assert.assertTrue(mgrProfiles != null && mgrProfiles.size() > 0);
        } catch (EarthException ex) {
            Assert.assertTrue(mgrProfiles==null);
        }
    }

    @Test
    public void getProfilesByUserIdTest() {
        Map<String, Object> detail = new HashMap<String, Object>();
//        try{
////            detail=profileService.getDetail();
//
//        }catch (EarthException ex) {
//            Assert.assertTrue(detail==null);
//        }


//        List<MgrProfile> mgrProfiles = profileService.get();
//        Assert.assertTrue(mgrProfiles != null && mgrProfiles.size() > 0);
    }
}
