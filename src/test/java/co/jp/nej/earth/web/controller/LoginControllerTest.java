package co.jp.nej.earth.web.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.service.UserService;

public class LoginControllerTest extends BaseTest{

    @Autowired
    UserService userService;
    
    @Autowired
    MockHttpSession session;
    
    @Test
    public void testLogin() throws EarthException{
        List<Message> listMessage = userService.login("admin", "123456", session);
            Assert.assertFalse(listMessage != null && listMessage.size() > 0);
    }
}