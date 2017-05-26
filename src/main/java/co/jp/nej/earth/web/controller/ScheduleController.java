package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProcessService;
import co.jp.nej.earth.model.entity.MgrTask;
import co.jp.nej.earth.model.form.AddScheduleForm;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;
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
import java.util.Map;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showList(Model model) throws EarthException {
        model.addAttribute("mgrWorkspaces", workspaceService.getAll());
        return "schedule/scheduleList";
    }

    @RequestMapping(value = "/switchWorkspace", method = RequestMethod.POST)
    public String switchWorkspace(@ModelAttribute("workspaceId") String workspaceId, Model model)
            throws EarthException {
        model.addAttribute("mgrWorkspaces", workspaceService.getAll());
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrSchedules", scheduleService.getSchedulesByWorkspaceId(workspaceId));

        return "schedule/scheduleList";
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(@ModelAttribute("workspaceId") String workspaceId, Model model) throws EarthException {
        Map<String, Object> info = scheduleService.getInfo(workspaceId);
        model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
        model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
        model.addAttribute("mgrProcessServices", ConversionUtil.castList(info.get("mgrProcessServices"),
                MgrProcessService.class));
        MgrSchedule mgrSchedule = new MgrSchedule();
        mgrSchedule.setScheduleId((String) info.get("scheduleId"));
        model.addAttribute("mgrSchedule", mgrSchedule);
        model.addAttribute("workspaceId", workspaceId);
        return "schedule/addSchedule";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("workspaceId") String workspaceId,
                            @Valid @ModelAttribute("addScheduleForm") AddScheduleForm addScheduleForm,
                            BindingResult result, Model model) throws EarthException {
        MgrSchedule mgrSchedule = setMgrSchedule(addScheduleForm);
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrSchedule", mgrSchedule);

        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            return getView(model, messages, workspaceId, true);
        } else {
            messages = scheduleService.validate(workspaceId, mgrSchedule, true);
            model.addAttribute("mgrSchedule", mgrSchedule);

            if (messages.size() > 0) {
                return getView(model, messages, workspaceId, true);
            } else {
                boolean insertSchedule = scheduleService.insertOne(workspaceId, mgrSchedule);
                if (insertSchedule) {
                    return switchWorkspace(workspaceId, model);
                } else {
                    model.addAttribute("messageError", Constant.ErrorCode.E1009);
                    return "schedule/addSchedule";
                }
            }
        }
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, @ModelAttribute("workspaceId") String workspaceId, String scheduleId) {
        try {
            Map<String, Object> info = scheduleService.showDetail(workspaceId, scheduleId);
            model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
            model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
            model.addAttribute("mgrProcessServices", ConversionUtil.castList(info.get("mgrProcessServices"),
                    MgrProcessService.class));
            model.addAttribute("mgrSchedule", ConversionUtil.castObject(info.get("mgrSchedule"),
                    MgrSchedule.class));
            model.addAttribute("workspaceId", workspaceId);
            return "schedule/editSchedule";
        } catch (Exception ex) {
            return redirectToList();
        }
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("workspaceId") String workspaceId,
                            @Valid @ModelAttribute("addScheduleForm") AddScheduleForm addScheduleForm,
                            BindingResult result, Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        MgrSchedule mgrSchedule = setMgrSchedule(addScheduleForm);
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrSchedule", mgrSchedule);
        if (messages.size() > 0) {
            return getView(model, messages, workspaceId, false);
        } else {
            messages = scheduleService.validate(workspaceId, mgrSchedule, false);
            model.addAttribute("mgrSchedule", mgrSchedule);
            if (messages.size() > 0) {
                return getView(model, messages, workspaceId, false);
            } else {
                boolean insertSchedule = scheduleService.updateOne(workspaceId, mgrSchedule);
                if (insertSchedule) {
                    return switchWorkspace(workspaceId, model);
                } else {
                    model.addAttribute("messageError", Constant.ErrorCode.E1009);
                    return "schedule/editSchedule";
                }
            }
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("scheduleIds") String scheduleIds,
                             @ModelAttribute("workspaceId") String workspaceId, Model model) {
        try {
            List<String> scheduleId = EStringUtil.getListFromString(scheduleIds, "\\s*,\\s*");
            scheduleService.deleteList(workspaceId, scheduleId);
            return switchWorkspace(workspaceId, model);
        } catch (Exception ex) {
            return redirectToList();
        }
    }

    private String getView(Model model, List<Message> messages, String workspaceId, boolean insert) throws
            EarthException {
        model.addAttribute(Constant.Session.MESSAGES, messages);
        Map<String, Object> info = scheduleService.getInfo(workspaceId);
        model.addAttribute("mgrProcesses", ConversionUtil.castList(info.get("mgrProcesses"), MgrProcess.class));
        model.addAttribute("mgrTasks", ConversionUtil.castList(info.get("mgrTasks"), MgrTask.class));
        model.addAttribute("mgrProcessServices", ConversionUtil.castList(info.get("mgrProcessServices"),
                MgrProcessService.class));

        if (insert) {
            return "schedule/addSchedule";
        } else {
            return "schedule/editSchedule";
        }

    }

    private MgrSchedule setMgrSchedule(AddScheduleForm addScheduleForm) {
        return new MgrSchedule(addScheduleForm.getScheduleId(), Integer.parseInt(addScheduleForm
                .getProcessId()), addScheduleForm.getTaskId(), addScheduleForm.getHostName(),
                addScheduleForm.getProcessIServiceId(), addScheduleForm.getEnableDisable(),
                addScheduleForm.getStartTime(), addScheduleForm.getEndTime(), addScheduleForm.getStartTime(),
                addScheduleForm.getRunIntervalDay(), addScheduleForm.getRunIntervalHour(),
                addScheduleForm.getRunIntervalMinute(), addScheduleForm.getRunIntervalSecond());
    }
}
