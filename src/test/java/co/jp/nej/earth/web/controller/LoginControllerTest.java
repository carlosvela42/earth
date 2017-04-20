//package co.jp.nej.earth.web.controller;
//
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpSession;
//
//import co.jp.nej.earth.BaseTest;
//import co.jp.nej.earth.exception.EarthException;
//import co.jp.nej.earth.model.Message;
//import co.jp.nej.earth.service.UserService;
//
//public class LoginControllerTest extends BaseTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private MockHttpSession session;
//
//    @Test
//    public void testLogin() throws EarthException {
//        List<Message> listMessage = userService.login("admin", "123456", session);
//        Assert.assertFalse(listMessage != null && listMessage.size() > 0);
//        // ((NTEventLogAppender)
//        // LogManager.getRootLogger().getAppender("ntAppender")).setSource("thuynd
//        // come here");
//        // LOG.info(((NTEventLogAppender)
//        // LogManager.getRootLogger().getAppender("ntAppender")).getSource());
//        // LogManager.getRootLogger().addAppender(new
//        // NTEventLogAppender("thuynd"));
//    }
//}