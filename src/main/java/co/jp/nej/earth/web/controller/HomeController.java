package co.jp.nej.earth.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import co.jp.nej.earth.model.constant.Constant.View;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        return View.HOME;
    }
}
