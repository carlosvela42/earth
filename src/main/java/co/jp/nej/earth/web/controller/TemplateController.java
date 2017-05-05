package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.constant.Constant.Template;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.util.EStringUtil;

/**
 * @author longlt
 *
 */

@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/dataMenu", method = RequestMethod.GET)
    public String displayDataMenu(Model model) throws EarthException {

        List<String> templateTypes = TemplateType.getTempateTypes();
        model.addAttribute("templateTypes", templateTypes);
        return "template/dataMenu";
    }

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(String templateType, Model model, HttpServletRequest request) throws EarthException {
        HttpSession session = request.getSession();
        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        List<MgrWorkspace> mgrWorkspaces = new ArrayList<>();
        String workspaceId = (String) session.getAttribute("workspaceId");
        if (StringUtils.isEmpty(workspaceId)) {
            session.setAttribute("templateType", templateType);
            mgrWorkspaces = (List<MgrWorkspace>) session.getAttribute("mgrWorkspaces");
            model.addAttribute("mgrTemplates", mgrTemplates);
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        } else {
            String type = session.getAttribute("templateType").toString();
            mgrTemplates = templateService.getTemplateByType(workspaceId, type);
            model.addAttribute("mgrTemplates", mgrTemplates);
        }
        return "template/templateList";
    }

    @RequestMapping(value = "/switchWorkspace", method = RequestMethod.POST)
    public String switchWorkspace(@ModelAttribute("workspaceId") String workspaceId, Model model,
            HttpServletRequest request) throws EarthException {

        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        HttpSession session = request.getSession();
        session.setAttribute("workspaceId", workspaceId);
        String templateType = session.getAttribute("templateType").toString();
        mgrTemplates = templateService.getTemplateByType(workspaceId, templateType);
        model.addAttribute("mgrTemplates", mgrTemplates);

        return "template/templateList";
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model, HttpServletRequest request) throws EarthException {
        MgrTemplate mgrTemplate = new MgrTemplate();
        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        List<String> templateIds = new ArrayList<>();
        HttpSession session = request.getSession();
        String templateType = session.getAttribute("templateType").toString();
        String workspaceId = session.getAttribute("workspaceId").toString();
        mgrTemplates = templateService.getTemplateByType(workspaceId, templateType);
        for (MgrTemplate temp : mgrTemplates) {
            templateIds.add(temp.getTemplateId());
        }
        int max = Integer.parseInt(Collections.max(templateIds)) + 1;
        mgrTemplate.setTemplateId(String.valueOf(max));
        model.addAttribute("mgrTemplate", mgrTemplate);
        model.addAttribute("fields", fields);
        return "template/addNewTemplate";
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("templateIds") String templateIds, Model model,
            HttpServletRequest request) {
        String result = EStringUtil.EMPTY;
        boolean status;
        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        HttpSession session = request.getSession();
        String workspaceId = session.getAttribute("workspaceId").toString();
        String templateType = session.getAttribute("templateType").toString();
        List<String> templateIdList = Arrays.asList(templateIds.split("\\s*,\\s*"));
        try {
            status = templateService.deleteTemplates(templateIdList, workspaceId);
            session.setAttribute("templateType", templateType);
            if (!status) {
                Message message = new Message(Template.NOT_DELETE,
                        messageSource.getMessage("E0022", new String[] { "ID" }, Locale.ENGLISH));
                messages.add(message);
                model.addAttribute(Session.MESSAGES, messages);
            }
            result = "redirect:showList";
        } catch (EarthException ex) {
            result = ex.getMessage();
        }
        return result;
    }
}
