package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EWorkspaceId;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.WorkspaceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author longlt
 *
 */

@Controller
@RequestMapping("/workspace")
public class WorkspaceController extends BaseController {

  @Autowired
  private WorkspaceService workspaceService;

  @Autowired
  private ValidatorUtil validatorUtil;

  @Autowired
  private EWorkspaceId eWorkspaceId;

  @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
  public String displayWorkspace(Model model, HttpServletRequest request) throws EarthException {
    List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
    model.addAttribute("mgrWorkspaces", mgrWorkspaces);
    return "workspace/workspaceList";
  }

  @RequestMapping(value = "/addNew", method = RequestMethod.GET)
  public String addNew(Model model, HttpServletRequest request) throws EarthException {
    WorkspaceForm workspaceForm = new WorkspaceForm();
    // TODO:
    workspaceForm.setWorkspaceId(eWorkspaceId.getAutoId());
    model.addAttribute("workspaceForm", workspaceForm);
    return "workspace/addNewWorkspace";
  }

  @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
  public String insertOne(@Valid @ModelAttribute("workspaceForm") WorkspaceForm workspaceForm, BindingResult result,
      HttpServletRequest request, Model model) throws EarthException {
    List<Message> messages = validatorUtil.validate(result);
    if (messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      workspaceForm.setWorkspaceId(eWorkspaceId.getAutoId());
      model.addAttribute("workspaceForm", workspaceForm);
      return "workspace/addNewWorkspace";
    }
    MgrWorkspaceConnect mgrWorkspaceConnect = new MgrWorkspaceConnect();
    mgrWorkspaceConnect.setWorkspaceId(workspaceForm.getWorkspaceId());
    mgrWorkspaceConnect.setSchemaName(workspaceForm.getSchemaName());
    mgrWorkspaceConnect.setDbUser(workspaceForm.getDbUser());
    mgrWorkspaceConnect.setDbPassword(workspaceForm.getDbPassword());
    mgrWorkspaceConnect.setOwner(workspaceForm.getOwner());
    mgrWorkspaceConnect.setDbServer(workspaceForm.getDbServer());
    messages = workspaceService.validateInsert(mgrWorkspaceConnect);
    if (messages != null && messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);
      return "workspace/addNewWorkspace";
    } else {
      workspaceService.insertOne(mgrWorkspaceConnect);
      return redirectToList("workspace");
    }
  }

  @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
  public String updateOne(@Valid @ModelAttribute("workspaceForm") WorkspaceForm workspaceForm, BindingResult result,
      Model model) throws EarthException {
    List<Message> messages = validatorUtil.validate(result);
    if (messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      return "workspace/editWorkspace";
    }
    MgrWorkspaceConnect mgrWorkspaceConnect = new MgrWorkspaceConnect();
    mgrWorkspaceConnect.setWorkspaceId(workspaceForm.getWorkspaceId());
    mgrWorkspaceConnect.setSchemaName(workspaceForm.getSchemaName());
    mgrWorkspaceConnect.setDbUser(workspaceForm.getDbUser());
    mgrWorkspaceConnect.setDbPassword(workspaceForm.getDbPassword());
    mgrWorkspaceConnect.setOwner(workspaceForm.getOwner());
    mgrWorkspaceConnect.setDbServer(workspaceForm.getDbServer());
    boolean success = workspaceService.updateOne(mgrWorkspaceConnect);
    if (success) {
      return redirectToList("workspace");
    } else {
      return "workspace/editWorkspace";
    }
  }

  @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
  public String showDetail(String workspaceId, Model model, HttpServletRequest request) throws EarthException {
    WorkspaceForm workspaceForm = new WorkspaceForm();
    MgrWorkspaceConnect mgrWorkspaceConnect = workspaceService.getDetail(workspaceId);

    workspaceForm.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
    workspaceForm.setSchemaName(mgrWorkspaceConnect.getSchemaName());
    workspaceForm.setDbUser(mgrWorkspaceConnect.getDbUser());
    workspaceForm.setDbPassword(mgrWorkspaceConnect.getDbPassword());
    workspaceForm.setOwner(mgrWorkspaceConnect.getOwner());
    workspaceForm.setDbServer(mgrWorkspaceConnect.getDbServer());

    model.addAttribute("workspaceForm", workspaceForm);
    return "workspace/editWorkspace";
  }

  @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
  public String deleteList(@ModelAttribute("workspaceIds") String workspaceIds, Model model, HttpServletRequest request)
      throws EarthException {
    if (!EStringUtil.isEmpty(workspaceIds)) {
      List<String> workspaceIdList = Arrays.asList(workspaceIds.split("\\s*,\\s*"));
      List<Message> messages = workspaceService.deleteList(workspaceIdList);
      if (messages != null && messages.size() > 0) {
        model.addAttribute(Session.MESSAGES, messages);
      }
    }
    model.addAttribute("mgrWorkspaces", workspaceService.getAll());
    return "workspace/workspaceList";
  }
}
