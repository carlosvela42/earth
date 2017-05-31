package co.jp.nej.earth.web;

import co.jp.nej.earth.util.EMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by cuongtm on 2017/05/29.
 */
@ControllerAdvice(basePackages = {"co.jp.nej.earth.web.controller"})
public class GlobalControllerAdvice {
  @Autowired
  protected EMessageResource messageSource;

  @ModelAttribute
  public void globalAttributes(Model model) {
    model.addAttribute("e", messageSource);
  }
}
