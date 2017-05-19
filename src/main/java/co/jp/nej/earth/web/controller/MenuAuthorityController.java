package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.service.MenuService;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author p-tvo-thuynd
 */

@Controller
@RequestMapping("/menuAccessRight")
public class MenuAuthorityController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/menuList")
    public String menuList(Model model) throws EarthException {
        model.addAttribute("mgrMenus", menuService.getAll());
        return "menuAccessRight/menuList";
    }

    @RequestMapping(value = "/showDetail")
    public String menuDetail(String functionId, Model model, HttpServletRequest request)
            throws EarthException {
        model.addAttribute("mgrMenu", menuService.getDetail(functionId));
        model.addAttribute("mgrUsers", userService.getAll());
        model.addAttribute("mgrProfiles", profileService.getAll());
        model.addAttribute("userAccessRights", menuService.getUserAuthority(functionId));
        model.addAttribute("profileAccessRights", menuService.getProfileAuthority(functionId));
        model.addAttribute("accessRights", Arrays.asList(AccessRight.values()));

        return "menuAccessRight/editMenuAccessRight";
    }

    @RequestMapping(value = "/updateMenuAccessRight", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("userIdAccessRight") String userIdAccessRight,
                            @ModelAttribute("profileIdAccessRight") String profileIdAccessRight,
                            @ModelAttribute("mgrMenu") MgrMenu mgrMenu,
                            Model model) throws EarthException {
        // Convert string userIdAccessRight to list of UserAccessRight.
        List<UserAccessRight> userAccessRights = new ArrayList<>();
        if (!StringUtils.isEmpty(userIdAccessRight)) {
            String[] userIdArr =  userIdAccessRight.split(",");
            if (userIdArr.length > 0) {
                for (String userId : userIdArr) {
                    if (!StringUtils.isEmpty(userId)) {
                        String[] userAccessRightArr = userId.split("\\|");
                        UserAccessRight userAccessRight = new UserAccessRight();
                        userAccessRight.setUserId(userAccessRightArr[0]);
                        if (!StringUtils.isEmpty(userAccessRightArr[1])) {
                            userAccessRight.setAccessRight(AccessRight.valueOf(userAccessRightArr[1]));
                        }
                        userAccessRights.add(userAccessRight);
                    }
                }
            }
        }

        // Convert string profileIdAccessRight to list of ProfileAccessRight.
        List<ProfileAccessRight> profileAccessRights = new ArrayList<>();
        if (!StringUtils.isEmpty(profileIdAccessRight)) {
            String[] profileIdArr = profileIdAccessRight.split(",");
            if (profileIdArr.length > 0) {
                for (String profileId : profileIdArr) {
                    if (!StringUtils.isEmpty(profileId)) {
                        String[] profileAccessRightArr = profileId.split("\\|");
                        ProfileAccessRight profileAccessRight = new ProfileAccessRight();
                        profileAccessRight.setProfileId(profileAccessRightArr[0]);
                        if (!StringUtils.isEmpty(profileAccessRightArr[1])) {
                            profileAccessRight.setAccessRight(AccessRight.valueOf(profileAccessRightArr[1]));
                        }
                        profileAccessRights.add(profileAccessRight);
                    }
                }
            }
        }

        // Insert Menu Authority.
        menuService.saveAuthority(mgrMenu.getFunctionId(),userAccessRights,profileAccessRights);
        return "redirect: menuList";
    }
}
