package co.jp.nej.earth.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.UserInfo;
import co.jp.nej.earth.model.constant.Constant.Session;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index(HttpServletRequest request)
			throws ClassNotFoundException, SchedulerException, FileNotFoundException, EarthException, IOException {

		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute(Session.USER_INFO);
		return userInfo != null ? "home/home" : "login/login";
	}

	@RequestMapping(value = "/imageviewer", method = RequestMethod.GET)
	public String displayWorkspace() {
		return "imageViewer/ImageViewer";
	}

}
