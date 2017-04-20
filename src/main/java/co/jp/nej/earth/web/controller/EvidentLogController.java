package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.service.EvidentLogService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/evidentLog")
public class EvidentLogController {

    @Autowired
    private EvidentLogService evidentLogService;

    @Autowired
    private WorkspaceService workspaceService;

    @RequestMapping(value = "/evidentLog", method = RequestMethod.GET)
    public String evidentLog(Model model) {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
            return "evidentLogScreen/evidentLogScreen";
        } catch (Exception ex) {
            return "redirect: /";
        }
    }

    @RequestMapping(value = "/evidentLogScreen", method = RequestMethod.GET)
    public String evidentLogScreen(@ModelAttribute("mgrWorkspace") MgrWorkspace mgrWorkspace, Model model) {
        try {
            List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
            String workspaceId = "";
            if (!EStringUtil.isEmpty(mgrWorkspace.getWorkspaceId())) {
                workspaceId = mgrWorkspace.getWorkspaceId();
                model.addAttribute("strLogAccesses", evidentLogService.getListByWorkspaceId(workspaceId));
            }
            model.addAttribute("workspaceId", workspaceId);
            return "evidentLogScreen/evidentLogScreen";
        } catch (Exception ex) {
            return "redirect: /";
        }
    }


    @RequestMapping(value = "/evidentLogScreenNew", method = RequestMethod.POST,produces = MediaType
            .APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<StrLogAccess> evidentLogScreenNew(@RequestParam("workspaceId") String workspaceId,Model model) {
        List<StrLogAccess> strLogAccesses = new ArrayList<StrLogAccess>();
        try {
            strLogAccesses = evidentLogService.getListByWorkspaceId(workspaceId);
            model.addAttribute("strLogAccesses",strLogAccesses);
            return strLogAccesses;
        } catch (Exception ex) {
            ex.printStackTrace();

            return strLogAccesses;
        }
    }
}
