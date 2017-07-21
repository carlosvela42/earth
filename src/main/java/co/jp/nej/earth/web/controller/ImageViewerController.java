package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.model.constant.Constant.View;
import co.jp.nej.earth.web.form.ImageViewerForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @RequestMapping(value = "svgImageViewer", method = RequestMethod.GET)
    public String svgImageviewer(ImageViewerForm iv,Model model, HttpServletRequest request) {
        // Get accessible template by login user
        HttpSession session = request.getSession();
        iv.setSessionId(session.getId());
        iv.setWorkspaceId(session.getAttribute("workspaceId").toString());
        model.addAttribute("imageViewer", iv);
        return "imageViewer/svgImageViewer";
    }

    @RequestMapping(value = "specRunner", method = RequestMethod.GET)
    public String specRunner(HttpServletRequest request) {
        return "imageViewer/SpecRunner";
    }
}
