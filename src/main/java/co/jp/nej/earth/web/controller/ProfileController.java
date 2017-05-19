package co.jp.nej.earth.web.controller;

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
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(Model model) throws EarthException {
        List<MgrProfile> mgrProfiles = profileService.getAll();
        model.addAttribute("mgrProfiles", mgrProfiles);
        return "profile/profileList";

    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String profileId) throws EarthException {
            Map<String, Object> profileDetail = profileService.getDetail(profileId);
            MgrProfile mgrProfile = ConversionUtil.castObject(profileDetail.get("mgrProfile"), MgrProfile.class);
            List<MgrUser> mgrUsers = ConversionUtil.castList(profileDetail.get("mgrUsers"), MgrUser.class);
            List<String> userIds = ConversionUtil.castList(profileDetail.get("userIds"), String.class);

            String strUserId = EStringUtil.getStringFromList(",",userIds);
            model.addAttribute("mgrUsers", mgrUsers);
            model.addAttribute("mgrProfile", mgrProfile);
            model.addAttribute("userIds", userIds);
            model.addAttribute("strUserId", strUserId);
            return "profile/editProfile";

    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("mgrProfile") MgrProfile mgrProfile,
            @ModelAttribute("strUserId") String strUserId, Model model) throws EarthException {
        List<Message> messages = profileService.validate(mgrProfile, false);
        List<String> userIds = EStringUtil.getListFromString(strUserId, ",");
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            model.addAttribute("mgrUsers", userService.getAll());
            model.addAttribute("mgrProfile", mgrProfile);
            model.addAttribute("strUserId", strUserId);
            model.addAttribute("userIds", userIds);

            return "profile/editProfile";
        } else {
            boolean updateProfile = profileService.updateAndAssignUsers(mgrProfile, userIds);
            if (updateProfile) {
                return "redirect: showList";
            } else {
                model.addAttribute("messageError", ErrorCode.E1009);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("userIds", userIds);
                return "profile/editProfile";
            }
        }
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model) throws EarthException {
        List<MgrUser> mgrUsers = userService.getAll();
        model.addAttribute("mgrUsers", mgrUsers);
        return "profile/addProfile";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("mgrProfile") MgrProfile mgrProfile, String strUserId, Model model)
            throws EarthException {
        List<Message> messages = profileService.validate(mgrProfile, true);
        List<String> userIds = EStringUtil.getListFromString(strUserId, ",");
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            model.addAttribute("mgrUsers", userService.getAll());
            model.addAttribute("mgrProfile", mgrProfile);
            model.addAttribute("strUserId", strUserId);
            model.addAttribute("userIds", userIds);

            return "profile/addProfile";
        } else {
            boolean insertProfile = profileService.insertAndAssignUsers(mgrProfile, userIds);
            if (insertProfile) {
                return "redirect: showList";
            } else {
                model.addAttribute("messageError", ErrorCode.E1009);
                model.addAttribute("mgrUsers", userService.getAll());
                model.addAttribute("mgrProfile", mgrProfile);
                model.addAttribute("strUserId", strUserId);
                model.addAttribute("userIds", userIds);

                return "profile/addProfile";
            }
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("profileIds") String profileIds, Model model) throws EarthException {
        List<String> profileId = EStringUtil.getListFromString(profileIds, ",");
        profileService.deleteList(profileId);
        return "redirect: showList";
    }
}