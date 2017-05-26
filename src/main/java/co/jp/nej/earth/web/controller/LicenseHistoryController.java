package co.jp.nej.earth.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.LicenseHistory;
import co.jp.nej.earth.model.constant.Constant.ScreenItem;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.StrCal;
import co.jp.nej.earth.service.LicenseHistoryService;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.LicenseHistoryForm;

@Controller
@RequestMapping("/licenseHistory")
public class LicenseHistoryController extends BaseController {

    @Autowired
    private LicenseHistoryService licenseHistoryService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private EMessageResource eMessageResource;

    @RequestMapping(value = "/initLicenseHistoryScreen", method = RequestMethod.GET)
    public String initScreen() {
        return "licenseHistory/licenseHistory";
    }

    @RequestMapping(value = "/searchLicenseHistory", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("LicenseHistoryForm") LicenseHistoryForm licenseHistoryForm,
            BindingResult result, Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        String fromDate = licenseHistoryForm.getFromDate();
        String toDate = licenseHistoryForm.getToDate();
        if (fromDate.compareTo(toDate) == 1) {
            Message message = new Message(null,
                    eMessageResource.get(ErrorCode.E0015, new String[] { ScreenItem.FROM_DATE, ScreenItem.TO_DATE }));
            messages.add(message);
        }
        if (messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            return LicenseHistory.LICENSEHISTORY;
        }
        List<StrCal> strCals = licenseHistoryService.search(licenseHistoryForm.getFromDate().trim(),
                licenseHistoryForm.getToDate().trim());
        model.addAttribute("strCals", strCals);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return LicenseHistory.LICENSEHISTORY;
    }
}