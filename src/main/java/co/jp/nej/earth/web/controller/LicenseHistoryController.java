package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.LicenseHistory;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.model.enums.SearchOperator;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.form.SearchForm;
import co.jp.nej.earth.service.LicenseHistoryService;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/licenseHistory")
public class LicenseHistoryController extends BaseController {

    @Autowired
    private LicenseHistoryService licenseHistoryService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String initScreen(SearchForm searchForm, Model model, HttpServletRequest request) throws EarthException {
        List<StrCal> strCals = new ArrayList<StrCal>();
        strCals = licenseHistoryService.search(searchForm.getLimit(), searchForm.getSkip());
        request.getSession().removeAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM);
        model.addAttribute("strCals", strCals);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("templateTypes", TemplateType.getTempateTypes());
        model.addAttribute("searchOperators", SearchOperator.values());
        return LicenseHistory.LICENSEHISTORY;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    @ResponseBody
    public List<StrCal> initScreenByColumn(SearchByColumnsForm searchByColumnsForm,
            HttpServletRequest request) throws EarthException {
        List<StrCal> strCals = licenseHistoryService.search(searchByColumnsForm);
        request.getSession().setAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM, searchByColumnsForm);
        return strCals;
    }
}