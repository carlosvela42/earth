package co.jp.nej.earth.web.controller;

import java.util.List;
import java.util.Map;

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

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProcessService;
import co.jp.nej.earth.model.entity.MgrTask;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.model.form.ScheduleForm;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.ClientSearchForm;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private ValidatorUtil validatorUtil;

    private static final String URL = "schedule";

    /*
     * @RequestMapping(value = {"", "/"}, method = RequestMethod.GET) public String showList(Model model) throws
     * EarthException { model.addAttribute("mgrWorkspaces", workspaceService.getAll()); return "schedule/scheduleList";
     * }
     */

    @RequestMapping(value = { "", "/" }, method = {RequestMethod.GET, RequestMethod.POST})
    public String switchWorkspace(Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        HttpSession session = request.getSession();
        SessionUtil.clearAllOtherSearchCondition(session, Constant.ScreenKey.PROCESS_SCHEDULE);
        ClientSearchForm searchForm = (ClientSearchForm) SessionUtil.getSearchCondtionValue(session,
            Constant.ScreenKey.PROCESS_SCHEDULE);

        if(searchForm == null) {
            searchForm = new ClientSearchForm();
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("mgrSchedules", scheduleService.getSchedulesByWorkspaceId(workspaceId));
        model.addAttribute("messages", model.asMap().get("messages"));
        return "schedule/scheduleList";
    }

    @RequestMapping(value = "/addNew", method = {RequestMethod.GET, RequestMethod.POST})
    public String addNew(Model model, HttpServletRequest request, ClientSearchForm searchForm) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SCHEDULE, searchForm);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        Map<String, Object> info = scheduleService.getInfo(workspaceId);
        model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
        model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
        model.addAttribute("mgrProcessServices",
                ConversionUtil.castList(info.get("mgrProcessServices"), MgrProcessService.class));
        MgrSchedule mgrSchedule = new MgrSchedule();
        mgrSchedule.setScheduleId((String) info.get("scheduleId"));
        model.addAttribute("mgrSchedule", mgrSchedule);
        return "schedule/addSchedule";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("scheduleForm") ScheduleForm scheduleForm, BindingResult result,
            Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        MgrSchedule mgrSchedule = setMgrSchedule(scheduleForm);
        mgrSchedule.setLastUpdateTime(null);
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrSchedule", mgrSchedule);

        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(scheduleService.validate(workspaceId, mgrSchedule, true));
        if (messages.size() > 0) {
            return getView(model, messages, workspaceId, true);
        } else {
            boolean insertSchedule = scheduleService.insertOne(workspaceId, mgrSchedule);
            if (insertSchedule) {
                return redirectToList(URL);
            } else {
                model.addAttribute("messageError", Constant.ErrorCode.E1009);
                return "schedule/addSchedule";
            }
        }
    }

    @RequestMapping(value = "/showDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDetail(String scheduleId, HttpServletRequest request, Model model,
        ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SCHEDULE, searchForm);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        Map<String, Object> info = scheduleService.showDetail(workspaceId, scheduleId);
        model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
        model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
        model.addAttribute("mgrProcessServices",
                ConversionUtil.castList(info.get("mgrProcessServices"), MgrProcessService.class));
        model.addAttribute("mgrSchedule", ConversionUtil.castObject(info.get("mgrSchedule"), MgrSchedule.class));
        model.addAttribute("workspaceId", workspaceId);
        return "schedule/addSchedule";

    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("addScheduleForm") ScheduleForm addScheduleForm,
            BindingResult result, Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        MgrSchedule mgrSchedule = setMgrSchedule(addScheduleForm);
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrSchedule", mgrSchedule);

        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(scheduleService.validate(workspaceId, mgrSchedule, false));
        if (messages.size() > 0) {
            return getView(model, messages, workspaceId, false);
        } else {
            boolean insertSchedule = scheduleService.updateOne(workspaceId, mgrSchedule);
            if (insertSchedule) {
                return redirectToList(URL);
            } else {
                model.addAttribute("messageError", Constant.ErrorCode.E1009);
                return "schedule/addSchedule";
            }
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(DeleteListForm deleteForm, HttpServletRequest request, ClientSearchForm searchForm) {
        try {
            SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SCHEDULE, searchForm);
            List<String> scheduleId = deleteForm.getListIds();
            scheduleService.deleteList(deleteForm.getWorkspaceId(), scheduleId);
            return redirectToList(URL);
        } catch (Exception ex) {
            return redirectToList(URL);
        }
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
    }

    private String getView(Model model, List<Message> messages, String workspaceId, boolean insert)
            throws EarthException {
        model.addAttribute(Constant.Session.MESSAGES, messages);
        Map<String, Object> info = scheduleService.getInfo(workspaceId);
        model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
        model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
        model.addAttribute("mgrProcessServices",
                ConversionUtil.castList(info.get("mgrProcessServices"), MgrProcessService.class));

        if (insert) {
            return "schedule/addSchedule";
        } else {
            return "schedule/addSchedule";
        }

    }

    private MgrSchedule setMgrSchedule(ScheduleForm addScheduleForm) {
        return new MgrSchedule(addScheduleForm.getScheduleId(), Integer.parseInt(addScheduleForm.getProcessId()),
                addScheduleForm.getTaskId(), addScheduleForm.getHostName(), addScheduleForm.getProcessIServiceId(),
                addScheduleForm.getEnableDisable(), addScheduleForm.getStartTime(), addScheduleForm.getEndTime(),
                addScheduleForm.getStartTime(), addScheduleForm.getRunIntervalDay(),
                addScheduleForm.getRunIntervalHour(), addScheduleForm.getRunIntervalMinute(),
                addScheduleForm.getRunIntervalSecond(), addScheduleForm.getLastUpdateTime());
    }
}
