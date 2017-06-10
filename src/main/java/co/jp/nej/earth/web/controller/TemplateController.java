package co.jp.nej.earth.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.ETemplateId;
import co.jp.nej.earth.id.ETemplateTableName;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.enums.Type;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.TemplateForm;

/**
 * @author longlt
 */

@Controller
@RequestMapping("/template")
public class TemplateController extends BaseController {

  public static final String URL = "template";
  @Autowired
  private TemplateService templateService;

  @Autowired
  private WorkspaceService workspaceService;

  @Autowired
  private ValidatorUtil validatorUtil;

  @Autowired
  private ETemplateId eTemplateId;

  @Autowired
  private ETemplateTableName eTemplateTableName;

  @RequestMapping(value = "/dataMenu", method = RequestMethod.GET)
  public String displayDataMenu(Model model, HttpServletRequest request) throws EarthException {
    List<String> templateTypes = TemplateType.getTempateTypes();
    model.addAttribute("templateTypes", templateTypes);
    HttpSession session = request.getSession();
    session.removeAttribute("workspaceId");
    return "template/dataMenu";
  }

  @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
  public String showList(String templateType, Model model, HttpServletRequest request) throws EarthException {
    SessionUtil.loadWorkspaces(workspaceService, model, request);
    String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
    HttpSession session = request.getSession();
    if (EStringUtil.isEmpty(templateType)) {
      templateType = (String) session.getAttribute("templateType");
    } else {
      session.setAttribute("templateType", templateType);
    }
    List<MgrTemplate> mgrTemplates = new ArrayList<>();
    if (!EStringUtil.isEmpty(templateType) && !EStringUtil.isEmpty(workspaceId)) {
      mgrTemplates = templateService.getTemplateByType(workspaceId, templateType);
    }
    model.addAttribute("mgrTemplates", mgrTemplates);
    model.addAttribute("templateType", templateType);

    return "template/templateList";
  }

  @RequestMapping(value = "/switchWorkspace", method = RequestMethod.POST)
  public String switchWorkspace(@ModelAttribute("workspaceId") String workspaceId, Model model,
      HttpServletRequest request) throws EarthException {
    if (EStringUtil.isEmpty(workspaceId)) {
      workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
    }
    HttpSession session = request.getSession();
    List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
    List<MgrTemplate> mgrTemplates = templateService.getTemplateByType(workspaceId,
        session.getAttribute("templateType").toString());
    model.addAttribute("mgrTemplates", mgrTemplates);
    model.addAttribute("mgrWorkspaces", mgrWorkspaces);
    return "template/templateList";
  }

  @RequestMapping(value = "/addNew", method = RequestMethod.GET)
  public String addNew(Model model, HttpServletRequest request) throws EarthException {
    String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
    TemplateForm templateForm = new TemplateForm();
    HttpSession session = request.getSession();
    String templateType = session.getAttribute("templateType").toString();

    templateForm.setTemplateId(eTemplateId.getAutoId(workspaceId));
    templateForm
        .setTemplateTableName(eTemplateTableName.getTemplateTableName(templateType, templateForm.getTemplateId()));
    templateForm.setTemplateFields(new ArrayList<Field>());
    templateForm.setWorkspaceId(workspaceId);

    model.addAttribute("templateForm", templateForm);
    model.addAttribute("fieldTypes", Type.getFieldTypes());
    return "template/addTemplate";
  }

  @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
  public String insertOne(@Valid @ModelAttribute("templateForm") TemplateForm templateForm, BindingResult result,
      Model model, HttpServletRequest request) throws EarthException {
    List<Message> messages = validatorUtil.validate(result);
    if (messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      model.addAttribute("templateForm", templateForm);
      model.addAttribute("fieldTypes", Type.getFieldTypes());
      return "template/addTemplate";
    }
    HttpSession session = request.getSession();
    String workspaceId = templateForm.getWorkspaceId();
    MgrTemplate mgrTemplate = new MgrTemplate();
    mgrTemplate.setTemplateId(templateForm.getTemplateId());
    mgrTemplate.setTemplateName(templateForm.getTemplateName());
    mgrTemplate.setTemplateTableName(templateForm.getTemplateTableName());
    mgrTemplate.setTemplateFields(templateForm.getTemplateFields());
    mgrTemplate.setTemplateType(session.getAttribute("templateType").toString());
    mgrTemplate.setWorkspaceId(workspaceId);
    mgrTemplate.setLastUpdateTime(null);
    MgrWorkspaceConnect mgrWorkspaces = workspaceService.getDetail(workspaceId);
    if (!EStringUtil.isEmpty(mgrTemplate) && !EStringUtil.isEmpty(workspaceId)) {
      messages = templateService.checkExistsTemplate(mgrTemplate, mgrWorkspaces.getDbUser());
      if ((!(EStringUtil.isEmpty(messages)) && messages.size() > 0)) {
        List<Type> fieldTypes = Type.getFieldTypes();
        model.addAttribute("messages", messages);
        model.addAttribute("mgrWorkspaceConnect", mgrTemplate);
        model.addAttribute("fieldTypes", fieldTypes);
        return "template/addTemplate";
      } else {
        templateService.insertOne(workspaceId, mgrTemplate);
      }
    }
    return redirectToList(URL);
  }

  @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
  public String showDetail(@ModelAttribute("templateIds") String templateId, Model model, HttpServletRequest request)
      throws EarthException, JsonParseException, JsonMappingException, IOException {
    TemplateKey templateKey = new TemplateKey();
    String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
    if (!EStringUtil.isEmpty(templateId) && !EStringUtil.isEmpty(workspaceId)) {
      templateKey.setTemplateId(templateId);
      templateKey.setWorkspaceId(workspaceId);
      MgrTemplate mgrTemplate = templateService.getById(templateKey);
      if (!(EStringUtil.isEmpty(mgrTemplate))) {
        TemplateForm templateForm = new TemplateForm();
        List<Field> fields = new ObjectMapper().readValue(mgrTemplate.getTemplateField(),
            new TypeReference<List<Field>>() {
            });
        templateForm.setTemplateFields(fields);
        templateForm.setTemplateId(templateId);
        templateForm.setTemplateName(mgrTemplate.getTemplateName());
        templateForm.setTemplateTableName(mgrTemplate.getTemplateTableName());
        templateForm.setLastUpdateTime(mgrTemplate.getLastUpdateTime());
        model.addAttribute("templateForm", templateForm);
      }
    }
    model.addAttribute("fieldTypes", Type.getFieldTypes());
    return "template/addTemplate";
  }

  /*
   * @RequestMapping(value = "/updateOne", method = RequestMethod.POST) public String
   * updateOne(@ModelAttribute("mgrTemplate") MgrTemplate mgrTemplate, Model model, HttpServletRequest request) throws
   * EarthException { HttpSession session = request.getSession(); String workspaceId =
   * session.getAttribute("workspaceId").toString(); if ((mgrTemplate != null && !EStringUtil.isEmpty(workspaceId))) {
   * mgrTemplate.setTemplateType(session.getAttribute("templateType").toString());
   * mgrTemplate.setWorkspaceId(session.getAttribute("workspaceId").toString()); templateService.updateOne(workspaceId,
   * mgrTemplate); } return redirectToList("template"); }
   */

  @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
  public String updateOne(@Valid @ModelAttribute("templateForm") TemplateForm templateForm, BindingResult result,
      Model model, HttpServletRequest request) throws EarthException {
    HttpSession session = request.getSession();
    String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
    MgrTemplate mgrTemplate = new MgrTemplate();
    List<Message> messages = validatorUtil.validate(result);
    if (messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      model.addAttribute("templateForm", templateForm);
      model.addAttribute("fieldTypes", Type.getFieldTypes());
      return "template/addTemplate";
    }
    mgrTemplate.setTemplateId(templateForm.getTemplateId());
    mgrTemplate.setTemplateName(templateForm.getTemplateName());
    mgrTemplate.setTemplateTableName(templateForm.getTemplateTableName());
    mgrTemplate.setTemplateType(session.getAttribute("templateType").toString());
    mgrTemplate.setTemplateFields(templateForm.getTemplateFields());
    mgrTemplate.setWorkspaceId(workspaceId);
    templateService.updateOne(workspaceId, mgrTemplate);
    return redirectToList(URL);
  }

  @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
  public String deleteList(DeleteListForm form) throws EarthException {
    String workspaceId = form.getWorkspaceId();
    List<String> templateIdList = form.getListIds();
    templateService.deleteTemplates(templateIdList, workspaceId);
    return redirectToList(URL);
  }

  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  public String cancel() {
    return redirectToList(URL);
  }

}
