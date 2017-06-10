package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant.LicenseHistory;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.form.SearchForm;
import co.jp.nej.earth.service.LicenseHistoryService;

@Controller
@RequestMapping("/licenseHistory")
public class LicenseHistoryController extends BaseController {

    @Autowired
    private LicenseHistoryService licenseHistoryService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String initScreen(SearchForm searchForm, Model model) throws EarthException {
        List<StrCal> strCals = new ArrayList<StrCal>();
        strCals = licenseHistoryService.search(searchForm.getLimit(), searchForm.getSkip());
        model.addAttribute("strCals", strCals);
        model.addAttribute("searchForm", searchForm);
        return LicenseHistory.LICENSEHISTORY;
    }
}