package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.util.EMessageResource;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    protected EMessageResource messageSource;

    protected String redirectToList() {
        return  "redirect:";
    }

    protected String redirectToList(String url) {
        return  "redirect:/" + url;
    }
}
