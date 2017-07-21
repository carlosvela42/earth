package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.model.enums.SearchOperator;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.form.SearchForm;
import co.jp.nej.earth.service.EvidentLogService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        request.getSession().removeAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM);
        model.addAttribute("strLogAccesses",
            evidentLogService.getListByWorkspaceId(workspaceId, searchForm.getSkip(), searchForm.getLimit(), null));
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("templateTypes", TemplateType.getTempateTypes());
        model.addAttribute("searchOperators", SearchOperator.values());
        model.addAttribute("messages", model.asMap().get("messages"));
        return "evidentLogScreen/evidentLogScreen";
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    @ResponseBody
    public List<StrLogAccess> evidentLogScreen(SearchByColumnsForm searchByColumnsForm, Model model,
                                               HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        request.getSession().setAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM, searchByColumnsForm);
        List<StrLogAccess> strLogAccesses = evidentLogService.getListByWorkspaceIdColumn(workspaceId,
            searchByColumnsForm, null);
        return strLogAccesses;
    }

}
