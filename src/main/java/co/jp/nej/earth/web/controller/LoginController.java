package co.jp.nej.earth.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.constant.Constant.View;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.util.ViewUtil;
import co.jp.nej.earth.web.form.LoginForm;

/**
 * Created by minhtv on 3/21/2017. Login System
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private ValidatorUtil validationUtil;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model, HttpServletRequest request) {
        MgrUser mgrUser = new MgrUser();
        model.addAttribute("mgrUser", mgrUser);
        return ViewUtil.requiredLoginView(View.HOME, request.getSession());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@Valid @ModelAttribute("LoginForm") LoginForm loginForm, BindingResult result,
            Model model, HttpServletRequest request) {
        try {
            if (!validationUtil.validate(model, result)) {
                return View.LOGIN;
            }

            List<Message> messages = userService.login(loginForm.getUserId(), loginForm.getPassword(),
                    request.getSession());
            if (messages != null && messages.size() > 0) {
                model.addAttribute(Session.MESSAGES, messages);
                return View.LOGIN;
            } else {
                String lastPath = (String) request.getSession().getAttribute(Session.LAST_PATH);
                return EStringUtil.isEmpty(lastPath) ? View.HOME : lastPath;
            }
        } catch (EarthException e) {
            model.addAttribute(Session.MESSAGES, "Error occured!");
        }

        return View.LOGIN;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@ModelAttribute("User") MgrUser mgrUser, Model model, HttpServletRequest request) {
        try {
            userService.logout(request.getSession());
        } catch (EarthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "login/login";
    }
}
