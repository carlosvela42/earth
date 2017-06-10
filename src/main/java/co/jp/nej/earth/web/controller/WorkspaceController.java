package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EWorkspaceId;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.enums.DatabaseType;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.model.form.WorkspaceForm;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String displayWorkspace(Model model) throws EarthException {
        List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
        model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        return "workspace/workspaceList";
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model) throws EarthException {
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
        List<Message> messages = validatorUtil.validate(result);
        workspaceForm.setLastUpdateTime(null);
        if (messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            workspaceForm.setWorkspaceId(eWorkspaceId.getAutoId());
            model.addAttribute("workspaceForm", workspaceForm);
            return "workspace/addWorkspace";
        }
        MgrWorkspaceConnect mgrWorkspaceConnect = setMgrWorkspaceConnect(workspaceForm);
        messages = workspaceService.validateInsert(mgrWorkspaceConnect);
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);
            return "workspace/addWorkspace";
        } else {
            workspaceService.insertOne(mgrWorkspaceConnect);
            return redirectToList(URL);
        }
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("workspaceForm") WorkspaceForm workspaceForm, BindingResult result,
                            Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            return "workspace/addWorkspace";
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
            return redirectToList(URL);
        } else {
            return "workspace/addWorkspace";
        }
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(String workspaceId, Model model) throws EarthException {
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
    public String deleteList(DeleteListForm form, Model model) throws EarthException {
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
