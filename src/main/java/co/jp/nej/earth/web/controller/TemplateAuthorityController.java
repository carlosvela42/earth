package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUser;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.service.TemplateService;

/**
 * @author p-tvo-thuynd
 *
 */

@Controller
@RequestMapping("/templateAccessRight")
public class TemplateAuthorityController extends BaseController {

    @Autowired
    private TemplateService templateService;

    /**
     * @author p-tvo-thuynd this method is to show list of workspace in openning
     *         screen of Template list
     * @param model
     *            to hold model attribute
     * @return path of screen
     */
    @RequestMapping(value = "/initTemplateListScreen", method = RequestMethod.GET)
    public String showListWorkspace(Model model) {
        try {
            List<MgrWorkspace> mgrWorkspaces = templateService.getAllWorkspace();
            model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        } catch (EarthException e) {
            String message = "Error: Can not get workspace list";
            model.addAttribute("message", message);
        }
        return "templateAccessRight/showList";
    }

    /**
     * @author p-tvo-thuynd this method is to get list of template based on
     *         workspace id
     * @param mgrWorkspace
     *            object workspace passed from screen
     * @param model
     *            to hold model attribute
     * @return path of screen
     * @throws EarthException
     */
    @RequestMapping(value = "/chooseWorkspace", method = RequestMethod.GET)
    public String getListTemplate(@ModelAttribute("mgrWorkspace") MgrWorkspace mgrWorkspace, Model model)
            throws EarthException {
        List<MgrWorkspace> mgrWorkspaces = templateService.getAllWorkspace();
        model.addAttribute("mgrWorkspaces", mgrWorkspaces);
        List<MgrTemplate> mgrTemplates = new ArrayList<MgrTemplate>();
        String workspaceId = "";
        if (!StringUtils.isEmpty(mgrWorkspace.getWorkspaceId())) {
            workspaceId = mgrWorkspace.getWorkspaceId();
            mgrTemplates = templateService.getTemplateListInfo(workspaceId);
        }
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("mgrTemplates", mgrTemplates);
        return "templateAccessRight/showList";
    }

    /**
     * @author p-tvo-thuynd this method is to show detail infor of an template
     * @param model
     *            to hold model attribute
     * @param templateId
     *            id of choosen template
     * @param workspaceId
     *            id of workspace which template belongs to
     * @return path of screen
     * @throws EarthException
     */
    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String templateId, String workspaceId) throws EarthException {
        TemplateKey templateKey = new TemplateKey();
        templateKey.setTemplateId(templateId);
        templateKey.setWorkspaceId(workspaceId);
        MgrTemplate mgrTemplate = templateService.getById(templateKey);
        List<UserAccessRight> userAccessRights = templateService.getUserAuthority(templateKey);
        List<ProfileAccessRight> profileAccessRights = templateService.getProfileAuthority(templateKey);
        List<MgrUser> mgrUsers = templateService.getAllUser();
        List<MgrProfile> mgrProfiles = templateService.getAllProfile();
        List<AccessRight> accessRights = new ArrayList<AccessRight>(Arrays.asList(AccessRight.values()));
        model.addAttribute("userAccessRights", userAccessRights);
        model.addAttribute("profileAccessRights", profileAccessRights);
        model.addAttribute("mgrTemplate", mgrTemplate);
        model.addAttribute("mgrUsers", mgrUsers);
        model.addAttribute("mgrProfiles", mgrProfiles);
        model.addAttribute("accessRights", accessRights);
        model.addAttribute("templateId", templateId);
        model.addAttribute("workspaceId", workspaceId);
        return "templateAccessRight/editTemplateAccessRight";
    }

    /**
     * @author p-tvo-thuynd this method is to update info of a template into DB
     * @param userIdAccessRight
     *            string of userId and access right to template of them
     * @param profileIdAccessRight
     *            string of profileId and access right to template of them
     * @param templateId
     *            id of choosen template
     * @param workspaceId
     *            id of workspace which template belongs to
     * @param model
     *            to hold model attribute
     * @return path of screen
     */
    @RequestMapping(value = "/updateTemplateAccessRight", method = RequestMethod.POST)
    public String updateOne(@ModelAttribute("userIdAccessRight") String userIdAccessRight,
            @ModelAttribute("profileIdAccessRight") String profileIdAccessRight,
            @ModelAttribute("templateId") String templateId, @ModelAttribute("workspaceId") String workspaceId,
            Model model) {
        try {
            TemplateKey templateKey = new TemplateKey();
            templateKey.setTemplateId(templateId);
            templateKey.setWorkspaceId(workspaceId);

            // convert string userIdAccessRight to list of UserAccessRight
            // object
            List<UserAccessRight> userAccessRights = new ArrayList<UserAccessRight>();
            if (!StringUtils.isEmpty(userIdAccessRight)) {
                String[] userIdArr = userIdAccessRight.split(",");
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

            // convert string profileIdAccessRight to list of ProfileAccessRight
            // object
            List<ProfileAccessRight> profileAccessRights = new ArrayList<ProfileAccessRight>();
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

            // save user authority to template to CTL_TEMPLATE table
            templateService.saveAuthority(templateKey, userAccessRights, profileAccessRights);
            List<MgrTemplate> mgrTemplates = templateService.getTemplateListInfo(workspaceId);
            model.addAttribute("workspaceId", workspaceId);
            model.addAttribute("mgrTemplates", mgrTemplates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "templateAccessRight/showList";
    }
}
