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
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.util.EStringUtil;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(Model model) {
        try {
            List<MgrUser> mgrUsers = userService.getAll();
            model.addAttribute("mgrUsers", mgrUsers);
            return "user/userList";
        } catch (EarthException ex) {
            return "error/error";
        }
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew() {
        return "user/addUser";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("mgrUser") MgrUser mgrUser, Model model) {
        try {
            List<Message> messages = userService.validate(mgrUser, true);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                mgrUser = setUser(mgrUser);
                model.addAttribute("mgrUser", mgrUser);
                return "user/addUser";
            } else {
                boolean insertUser = userService.insertOne(mgrUser);
                if (insertUser) {
                    return "redirect: showList";
                } else {
                    model.addAttribute("messageError", "E1009");
                    mgrUser = setUser(mgrUser);
                    model.addAttribute("mgrUser", mgrUser);
                    return "user/addUser";
                }
            }
        } catch (EarthException ex) {
            mgrUser = setUser(mgrUser);
            model.addAttribute("mgrUser", mgrUser);
            return "user/addUser";
        }
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String userId) {
        try {
            Map<String, Object> userDetail = userService.getDetail(userId);
            MgrUser mgrUser = (MgrUser) userDetail.get("mgrUser");
            List<MgrProfile> mgrProfiles = (List<MgrProfile>) userDetail.get("mgrProfiles");
            model.addAttribute("mgrUser", mgrUser);
            model.addAttribute("mgrProfiles", mgrProfiles);
            return "user/editUser";
        } catch (EarthException ex) {
            return "redirect: showList";
        }
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("userIds") String userIds) {
        try {
            List<String> userId = EStringUtil.getListString(userIds, "\\s*,\\s*");
            userService.deleteList(userId);
            return "redirect: showList";
        } catch (EarthException ex) {
            return "redirect: showList";
        }
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("mgrUser") MgrUser mgrUser, Model model) {
        try {
            List<Message> messages = userService.validate(mgrUser, false);
            if (messages != null && messages.size() > 0) {
                model.addAttribute("messages", messages);
                mgrUser = setUser(mgrUser);
                model.addAttribute("mgrUser", mgrUser);
                return "user/editUser";
            } else {
                boolean updateUser = userService.updateOne(mgrUser);
                if (updateUser) {
                    return "redirect: showList";
                } else {
                    model.addAttribute("messageError", "E1009");
                    mgrUser = setUser(mgrUser);
                    model.addAttribute("mgrUser", mgrUser);
                    return "user/editUser";
                }
            }
        } catch (EarthException ex) {
            model.addAttribute("mgrUser", mgrUser);
            return "user/editUser";
        }
    }

    private MgrUser setUser(MgrUser mgrUser) {
        mgrUser.setPassword("");
        mgrUser.setConfirmPassword("");
        return mgrUser;
    }
}
