package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.model.constant.Constant.LicenseHistory;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.service.LicenseHistoryService;
import co.jp.nej.earth.web.form.LicenseHistoryForm;

@Controller
@RequestMapping("/licenseHistory")
public class LicenseHistoryController extends BaseController {

    @Autowired
    private LicenseHistoryService licenseHistoryService;

    @RequestMapping(value = "/initLicenseHistoryScreen", method = RequestMethod.GET)
    public String initScreen() {
        return "licenseHistory/licenseHistory";
    }

    @RequestMapping(value = "/searchLicenseHistory", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("LicenseHistoryForm") LicenseHistoryForm licenseHistoryForm,
            BindingResult result, Model model) {
        List<StrCal> strCals = new ArrayList<StrCal>();
        try {
            Long skip = licenseHistoryForm.getSkip();
            Long limit = licenseHistoryForm.getLimit();
            strCals = licenseHistoryService.search(skip, limit);
            model.addAttribute("strCals", strCals);
            model.addAttribute("skip", skip);
            model.addAttribute("limit", limit);
        } catch (Exception e) {
            String message = "Error Occurred!";
            model.addAttribute("message", message);
        }
        return LicenseHistory.LICENSEHISTORY;
    }
}