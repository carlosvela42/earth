package co.jp.nej.earth.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.model.constant.Constant.View;

@Controller
@RequestMapping("/imageviewer")
public class ImageViewerController extends BaseController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String imageviewer(HttpServletRequest request) {
        return View.IMAGE_VIEWER;
    }

    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String template(HttpServletRequest request) {
        return View.IMAGE_VIEWER_TEMPLATE;
    }
}
