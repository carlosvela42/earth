package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.ws.LoginResponse;
import co.jp.nej.earth.model.ws.LogoutResponse;
import co.jp.nej.earth.service.UserService;

@RestController
@RequestMapping("/WS")
public class UserRestController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public LoginResponse login(String userId, String password, HttpServletRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        List<Message> messages;
        try {
            messages = userService.login(userId, password, request.getSession());
            if (messages != null && messages.size() > 0) {
                loginResponse.setResult(true);
                loginResponse.setJsessionId(request.getSession().getId());
            } else {
                loginResponse.setResult(false);
                loginResponse.setMessage(messages.get(0).getContent());
            }
        } catch (EarthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return loginResponse;
    }

    @RequestMapping(value = "/logout")
    public LogoutResponse logout(
            @RequestParam(value = "jsessionId", required = true, defaultValue = "") String jsessionId,
            HttpServletRequest request) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setResult(true);
        return logoutResponse;
    }
}