package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.form.SearchForm;
import co.jp.nej.earth.service.EvidentLogService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/evidentLog")
public class EvidentLogController extends BaseController {

    @Autowired
    private EvidentLogService evidentLogService;

    @Autowired
    private WorkspaceService workspaceService;

    @RequestMapping(value = "/evidentLog", method = RequestMethod.GET)
    public String evidentLog(Model model) throws EarthException {
        List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
        model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        return "evidentLogScreen/evidentLogScreen";
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showLog(SearchForm searchForm, Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("strLogAccesses",
                evidentLogService.getListByWorkspaceId(workspaceId, searchForm.getSkip(), searchForm.getLimit(), null));
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("messages", model.asMap().get("messages"));
        return "evidentLogScreen/evidentLogScreen";
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public String evidentLogScreen(SearchForm searchForm, Model model, HttpServletRequest request)
            throws EarthException {
        try {
            SessionUtil.loadWorkspaces(workspaceService, model, request);
            String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
            model.addAttribute("strLogAccesses",
             evidentLogService.getListByWorkspaceId(workspaceId, searchForm.getSkip(), searchForm.getLimit(), null));
            model.addAttribute("messages", model.asMap().get("messages"));
            model.addAttribute("searchForm", searchForm);
            return "evidentLogScreen/evidentLogScreen";
        } catch (Exception ex) {
            return "redirect: /";
        }
    }

}
