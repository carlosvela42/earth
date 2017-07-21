package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.form.DeleteListForm;
import co.jp.nej.earth.model.form.UserForm;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.ConversionUtil;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ValidatorUtil validatorUtil;

    private static final String URL = "user";

    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String showList(Model model, HttpServletRequest request) {
        try {
            List<MgrUser> mgrUsers = userService.getAll();
            HttpSession session = request.getSession();
            SessionUtil.clearAllOtherSearchCondition(session, Constant.ScreenKey.USER);
            ClientSearchForm searchForm = (ClientSearchForm) SessionUtil.getSearchCondtionValue(session,
                Constant.ScreenKey.USER);

            if(searchForm == null) {
                searchForm = new ClientSearchForm();
            }
            model.addAttribute("searchForm", searchForm);
            model.addAttribute("mgrUsers", mgrUsers);
            return "user/userList";
        } catch (EarthException ex) {
            return "error/error";
        }
    }

    @RequestMapping(value = "/addNew", method = {RequestMethod.GET, RequestMethod.POST})
    public String addNew(Model model, ClientSearchForm searchForm, HttpServletRequest request) throws EarthException{
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER, searchForm);
        model.addAttribute("user", new MgrUser());
        return "user/addUser";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult result, Model model) {
        MgrUser mgrUser = setMgrUser(userForm);
        mgrUser.setLastUpdateTime(null);
        try {
            List<Message> messages = validatorUtil.validate(result);
            messages.addAll(userService.validate(mgrUser, true));
            if (messages != null && messages.size() > 0) {
                model.addAttribute(Session.MESSAGES, messages);
                mgrUser = setUser(mgrUser);
                model.addAttribute("user", mgrUser);
                return "user/addUser";
            } else {
                boolean insertUser = userService.insertOne(mgrUser);
                if (insertUser) {
                    return redirectToList(URL);
                } else {
                    model.addAttribute("messageError", "E1009");
                    mgrUser = setUser(mgrUser);
                    model.addAttribute("user", mgrUser);
                    return "user/addUser";
                }
            }
        } catch (EarthException ex) {
            mgrUser = setUser(mgrUser);
            model.addAttribute("user", mgrUser);
            return "user/addUser";
        }
    }

    @RequestMapping(value = "/showDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDetail(Model model, String userId, ClientSearchForm searchForm, HttpServletRequest request) {
        try {
            SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER, searchForm);
            Map<String, Object> userDetail = userService.getDetail(userId);
            MgrUser mgrUser = (MgrUser) userDetail.get("mgrUser");
            List<MgrProfile> mgrProfiles = ConversionUtil.castList(userDetail.get("mgrProfiles"), MgrProfile.class);
            model.addAttribute("user", mgrUser);
            model.addAttribute("mgrProfiles", mgrProfiles);
            return "user/addUser";
        } catch (EarthException ex) {
            return redirectToList();
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(DeleteListForm form, ClientSearchForm searchForm, HttpServletRequest request) {
        try {
            SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.USER, searchForm);
            List<String> userIds = form.getListIds();
            userService.deleteList(userIds);
            return redirectToList(URL);
        } catch (EarthException ex) {
            return redirectToList(URL);
        }
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult result,
                            Model model) throws EarthException {
        MgrUser mgrUser = setMgrUser(userForm);
        List<Message> messages = userService.validate(mgrUser, false);
        model.addAttribute("mgrProfiles", profileService.getProfilesByUserId(mgrUser.getUserId()));
        if (messages != null && messages.size() > 0) {
            model.addAttribute(Session.MESSAGES, messages);
            mgrUser = setUser(mgrUser);
            model.addAttribute("user", mgrUser);
            return "user/addUser";
        } else {
            boolean updateUser = userService.updateOne(mgrUser);
            if (updateUser) {
                return redirectToList(URL);
            } else {
                model.addAttribute("messageError", "E1009");
                mgrUser = setUser(mgrUser);
                model.addAttribute("user", mgrUser);
                return "user/addUser";
            }
        }
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
    }

    private MgrUser setUser(MgrUser mgrUser) {
        mgrUser.setPassword("");
        mgrUser.setConfirmPassword("");
        return mgrUser;
    }

    private MgrUser setMgrUser(UserForm userForm) {
        return new MgrUser(userForm.getUserId(), userForm.getName(), userForm.getPassword(),
                userForm.getConfirmPassword(), userForm.isChangePassword(), userForm.getLastUpdateTime());
    }

}
