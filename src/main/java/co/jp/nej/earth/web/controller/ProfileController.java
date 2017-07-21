package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.model.form.ProfileForm;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.ClientSearchForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidatorUtil validatorUtil;

    private static final String URL = "profile";

    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String showList(Model model, HttpServletRequest request) throws EarthException {
        List<MgrProfile> mgrProfiles = profileService.getAll();
        HttpSession session = request.getSession();
        SessionUtil.clearAllOtherSearchCondition(session, Constant.ScreenKey.USER_PROFILE);
        ClientSearchForm searchForm = (ClientSearchForm) SessionUtil.getSearchCondtionValue(session,
            Constant.ScreenKey.USER_PROFILE);

        if(searchForm == null) {
            searchForm = new ClientSearchForm();
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("mgrProfiles", mgrProfiles);
        model.addAttribute("userIds", new ArrayList<String>());
        return "profile/profileList";

    }

    @RequestMapping(value = "/showDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDetail(Model model, String profileId, ClientSearchForm searchForm,
            HttpServletRequest request) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER_PROFILE, searchForm);
        Map<String, Object> profileDetail = profileService.getDetail(profileId);
        MgrProfile mgrProfile = ConversionUtil.castObject(profileDetail.get("mgrProfile"), MgrProfile.class);
        List<MgrUser> mgrUsers = ConversionUtil.castList(profileDetail.get("mgrUsers"), MgrUser.class);
        List<String> userIds = ConversionUtil.castList(profileDetail.get("userIds"), String.class);

        String strUserId = EStringUtil.getStringFromList(",", userIds);
        model.addAttribute("mgrUsers", mgrUsers);
        model.addAttribute("mgrProfile", mgrProfile);
        model.addAttribute("userIds", userIds);
        model.addAttribute("strUserId", strUserId);
        return "profile/addProfile";

    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, BindingResult result
            , Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        MgrProfile mgrProfile = setMgrProfile(profileForm);
        messages.addAll(profileService.validate(mgrProfile, false));
        String strUserId = profileForm.getUserIds();
        List<String> userIds = EStringUtil.getListFromString(strUserId, ",");
        if (messages != null && messages.size() > 0) {
            return getView(model, mgrProfile, strUserId, userIds, messages);
        } else {
            boolean updateProfile = profileService.updateAndAssignUsers(mgrProfile, userIds);
            if (updateProfile) {
                return redirectToList(URL);
            } else {
                return getView(model, mgrProfile, strUserId, userIds, messages);
            }
        }
    }

    @RequestMapping(value = "/addNew", method = {RequestMethod.GET, RequestMethod.POST})
    public String addNew(Model model, HttpServletRequest request, ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER_PROFILE, searchForm);
        List<MgrUser> mgrUsers = userService.getAll();
        model.addAttribute("mgrUsers", mgrUsers);
        model.addAttribute("mgrProfile", new MgrProfile());
        model.addAttribute("userIds", new ArrayList<String>());
        return "profile/addProfile";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, BindingResult result,
                            Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        MgrProfile mgrProfile = setMgrProfile(profileForm);
        mgrProfile.setLastUpdateTime(null);
        messages.addAll(profileService.validate(mgrProfile, true));
        String strUserId = profileForm.getUserIds();
        List<String> userIds = EStringUtil.getListFromString(strUserId, ",");
        if (messages != null && messages.size() > 0) {
            return getView(model, mgrProfile, strUserId, userIds, messages);
        } else {
            boolean insertProfile = profileService.insertAndAssignUsers(mgrProfile, userIds);
            if (insertProfile) {
                return redirectToList(URL);
            } else {
                return getView(model, mgrProfile, strUserId, userIds, messages);
            }
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(DeleteListForm form, HttpServletRequest request,
            ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER_PROFILE, searchForm);
        List<String> profileId = form.getListIds();
        profileService.deleteList(profileId);
        return redirectToList(URL);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
    }

    private String getView(Model model, MgrProfile mgrProfile, String strUserId, List<String> userIds,
                           List<Message> messages) throws EarthException {
        model.addAttribute(Constant.Session.MESSAGES, messages);
        model.addAttribute("mgrUsers", userService.getAll());
        model.addAttribute("mgrProfile", mgrProfile);
        model.addAttribute("strUserId", strUserId);
        model.addAttribute("userIds", userIds);

        return "profile/addProfile";
    }

    private MgrProfile setMgrProfile(ProfileForm profileForm) {
        return new MgrProfile(profileForm.getProfileId(), Integer.parseInt("0"),
                profileForm.getDescription(), profileForm.getLdapIdentifier(), profileForm.getLastUpdateTime());
    }


}