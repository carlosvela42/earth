package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.Constant.*;
import co.jp.nej.earth.model.entity.*;
import co.jp.nej.earth.service.*;
import co.jp.nej.earth.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
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
                return redirectToList();
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
                return redirectToList();
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
        return redirectToList();
    }
}