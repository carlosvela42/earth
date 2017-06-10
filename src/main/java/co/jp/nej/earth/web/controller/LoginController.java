package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.Constant.*;
import co.jp.nej.earth.model.entity.*;
import co.jp.nej.earth.model.form.SearchForm;
import co.jp.nej.earth.service.*;
import co.jp.nej.earth.util.*;
import co.jp.nej.earth.web.form.*;
import com.querydsl.core.types.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;

/**
 * Created by minhtv on 3/21/2017. Login System
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private LoginStatusService loginStatusService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model, HttpServletRequest request) {
        if (LoginUtil.isLogin(request.getSession())) {
            return "redirect:/";
        }

        return View.LOGIN;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@Valid @ModelAttribute("LoginForm") LoginForm loginForm, BindingResult result,
            Model model, HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            return View.LOGIN;
        }

        messages = userService.login(loginForm.getUserId(), loginForm.getPassword(), request.getSession(), 0);
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            return View.LOGIN;
        } else {
            String lastPath = (String) request.getSession().getAttribute(Session.LAST_REQUEST_VIEW);
            return EStringUtil.isEmpty(lastPath) ? ("redirect:/") : ("redirect:" + lastPath);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@ModelAttribute("User") MgrUser mgrUser, Model model, HttpServletRequest request)
            throws EarthException {
        userService.logout(request.getSession());
        return "redirect:login";
    }

    @RequestMapping(value = "/loginView", method = RequestMethod.GET)
    public String evidentLog(SearchForm searchForm, Model model, HttpServletRequest request) throws EarthException {
        model.addAttribute("searchForm",searchForm);
        model.addAttribute("ctlLogins", loginStatusService.getAll(searchForm.getSkip(), searchForm.getLimit(), null));
        return "loginStatus/loginStatus";
    }

//    @RequestMapping(value = "/loginViewScreen", method = RequestMethod.GET)
//    public String loginView(Model model, HttpServletRequest request) throws EarthException {
//        Long offset = null;
//        Long limit = null;
//        OrderSpecifier<String> orderByColumn = null;
//        model.addAttribute("ctlLogins", loginStatusService.getAll(offset, limit, orderByColumn));
//        return "loginStatus/loginStatus";
//    }
}
