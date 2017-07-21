package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EWorkspaceId;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.model.form.WorkspaceForm;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.ClientSearchForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

/**
 * @author longlt
 */

@Controller
@RequestMapping("/workspace")
public class WorkspaceController extends BaseController {

    public static final String URL = "workspace";
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private EWorkspaceId eWorkspaceId;

    @Autowired
    private DatabaseType databaseType;

    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String displayWorkspace(Model model, HttpServletRequest request) throws EarthException {
        List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
        HttpSession session = request.getSession();
        SessionUtil.clearAllOtherSearchCondition(session, Constant.ScreenKey.PROCESS_WORKSPACE);
        ClientSearchForm searchForm = (ClientSearchForm) SessionUtil.getSearchCondtionValue(session,
            Constant.ScreenKey.PROCESS_WORKSPACE);

        if(searchForm == null) {
            searchForm = new ClientSearchForm();
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        return "workspace/workspaceList";
    }

    @RequestMapping(value = "/addNew", method = {RequestMethod.GET, RequestMethod.POST})
    public String addNew(Model model, ClientSearchForm searchForm, HttpServletRequest request) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_WORKSPACE, searchForm);
        WorkspaceForm workspaceForm = new WorkspaceForm();
        // TODO:
        workspaceForm.setWorkspaceId(eWorkspaceId.getAutoId());
        workspaceForm.setDbType(databaseType.name());
        model.addAttribute("workspaceForm", workspaceForm);
        return "workspace/addWorkspace";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("workspaceForm") WorkspaceForm workspaceForm, BindingResult result,
                            Model model) throws EarthException {
        MgrWorkspaceConnect mgrWorkspaceConnect = setMgrWorkspaceConnect(workspaceForm);
        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(workspaceService.validateInsert(mgrWorkspaceConnect));
        workspaceForm.setLastUpdateTime(null);
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            workspaceForm.setWorkspaceId(eWorkspaceId.getAutoId());
            model.addAttribute("workspaceForm", workspaceForm);
            return "workspace/addWorkspace";
        } else {
            workspaceService.insertOne(mgrWorkspaceConnect);
            return redirectToList(URL);
        }
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("workspaceForm") WorkspaceForm workspaceForm, BindingResult result,
                            Model model) throws EarthException {
        MgrWorkspaceConnect mgrWorkspaceConnect = setMgrWorkspaceConnect(workspaceForm);
        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(workspaceService.validateInsert(mgrWorkspaceConnect));
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            model.addAttribute("workspaceForm", workspaceForm);
            return "workspace/addWorkspace";
        } else {
            workspaceService.updateOne(mgrWorkspaceConnect);
            return redirectToList(URL);
        }
    }

    @RequestMapping(value = "/showDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDetail(String workspaceId, Model model, HttpServletRequest request,
             ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_WORKSPACE, searchForm);
        WorkspaceForm workspaceForm = new WorkspaceForm();
        MgrWorkspaceConnect mgrWorkspaceConnect = workspaceService.getDetail(workspaceId);

        workspaceForm.setWorkspaceId(mgrWorkspaceConnect.getWorkspaceId());
        workspaceForm.setDbType(databaseType.name());
        workspaceForm.setSchemaName(mgrWorkspaceConnect.getSchemaName());
        workspaceForm.setDbUser(mgrWorkspaceConnect.getDbUser());
        workspaceForm.setDbPassword(mgrWorkspaceConnect.getDbPassword());
        workspaceForm.setOwner(mgrWorkspaceConnect.getOwner());
        workspaceForm.setDbServer(mgrWorkspaceConnect.getDbServer());
        workspaceForm.setLastUpdateTime(mgrWorkspaceConnect.getLastUpdateTime());

        model.addAttribute("workspaceForm", workspaceForm);
        return "workspace/addWorkspace";
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(DeleteListForm form, Model model, HttpServletRequest request,
            ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_WORKSPACE, searchForm);
        List<String> workspaceIds = form.getListIds();
        List<Message> messages = workspaceService.deleteList(workspaceIds);
        model.addAttribute(Session.MESSAGES, messages);
        return redirectToList(URL);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
    }

    private MgrWorkspaceConnect setMgrWorkspaceConnect(WorkspaceForm workspaceForm) {
        MgrWorkspaceConnect mgrWorkspaceConnect = new MgrWorkspaceConnect();
        mgrWorkspaceConnect.setWorkspaceId(workspaceForm.getWorkspaceId());
        mgrWorkspaceConnect.setSchemaName(workspaceForm.getSchemaName());
        mgrWorkspaceConnect.setDbUser(workspaceForm.getDbUser());
        mgrWorkspaceConnect.setDbPassword(workspaceForm.getDbPassword());
        mgrWorkspaceConnect.setOwner(workspaceForm.getOwner());
        mgrWorkspaceConnect.setDbServer(workspaceForm.getDbServer());
        mgrWorkspaceConnect.setDbType(workspaceForm.getDbType());
        return mgrWorkspaceConnect;
    }


}
