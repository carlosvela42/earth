package co.jp.nej.earth.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant.View;
import co.jp.nej.earth.util.ViewUtil;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpServletRequest request)
            throws ClassNotFoundException, SchedulerException, FileNotFoundException, EarthException, IOException {

        return ViewUtil.requiredLoginView(View.HOME, request.getSession());
    }

    @RequestMapping(value = "/imageviewer", method = RequestMethod.GET)
    public String displayWorkspace(HttpServletRequest request) {
        return ViewUtil.requiredLoginView(View.IMAGE_VIEWER, request.getSession());
    }
}
