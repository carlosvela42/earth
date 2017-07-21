package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.model.constant.Constant.View;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {
    @GetMapping("/")
    public String index(Model model,HttpServletRequest request) {
        model.addAttribute("sessionId", request.getRequestedSessionId());
        return View.HOME;
    }
}
