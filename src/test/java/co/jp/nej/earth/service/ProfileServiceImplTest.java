package co.jp.nej.earth.service;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProfileServiceImplTest extends BaseTest {

    @Autowired
    ProfileService profileService;
    @Test
    public void getAllTest() {
        List<MgrProfile> mgrProfiles = profileService.getAll();
        Assert.assertTrue(mgrProfiles != null && mgrProfiles.size() > 0);
    }

    @Test
    public void getProfilesByUserIdTest(){
//        List<MgrProfile> mgrProfiles = profileService.get();
//        Assert.assertTrue(mgrProfiles != null && mgrProfiles.size() > 0);
    }
}
