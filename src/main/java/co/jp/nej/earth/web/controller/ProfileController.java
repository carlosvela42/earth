package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(Model model) {
        List<MgrProfile> mgrProfiles= profileService.getAll();
        model.addAttribute("mgrProfiles", mgrProfiles);
        return "profile/profileList";
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String profileId) {
        try {
            Map<String,Object> profileDetail = profileService.getDetail(profileId);
            MgrProfile mgrProfile= (MgrProfile) profileDetail.get("mgrProfile");
            List<MgrUser> mgrUsers = (List<MgrUser>) profileDetail.get("mgrUsers");
            String userIds = (String)profileDetail.get("userIds");
            model.addAttribute("mgrUsers", mgrUsers);
            model.addAttribute("mgrProfile", mgrProfile);
            model.addAttribute("userIds", userIds);
            return "profile/editProfile";
        }
        catch (Exception ex){
            return "redirect: showList";
        }

    }
    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(Model model) {
        return "";
    }
    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model) {
        List<MgrUser> mgrUsers = userService.getAll();
        model.addAttribute("mgrUsers",mgrUsers);
        return "profile/addProfile";
    }
    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(Model model) {
        return "";
    }
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(Model model) {
        return "profile/profileList";
    }
}
