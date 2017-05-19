package co.jp.nej.earth.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.enums.Type;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.util.ConversionUtil;
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

    @RequestMapping(value = "/dataMenu", method = RequestMethod.GET)
    public String displayDataMenu(Model model) throws EarthException {

        List<String> templateTypes = TemplateType.getTempateTypes();
        model.addAttribute("templateTypes", templateTypes);
        return "template/dataMenu";
    }

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(String templateType, Model model, HttpServletRequest request) throws EarthException {
        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        List<MgrWorkspace> mgrWorkspaces = new ArrayList<>();
        HttpSession session = request.getSession();
        String workspaceId = (String) session.getAttribute("workspaceId");
        mgrWorkspaces = ConversionUtil.castList(session.getAttribute(Session.WORKSPACES), MgrWorkspace.class);
        if (EStringUtil.isEmpty(workspaceId)) {
            session.setAttribute("templateType", templateType);
            model.addAttribute("mgrTemplates", mgrTemplates);
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        } else {
            mgrTemplates = templateService.getTemplateByType(workspaceId,
                    session.getAttribute("templateType").toString());
            model.addAttribute("mgrTemplates", mgrTemplates);
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        }
        return "template/templateList";
    }

    @RequestMapping(value = "/switchWorkspace", method = RequestMethod.POST)
    public String switchWorkspace(@ModelAttribute("workspaceId") String workspaceId, Model model,
            HttpServletRequest request) throws EarthException {
        List<MgrTemplate> mgrTemplates = new ArrayList<>();
        List<MgrWorkspace> mgrWorkspaces = new ArrayList<>();
        if (!(EStringUtil.isEmpty(workspaceId))) {
            HttpSession session = request.getSession();
            session.setAttribute("workspaceId", workspaceId);
            String templateType = session.getAttribute("templateType").toString();
            mgrWorkspaces = ConversionUtil.castList(session.getAttribute(Session.WORKSPACES), MgrWorkspace.class);
            mgrTemplates = templateService.getTemplateByType(workspaceId, templateType);
            model.addAttribute("mgrTemplates", mgrTemplates);
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        }
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
        mgrTemplates = templateService.getAllByWorkspace(workspaceId);
        if (mgrTemplates.size() > 0) {
            for (MgrTemplate temp : mgrTemplates) {
                templateIds.add(temp.getTemplateId());
            }
            mgrTemplate.setTemplateId(String.valueOf(Integer.parseInt(Collections.max(templateIds)) + 1));
        } else {
            mgrTemplate.setTemplateId(String.valueOf(1));
        }
        mgrTemplate.setTemplateTableName("TMP_" + templateType.substring(0, 1) + mgrTemplate.getTemplateId());
        mgrTemplate.setTemplateFields(fields);
        List<Type> fieldTypes = Type.getFieldTypes();
        model.addAttribute("mgrTemplate", mgrTemplate);
        model.addAttribute("fieldTypes", fieldTypes);
        return "template/addTemplate";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("mgrTemplate") MgrTemplate mgrTemplate, Model model,
            HttpServletRequest request) throws EarthException {
        String result = EStringUtil.EMPTY;
        List<Message> messages = new ArrayList<>();
        HttpSession session = request.getSession();
        String workspaceId = session.getAttribute("workspaceId").toString();
        if (!(EStringUtil.isEmpty(mgrTemplate) && EStringUtil.isEmpty(workspaceId))) {
            mgrTemplate.setTemplateType(session.getAttribute("templateType").toString());
            mgrTemplate.setWorkspaceId(workspaceId);

            messages = templateService.checkExistsTemplate(mgrTemplate);
            if (!(EStringUtil.isEmpty(messages)) && messages.size() > 0) {
                List<Type> fieldTypes = Type.getFieldTypes();
                model.addAttribute("messages", messages);
                model.addAttribute("mgrWorkspaceConnect", mgrTemplate);
                model.addAttribute("fieldTypes", fieldTypes);
                result = "template/addTemplate";
            } else {
                boolean success = templateService.insertOne(workspaceId, mgrTemplate);
                if (success) {
                    result = "redirect:showList";
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.POST)
    public String showDetail(@ModelAttribute("templateIds") String templateId, Model model, HttpServletRequest request)
            throws EarthException, JsonParseException, JsonMappingException, IOException {
        MgrTemplate mgrTemplate = new MgrTemplate();
        TemplateKey templateKey = new TemplateKey();
        HttpSession session = request.getSession();
        String workspaceId = session.getAttribute("workspaceId").toString();
        if (!(EStringUtil.isEmpty(templateId) && EStringUtil.isEmpty(workspaceId))) {
            templateKey.setTemplateId(templateId);
            templateKey.setWorkspaceId(workspaceId);
            mgrTemplate = templateService.getById(templateKey);
            if (!(EStringUtil.isEmpty(mgrTemplate))) {
                List<Field> fields = new ObjectMapper().readValue(mgrTemplate.getTemplateField(),
                        new TypeReference<List<Field>>() {
                        });
                mgrTemplate.setTemplateFields(fields);
                model.addAttribute("mgrTemplate", mgrTemplate);
            }
        }
        return "template/editTemplate";
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("mgrTemplate") MgrTemplate mgrTemplate, Model model,
            HttpServletRequest request) throws EarthException {
        String result = EStringUtil.EMPTY;
        List<Message> messages = new ArrayList<>();
        HttpSession session = request.getSession();
        String workspaceId = session.getAttribute("workspaceId").toString();
        if ((mgrTemplate != null && !EStringUtil.isEmpty(workspaceId))) {
            mgrTemplate.setTemplateType(session.getAttribute("templateType").toString());
            messages = templateService.checkExistsTemplate(mgrTemplate);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                model.addAttribute("mgrWorkspaceConnect", mgrTemplate);
                result = "template/editTemplate";
            } else {
                boolean success = templateService.updateOne(workspaceId, mgrTemplate);
                if (success) {
                    result = "redirect:showList";
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("templateIds") String templateIds, Model model, HttpServletRequest request)
            throws EarthException {
        String result = EStringUtil.EMPTY;
        HttpSession session = request.getSession();
        String workspaceId = session.getAttribute("workspaceId").toString();
        if (templateIds != null) {
            List<String> templateIdList = Arrays.asList(templateIds.split("\\s*,\\s*"));
            templateService.deleteTemplates(templateIdList, workspaceId);
            return "redirect:showList";
        }
        return result;
    }

}
