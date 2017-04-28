package co.jp.nej.earth.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.service.LicenseHistoryService;

@Controller
@RequestMapping("/licenseHistory")
public class LicenseHistoryController {

    @Autowired
    private LicenseHistoryService licenseHistoryService;

    @RequestMapping(value = "/initLicenseHistoryScreen", method = RequestMethod.GET)
    public String initScreen() {
        return "licenseHistory/licenseHistory";
    }

    @RequestMapping(value = "/searchLicenseHistory", method = RequestMethod.POST)
    public String submit(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
            Model model) throws EarthException {
            List<StrCal> strCals = licenseHistoryService.search(fromDate.trim(), toDate.trim());
            model.addAttribute("strCals", strCals);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
        return "licenseHistory/licenseHistory";
    }
}