package co.jp.nej.earth.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Column;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.Row;
import co.jp.nej.earth.model.WorkItemListDTO;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.service.ProcessService;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.web.form.SearchForm;

@Controller
@RequestMapping("/workItem")
public class WorkItemController extends BaseController {

  @Autowired
  private WorkItemService workItemService;

  @Autowired
  private WorkspaceService workspaceService;

  @Autowired
  private TemplateService templateService;

  @Autowired
  private ProcessService processService;

  @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
  public String showList(Model model, HttpServletRequest request) throws EarthException {
    HttpSession session = request.getSession();
    String workspaceId = (String) session.getAttribute("workspaceId");
    if (!EStringUtil.isEmpty(workspaceId)) {
      session.removeAttribute("workspaceId");
    }
    model.addAttribute("workItems", new ArrayList<WorkItemListDTO>());
    model.addAttribute("templateNames", new ArrayList<String>());
    model.addAttribute("templateTypes", new ArrayList<String>());
    model.addAttribute("mgrWorkspaces", workspaceService.getAll());
    return "workitem/workItemList";
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public @ResponseBody SearchForm searchByCondition(@ModelAttribute("templateValue") SearchForm searchForm, Model model,
      HttpServletRequest request) throws EarthException, JsonParseException, JsonMappingException, IOException {
    HttpSession session = request.getSession();
    String workspaceId = (String) session.getAttribute("workspaceId");
    String[] temp = searchForm.getTemplateId().split("-");
    searchForm.setTemplateId(temp[0]);
    searchForm.setTemplateTableName(temp[1]);
    List<Column> columns = new ArrayList<>();
    String fieldJsons = templateService.getFieldJson(workspaceId, searchForm);
    if (!EStringUtil.isEmpty(fieldJsons)) {
      List<Field> fields = new ObjectMapper().readValue(fieldJsons, new TypeReference<List<Field>>() {
      });
      for (Field field : fields) {
        Column column = new Column();
        column.setName(field.getName());
        columns.add(column);
      }
      List<Row> rows = workItemService.getRowValue(searchForm, columns, workspaceId);
      searchForm.setColumns(columns);
      searchForm.setRows(rows);
    }
    model.addAttribute("searchForm", searchForm);
    // return new ObjectMapper().writeValueAsString(searchForm);
    return searchForm;
  }

  @RequestMapping(value = { "", "/unlock" }, method = RequestMethod.POST)
  public String unlock(@ModelAttribute("workitemId") String workitemId,
      @ModelAttribute("workspaceId") String workspaceId, Model model, HttpServletRequest request)
      throws EarthException {
    List<String> stringWorkItemId = Arrays.asList(workitemId.split("\\s*,\\s*"));
    workItemService.unlock(stringWorkItemId, workspaceId);
    List<WorkItemListDTO> workItems = workItemService.getWorkItemsByWorkspace(workspaceId);
    model.addAttribute("mgrWorkspaces", workspaceService.getAll());
    model.addAttribute("workItems", workItems);
    return "workitem/workItemList";
  }

  @RequestMapping(value = "/switchWorkspace", method = RequestMethod.POST)
  public String switchWorkspace(@ModelAttribute("workspaceId") String workspaceId, Model model,
      HttpServletRequest request) throws EarthException {
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
    model.addAttribute("workItems", workItems);
    model.addAttribute("searchForms", searchForms);
    model.addAttribute("mgrWorkspaces", workspaceService.getAll());
    model.addAttribute("templateTypes", TemplateType.getTempateTypes());
    HttpSession session = request.getSession();
    session.setAttribute("workspaceId", workspaceId);
    return "workitem/workItemList";
  }

  @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
  public String showDetail(@ModelAttribute("workitemId") String workitemId, Model model, HttpServletRequest request)
      throws EarthException {
    String url = EStringUtil.EMPTY;
    HttpSession session = request.getSession();
    String workspaceId = (String) session.getAttribute("workspaceId");
    session.setAttribute("workitemId", workitemId);
    Integer processId = workItemService.getProcessIdByWorkItem(workspaceId, workitemId);
    boolean status = processService.openProcess(session, workspaceId, String.valueOf(processId), workitemId,
        OpenProcessMode.EDIT);
    if (status) {
      url = "workitem/editWorkItem?workitemId=" + workitemId + "&workspaceId=" + workspaceId;
    } else {
      url = "workitem/workItemList";
    }
    return url;
  }
}
