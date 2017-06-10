package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.form.TemplateAuthorityForm;
import co.jp.nej.earth.service.ProfileService;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.service.UserService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/templateAccessRight")
public class TemplateAuthorityController extends BaseController {

    public static final String URL = "templateAccessRight";
    @Autowired
    private TemplateService templateService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    /**
     * @author p-tvo-thuynd this method is to show list of workspace in openning
     *         screen of Template list
     * @param model
     *            to hold model attribute
     * @return path of screen
     */
    /*
     * @RequestMapping(value = "/initTemplateListScreen", method =
     * RequestMethod.GET) public String showListWorkspace(Model model) { try {
     * List<MgrWorkspace> mgrWorkspaces = templateService.getAllWorkspace();
     * model.addAttribute("mgrWorkspaces", mgrWorkspaces); } catch
     * (EarthException e) { String message =
     * "Error: Can not get workspace list"; model.addAttribute("message",
     * message); } return "templateAccessRight/showList"; }
     */

    /**
     * @param model to hold model attribute
     * @return path of screen
     * @throws EarthException
     * @author p-tvo-thuynd this method is to get list of template based on
     * workspace id
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String getListTemplate(Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("mgrTemplates", templateService.getTemplateListInfo(workspaceId));
        return "templateAccessRight/showList";
    }

    /**
     * @param model      to hold model attribute
     * @param templateId id of choosen template
     * @return path of screen
     * @throws EarthException
     * @author p-tvo-thuynd this method is to show detail infor of an template
     */
    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(Model model, String templateId, HttpServletRequest request) throws EarthException {
        /*
         * TemplateKey templateKey = new TemplateKey();
         * templateKey.setTemplateId(templateId);
         * templateKey.setWorkspaceId(workspaceId); MgrTemplate mgrTemplate =
         * templateService.getById(templateKey); List<UserAccessRight>
         * userAccessRights = templateService.getUserAuthority(templateKey);
         * List<ProfileAccessRight> profileAccessRights =
         * templateService.getProfileAuthority(templateKey); List<MgrUser>
         * mgrUsers = templateService.getAllUser(); List<MgrProfile> mgrProfiles
         * = templateService.getAllProfile(); List<AccessRight> accessRights =
         * new ArrayList<AccessRight>(Arrays.asList(AccessRight.values()));
         * model.addAttribute("userAccessRights", userAccessRights);
         * model.addAttribute("profileAccessRights", profileAccessRights);
         * model.addAttribute("mgrTemplate", mgrTemplate);
         * model.addAttribute("mgrUsers", mgrUsers);
         * model.addAttribute("mgrProfiles", mgrProfiles);
         * model.addAttribute("accessRights", accessRights);
         * model.addAttribute("templateId", templateId);
         * model.addAttribute("workspaceId", workspaceId);
         */
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("templateId", templateId);

        model.addAttribute("mgrUsers", userService.getAll());
        model.addAttribute("mgrProfiles", profileService.getAll());

        List<AccessRight> accessRights = new ArrayList<AccessRight>(Arrays.asList(AccessRight.values()));
        model.addAttribute("accessRights", accessRights);

        TemplateKey templateKey = new TemplateKey();
        templateKey.setTemplateId(templateId);
        templateKey.setWorkspaceId(workspaceId);
        MgrTemplate mgrTemplate = templateService.getById(templateKey);
        List<UserAccessRight> userAccessRights = templateService.getUserAuthority(templateKey);
        List<ProfileAccessRight> profileAccessRights = templateService.getProfileAuthority(templateKey);

        TemplateAuthorityForm templateAuthorityForm = new TemplateAuthorityForm(mgrTemplate.getTemplateId(),
                mgrTemplate.getTemplateName(), userAccessRights, profileAccessRights);
        model.addAttribute("templateAuthorityForm", templateAuthorityForm);
        return "templateAccessRight/editTemplateAccessRight";
    }

    /**
     * @param model to hold model attribute
     * @return path of screen
     * @author p-tvo-thuynd this method is to update info of a template into DB
     */
    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(TemplateAuthorityForm templateAuthorityForm, HttpServletRequest request,
                            Model model) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        try {
            TemplateKey templateKey = new TemplateKey();
            templateKey.setTemplateId(templateAuthorityForm.getTemplateId());
            templateKey.setWorkspaceId(workspaceId);

            List<UserAccessRight> userAccessRights = new ArrayList<>();
            List<UserAccessRight> userAccessRights1 = templateAuthorityForm.getUserAccessRights();
            if (userAccessRights1 != null) {
                for (UserAccessRight userAccessRight : userAccessRights1) {
                    if (userAccessRight.getUserId() != null) {
                        userAccessRights.add(userAccessRight);
                    }
                }
            }
            List<ProfileAccessRight> profileAccessRights = new ArrayList<>();
            List<ProfileAccessRight> profileAccessRights1 = templateAuthorityForm.getProfileAccessRights();
            if (profileAccessRights1 != null) {
                for (ProfileAccessRight profileAccessRight : profileAccessRights1) {
                    if (profileAccessRight.getProfileId() != null) {
                        profileAccessRights.add(profileAccessRight);
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
        return redirectToList(URL);
    }
}
