package co.jp.nej.earth.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(Model model) {
        try {
            List<MgrProfile> mgrProfiles = profileService.getAll();
            model.addAttribute("mgrProfiles", mgrProfiles);
            return "profile/profileList";
        } catch (EarthException ex) {
            return "redirect: showList";
        }

    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String profileId) {
        try {
            Map<String, Object> profileDetail = profileService.getDetail(profileId);
            MgrProfile mgrProfile = (MgrProfile) profileDetail.get("mgrProfile");
            List<MgrUser> mgrUsers = (List<MgrUser>) profileDetail.get("mgrUsers");
            String userIds = (String) profileDetail.get("userIds");
            model.addAttribute("mgrUsers", mgrUsers);
            model.addAttribute("mgrProfile", mgrProfile);
            model.addAttribute("userIds", userIds);
            return "profile/editProfile";
        } catch (EarthException ex) {
            return "redirect: showList";
        }

    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("mgrProfile") MgrProfile mgrProfile,
            @ModelAttribute("userIds") String userIds, Model model) {
        try {
            List<Message> messages = profileService.validate(mgrProfile, false);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/editProfile";
            } else {
                List<String> userId = Arrays.asList(userIds.split("\\s*,\\s*"));
                boolean updateProfile = profileService.updateAndAssignUsers(mgrProfile, userId);
                if (updateProfile) {
                    return "redirect: showList";
                } else {
                    model.addAttribute("messageError", "E1009");
                    model.addAttribute("mgrUsers", userService.getAll());
                    model.addAttribute("mgrProfile", mgrProfile);
                    model.addAttribute("userIds", userIds);
                    return "profile/editProfile";
                }
            }
        } catch (EarthException ex) {
            try {
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/editProfile";
            } catch (EarthException e) {
                return "error/error";
            }
        }
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model) {
        try {
            List<MgrUser> mgrUsers = userService.getAll();
            model.addAttribute("mgrUsers", mgrUsers);
            return "profile/addProfile";
        } catch (EarthException ex) {
            return "redirect: showList";
        }
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("mgrProfile") MgrProfile mgrProfile, String userIds, Model model) {
        try {
            List<Message> messages = profileService.validate(mgrProfile, true);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/addProfile";
            } else {
                List<String> userId = Arrays.asList(userIds.split("\\s*,\\s*"));
                boolean insertProfile = profileService.insertAndAssignUsers(mgrProfile, userId);
                if (insertProfile) {
                    return "redirect: showList";
                } else {
                    model.addAttribute("messageError", "E1009");
                    model.addAttribute("mgrUsers", userService.getAll());
                    model.addAttribute("mgrProfile", mgrProfile);
                    model.addAttribute("userIds", userIds);
                    return "profile/addProfile";
                }
            }
        } catch (EarthException ex) {
            try {
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/addProfile";
            } catch (EarthException e) {
                return "error/error";
            }
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("profileIds") String profileIds, Model model) {
        try {
            List<String> profileId = Arrays.asList(profileIds.split("\\s*,\\s*"));
            profileService.deleteList(profileId);
            return "redirect: showList";
        } catch (EarthException e) {
            return "redirect: showList";
        }
    }
}
