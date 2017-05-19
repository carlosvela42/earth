package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.service.ScheduleService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WorkspaceService workspaceService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
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
    public String addNew() {
        return "schedule/addSchedule";
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String userId) {
        try {
            return "schedule/editSchedule";
        } catch (Exception ex) {
            return "redirect: showList";
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("scheduleIds") String scheduleIds,
                             @ModelAttribute("workspaceId") String workspaceId) {
        try {
            List<String> scheduleId = EStringUtil.getListFromString(scheduleIds, "\\s*,\\s*");
            scheduleService.deleteList(workspaceId, scheduleId);
//            userService.deleteList(userId);
            return "redirect: switchWorkspace";
        } catch (Exception ex) {
            return "redirect: showList";
        }
    }

}
