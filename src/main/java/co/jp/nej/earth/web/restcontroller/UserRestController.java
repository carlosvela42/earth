package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.ws.LoginResponse;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.EStringUtil;

@RestController
@RequestMapping("/WS")
public class UserRestController extends BaseRestController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public LoginResponse login(String userId, String password, HttpServletRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        List<Message> messages;
        boolean isSuccess = false;
        try {
            messages = userService.login(userId, password, request.getSession(), 1);
            if (messages != null && messages.size() > 0) {
                isSuccess = false;
                loginResponse.setMessage(messages.get(0).getContent());
            } else {
                isSuccess = true;
                loginResponse.setJsessionId(request.getSession().getId());
            }
        } catch (EarthException e) {
            isSuccess = false;
            loginResponse.setMessage(e.getMessage());
        }
        loginResponse.setResult(isSuccess);
        return loginResponse;
    }

    @RequestMapping(value = "/logout")
    public RestResponse logout(
            @RequestParam(value = "sessionId", required = true, defaultValue = EStringUtil.EMPTY) String sessionId,
            HttpServletRequest request) throws JsonProcessingException {

        return getRestResponse(sessionId, null, () -> {
            RestResponse logoutResponse = new RestResponse();
            try {
                HttpSession session = EarthSessionManager.find(sessionId);
                logoutResponse.setResult(userService.logout(session));
            } catch (EarthException e) {
                logoutResponse.setData(e.getMessage());
            }
            return logoutResponse;
        });
    }
}