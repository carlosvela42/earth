/*package co.jp.nej.earth.service;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
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

*//**
 * Test UserService.
 *//*
public class UserServiceImplTest extends BaseTest {

    MgrUser mgrUser = new MgrUser();
    MgrUser mgrUserUdpate = new MgrUser();
    List<String> userIds = new ArrayList<String>();


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
            mgrUserUdpate.setName("Administrator");
            mgrUserUdpate.setChangePassword(false);
            userIds.add(mgrUser.getUserId());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assert.assertTrue(1 < 0);
        }

    }

    @Test
    public void getAllTest() {
        List<MgrUser> mgrUsers = null;
        try {
            mgrUsers = userService.getAll();
            Assert.assertTrue(mgrUsers != null && mgrUsers.size() > 0);
        } catch (EarthException ex) {
            Assert.assertTrue(mgrUsers == null);
        }
    }

    @Test
    public void getByIdTest() {
        Map<String, Object> detail=null;
        try {
            detail = userService.getDetail("admin");
            MgrUser mgrUser = (MgrUser) detail.get("mgrUser");
            List<MgrProfile> mgrProfiles = (List<MgrProfile>) detail.get("mgrProfiles");
            Assert.assertTrue(mgrUser != null && mgrProfiles.size() > 0);
        } catch (EarthException ex) {
            System.out.println(ex.getMessage());
            Assert.assertTrue(detail==null);
        }
    }

    @Test
    public void insertOneTest() {
        boolean insertUser = false;
        try {
            List<Message> messages = userService.validate(mgrUser, true);
            Assert.assertFalse(messages != null && messages.size() > 0);
            insertUser = userService.insertOne(mgrUser);
            Assert.assertTrue(insertUser);
        } catch (EarthException ex) {
            System.out.println(ex.getMessage());
            Assert.assertFalse(insertUser);
        }
    }

    @Test
    public void updateOneTest() {
        boolean updateUser = false;
        try {
            List<Message> messages = userService.validate(mgrUser, false);
            Assert.assertFalse(messages != null && messages.size() > 0);
            updateUser = userService.updateOne(mgrUserUdpate);
            Assert.assertTrue(updateUser);

        } catch (EarthException ex) {
            System.out.println(ex.getMessage());
            Assert.assertFalse(updateUser);
        }
    }

    @Test
    public void deleteListTest() {
        boolean deleteUser=false;
        try {
            deleteUser = userService.deleteList(userIds);
            Assert.assertTrue(deleteUser);
        } catch (EarthException e) {
            e.printStackTrace();
            Assert.assertFalse(deleteUser);
        }
    }

}
*/