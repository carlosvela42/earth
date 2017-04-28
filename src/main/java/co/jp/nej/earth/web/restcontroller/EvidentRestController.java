package co.jp.nej.earth.web.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import co.jp.nej.earth.model.entity.StrLogAccess;
import co.jp.nej.earth.service.EvidentLogService;

@RestController
@RequestMapping("/WS")
public class EvidentRestController {

    @Autowired
    private EvidentLogService evidentLogService;

    @RequestMapping(value = "/evidentLogScreenNew", method = RequestMethod.POST)
    public List<StrLogAccess> evidentLogScreenNew(@RequestParam("workspaceId") String workspaceId, Model model) {
        List<StrLogAccess> strLogAccesses = new ArrayList<StrLogAccess>();
        try {
            strLogAccesses = evidentLogService.getListByWorkspaceId(workspaceId);
            model.addAttribute("strLogAccesses", strLogAccesses);

            Gson gson = new Gson();

            String test = gson.toJson(strLogAccesses);
            System.out.print("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT: " + test);
            return strLogAccesses;
        } catch (Exception ex) {
            ex.printStackTrace();

            return strLogAccesses;
        }
    }
}
