package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
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
        try {
            List<MgrProfile> mgrProfiles = profileService.getAll();
            model.addAttribute("mgrProfiles", mgrProfiles);
            return "profile/profileList";
        } catch (EarthException ex) {
            return "profile/profileList";
        }

    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String profileId) {
        try {
            Map<String, Object> profileDetail = profileService.getDetail(profileId);
            MgrProfile mgrProfile = ConversionUtil.castObject(profileDetail.get("mgrProfile"), MgrProfile.class);
            List<MgrUser> mgrUsers = ConversionUtil.castList(profileDetail.get("mgrUsers"), MgrUser.class);

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
                model.addAttribute(Session.MESSAGES, messages);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/editProfile";
            } else {
                List<String> userId = new ArrayList<String>();
                if (!EStringUtil.isEmpty(userIds)) {
                    userId = EStringUtil.getListFromString(userIds, ",");
                }
                boolean updateProfile = profileService.updateAndAssignUsers(mgrProfile, userId);
                if (updateProfile) {
                    return this.showList(model);
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
            return this.showList(model);
        }
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("mgrProfile") MgrProfile mgrProfile, String userIds, Model model) {
        try {
            List<Message> messages = profileService.validate(mgrProfile, true);
            if (messages != null && messages.size() > 0) {
                model.addAttribute(Session.MESSAGES, messages);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/addProfile";
            } else {
                List<String> userId = new ArrayList<String>();
                if (!EStringUtil.isEmpty(userIds)) {
                    userId = EStringUtil.getListFromString(userIds, ",");
                }

                boolean insertProfile = profileService.insertAndAssignUsers(mgrProfile, userId);
                if (insertProfile) {
                    return "redirect: showList";
                } else {
                    model.addAttribute("messageError", ErrorCode.E1009);
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
            List<String> profileId = EStringUtil.getListFromString(profileIds, ",");
            profileService.deleteList(profileId);
            return this.showList(model);
        } catch (EarthException e) {
            return this.showList(model);
        }
    }
}
