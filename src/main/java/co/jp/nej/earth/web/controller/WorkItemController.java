package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Column;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.Row;
import co.jp.nej.earth.model.WorkItemListDTO;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.enums.SearchOperator;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.web.form.SearchByColumnForm;
import co.jp.nej.earth.web.form.SearchByColumnsForm;
import co.jp.nej.earth.web.form.SearchForm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workItem")
public class WorkItemController extends BaseController {

    public static final String URL = "workItem";
    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private EventControlService eventControlService;


    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showList(Model model, HttpServletRequest request, final RedirectAttributes redirectAttributes)
        throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        HttpSession session = request.getSession();
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(session);

        List<SearchForm> searchForms = new ArrayList<>();
        List<MgrTemplate> templates = templateService.getAllByWorkspace(workspaceId);
        for (MgrTemplate mgrTemplate : templates) {
            SearchForm searchForm = new SearchForm();
            searchForm.setTemplateName(mgrTemplate.getTemplateName());
            searchForm.setTemplateId(mgrTemplate.getTemplateId());
            searchForm.setTemplateTableName(mgrTemplate.getTemplateTableName());
            searchForms.add(searchForm);
        }
        List<WorkItemListDTO> workItems = workItemService.getWorkItemsByWorkspace(workspaceId);

        if (!EStringUtil.equals(ConversionUtil.castObject(session.getAttribute("workspaceId"), String.class),
            workspaceId)) {
            request.getSession().removeAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM);
        }
        model.addAttribute("workItems", workItems);
        model.addAttribute("searchForms", searchForms);
        model.addAttribute("searchByColumnForm", new SearchByColumnForm());
        model.addAttribute("templateTypes", TemplateType.getTempateTypes());
        model.addAttribute("searchOperators", SearchOperator.values());
        if (redirectAttributes.getFlashAttributes().get(Constant.Session.MESSAGES) == null) {
            model.addAttribute(Constant.Session.MESSAGES, model.asMap().get("messages"));
        } else {
            model.addAttribute(Constant.Session.MESSAGES, redirectAttributes.getFlashAttributes()
                .get(Constant.Session.MESSAGES));
        }
        request.getSession().setAttribute("workspaceId", workspaceId);
        return "workitem/workItemList";
    }

    @RequestMapping(value = {"/unlock"}, method = RequestMethod.POST)
    @ResponseBody
    public List<WorkItemListDTO> unlock(DeleteListForm deleteListForm, Model model, HttpServletRequest request)
        throws EarthException {

        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        List<String> eventIds = deleteListForm.getListIds();
        eventControlService.unlockEventControls(workspaceId, eventIds);
        List<WorkItemListDTO> workItems = workItemService.getWorkItemsByWorkspace(workspaceId);
        return workItems;
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(@ModelAttribute("workItemId") String workItemId, Model model,
                             HttpServletRequest request, final RedirectAttributes redirectAttributes)
        throws EarthException {
        HttpSession session = request.getSession();
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(session);
        Integer processId = workItemService.getProcessIdByWorkItem(workspaceId, workItemId);

        List<Message> messages = workItemService.openWorkItem(session, workspaceId, String.valueOf(processId),
            workItemId, OpenProcessMode.EDIT.getMode());
        if (messages.size() > 0) {
            model.addAttribute(Constant.Session.MESSAGES, messages);
            redirectAttributes.addFlashAttribute(Constant.Session.MESSAGES, messages);
            return redirectToList(URL);
        }
        return redirectToList("workItem/edit?workItemId=" + workItemId);
    }

    @RequestMapping(value = "/searchColumn", method = RequestMethod.POST)
    @ResponseBody
    public SearchForm searchColumn(SearchByColumnsForm searchByColumnsForm, Model model, HttpServletRequest request)
        throws EarthException, JsonParseException, JsonMappingException, IOException {
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        SearchForm searchForm = new SearchForm();
        Map<String, Object> map = workItemService.getRowValues(searchByColumnsForm, workspaceId);
        searchForm.setColumns(ConversionUtil.castList(map.get("columns"), Column.class));
        searchForm.setRows(ConversionUtil.castList(map.get("rows"), Row.class));
        request.getSession().setAttribute(Constant.Session.SEARCH_BY_COLUMNS_FORM, searchByColumnsForm);
        request.getSession().setAttribute("workspaceId", workspaceId);
        return searchForm;
    }

    @RequestMapping(value = {"/getList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<WorkItemListDTO> getList(DeleteListForm deleteListForm, Model model, HttpServletRequest request)
        throws EarthException, JsonParseException, JsonMappingException, IOException {
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        List<String> workItemIds = deleteListForm.getListIds();
        List<WorkItemListDTO> workItems = new ArrayList<>();
        request.getSession().setAttribute("workspaceId", workspaceId);
        if (!EStringUtil.isEmpty(workItemIds.get(0))) {
            workItems = workItemService.getWorkItemsByWorkspace(workspaceId, workItemIds);
        } else {
            workItems = workItemService.getWorkItemsByWorkspace(workspaceId);
        }
        return workItems;
    }

    @RequestMapping(value = {"/getTemplateName"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MgrTemplate> getTemplateName(SearchByColumnsForm searchByColumnsForm, HttpServletRequest request)
        throws EarthException, JsonParseException, JsonMappingException, IOException {
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        request.getSession().setAttribute("workspaceId", workspaceId);
        List<MgrTemplate> mgrTemplates = templateService.getAllByWorkspace(workspaceId,
            searchByColumnsForm.getTemplateType());
        return mgrTemplates;
    }

}
