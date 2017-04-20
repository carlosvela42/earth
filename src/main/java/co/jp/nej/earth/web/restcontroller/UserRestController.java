package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
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
        boolean isSuccess = false;
        try {
            messages = userService.login(userId, password, request.getSession());
            if (messages != null && messages.size() > 0) {
                isSuccess = true;
                loginResponse.setJsessionId(request.getSession().getId());
            } else {
                isSuccess = false;
                loginResponse.setMessage(messages.get(0).getContent());
            }
        } catch (EarthException e) {
            isSuccess = false;
            loginResponse.setMessage(e.getMessage());
        }
        loginResponse.setResult(isSuccess);
        return loginResponse;
    }

    @RequestMapping(value = "/logout")
    public LogoutResponse logout(
            @RequestParam(value = "sessionId", required = true, defaultValue = "") String sessionId,
            HttpServletRequest request) {
        LogoutResponse logoutResponse = new LogoutResponse();
        HttpSession session = EarthSessionManager.find(sessionId);
        if (session == null) {
            logoutResponse.setMessage("Can not load session!");
        }
        boolean isSucess = false;
        try {
            isSucess = userService.logout(session);
        } catch (EarthException e) {
            isSucess = false;
            logoutResponse.setMessage(e.getMessage());
        }

        logoutResponse.setResult(isSucess);
        return logoutResponse;
    }
}