package co.jp.nej.earth.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.UserService;

/**
 * Created by minhtv on 3/21/2017.
 * Login System
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model) {
        MgrUser mgrUser = new MgrUser();
        model.addAttribute("mgrUser", mgrUser);
        return "login/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@ModelAttribute("User") MgrUser mgrUser, Model model, HttpServletRequest request) {
        String result = "";
        try {
            List<Message> messages = userService.login(mgrUser.getUserId(), mgrUser.getPassword(),
                    request.getSession());
            model.addAttribute("MgrUser", mgrUser);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                result = "login/login";
            } else {
                result = "home/home";
            }
        } catch (EarthException e) {
            model.addAttribute("messages", "Error occured!");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@ModelAttribute("User") MgrUser mgrUser, Model model, HttpServletRequest request) {
        try {
            boolean logout = userService.logout(request.getSession());
        } catch (EarthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "login/login";
    }
}
