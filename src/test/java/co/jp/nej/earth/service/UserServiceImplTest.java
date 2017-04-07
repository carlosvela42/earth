package co.jp.nej.earth.service;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test UserService.
 */
public class UserServiceImplTest extends BaseTest {

    MgrUser mgrUser = new MgrUser();
    MgrUser mgrUserUdpate = new MgrUser();
    List<String> userIds=new ArrayList<String>();


    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    HttpSession session;

    @Before
    public void before() {
        try {
            mgrUser.setUserId("test");
            mgrUser.setName("test");
            mgrUser.setPassword("Test!2345");
            mgrUser.setConfirmPassword("Test!2345");
            mgrUser.setChangePassword(true);
            mgrUserUdpate.setUserId("admin");
            mgrUserUdpate.setName("Admistrator");
            mgrUserUdpate.setChangePassword(false);
            userIds.add(mgrUser.getUserId());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assert.assertTrue(1 < 0);
        }

    }

    @Test
    public void getAllTest() {
        List<MgrUser> mgrUsers = userService.getAll();
        List<MgrProfile> mgrProfiles=profileService.getAll();
        Assert.assertTrue(mgrUsers != null && mgrUsers.size() > 0);
    }

    @Test
    public void getByIdTest() {
        try {
            Map<String, Object> detial = userService.getDetail("admin");
            MgrUser mgrUser = (MgrUser) detial.get("mgrUser");
            List<MgrProfile> mgrProfiles = (List<MgrProfile>) detial.get("mgrProfiles");
            Assert.assertTrue(mgrUser != null && mgrProfiles.size() > 0);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assert.assertTrue(1 < 0);
        }
    }

    @Test
    public void insertOneTest() {
//        try {
//            List<Message> messages = userService.validate(mgrUser, true);
//            Assert.assertFalse(messages != null && messages.size() > 0);
//            boolean insertUser = userService.insertOne(mgrUser);
//            Assert.assertTrue(insertUser);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            Assert.assertTrue(1 < 0);
//        }
    }

    @Test
    public void udpateOneTest() {
//        try {
//            List<Message> messages = userService.validate(mgrUser, false);
//            Assert.assertFalse(messages != null && messages.size() > 0);
//            boolean updateUser = userService.updateOne(mgrUserUdpate);
//            Assert.assertTrue(updateUser);
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            Assert.assertTrue(1 < 0);
//        }
    }

    @Test
    public void deleteListTest() {
//        try {
//            boolean deleteUser =userService.deleteList(userIds);
//            Assert.assertTrue(deleteUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.assertTrue(1 < 0);
//        }
    }

}
