package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EAutoIncrease;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.EarthId;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.service.DirectoryService;
import co.jp.nej.earth.service.SiteService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.SiteForm;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/site")
public class SiteController extends BaseController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private EAutoIncrease eSiteId;

    @Autowired
    private ValidatorUtil validatorUtil;

    private static final String URL = "site";

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
    }

    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String showList(Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());

        HttpSession session = request.getSession();
        SessionUtil.clearAllOtherSearchCondition(session, Constant.ScreenKey.PROCESS_SITE);
        ClientSearchForm searchForm = (ClientSearchForm) SessionUtil.getSearchCondtionValue(session,
            Constant.ScreenKey.PROCESS_SITE);

        if(searchForm == null) {
            searchForm = new ClientSearchForm();
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("siteIds", siteService.getAllSiteIds(workspaceId));
        model.addAttribute("messages", model.asMap().get("messages"));
        return "site/siteList";
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("siteIds") String siteIds, Model model, HttpServletRequest request,
        ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SITE, searchForm);
        if (!EStringUtil.isEmpty(siteIds)) {
            List<Integer> siteIdInteger = new ArrayList<>();
            List<String> siteIdList = Arrays.asList(siteIds.split("\\s*,\\s*"));
            for (String siteId : siteIdList) {
                siteIdInteger.add(Integer.valueOf(siteId));
            }
            siteService.deleteSites(siteIdInteger, Constant.EARTH_WORKSPACE_ID);
        }
        return redirectToList("site");
    }

    @RequestMapping(value = "/addNew", method = {RequestMethod.GET, RequestMethod.POST})
    public String addNew(Model model, HttpServletRequest request, ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SITE, searchForm);
        List<Directory> directories = directoryService.getAllDirectories();
        SiteForm siteForm = new SiteForm();
        siteForm.setSiteId(String.valueOf(eSiteId.getAutoId(EarthId.SITE,request.getSession().getId())));
        siteForm.setDirectories(directories);
        model.addAttribute("siteForm", siteForm);
        return "site/addSite";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("siteForm") SiteForm siteForm, BindingResult result,
                            HttpServletRequest request, Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            List<Directory> directories = directoryService.getAllDirectories();
            String directoryBySites = siteForm.getDirectoryIds();
            List<String> directoryIdList = Arrays.asList(directoryBySites.split("\\s*,\\s*"));
            siteForm.setDirectories(directories);
            if (!EStringUtil.isEmpty(directoryBySites) && directoryIdList.size() > 0) {
                for (Directory directory : directories) {
                    for (String directoryId : directoryIdList) {
                        if (directory.getDataDirectoryId() == Integer.valueOf(directoryId)) {
                            directory.setChecked(true);
                        }
                    }
                }
            }
            siteForm.setLastUpdateTime(null);
            model.addAttribute("siteForm", siteForm);
            model.addAttribute(Session.MESSAGES, messages);
            return "site/addSite";
        }
        List<String> directoryIdList = Arrays.asList(siteForm.getDirectoryIds().split("\\s*,\\s*"));
        siteService.insertOne(siteForm.getSiteId(), directoryIdList, Constant.EARTH_WORKSPACE_ID);
        return redirectToList("site");
    }

    @RequestMapping(value = "/showDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDetail(@ModelAttribute("siteId") String siteId, Model model, HttpServletRequest request,
         ClientSearchForm searchForm) throws EarthException {
        SessionUtil.setSearchCondtionValue(request.getSession(), Constant.ScreenKey.PROCESS_SITE, searchForm);
        List<Directory> directorys = directoryService.getAllDirectories();
        List<Directory> directoryBySites = directoryService.getAllDirectoriesBySite(siteId,
                Constant.EARTH_WORKSPACE_ID);
        SiteForm siteForm = new SiteForm();

        siteForm.setLastUpdateTime(DateUtil.getCurrentDateString());

        siteForm.setSiteId(siteId);
        siteForm.setDirectories(directorys);
        for (Directory directory : directorys) {
            for (Directory directoryBySite : directoryBySites) {
                if (directory.getDataDirectoryId() == directoryBySite.getDataDirectoryId()) {
                    directory.setChecked(true);
                }
            }
        }
        model.addAttribute("siteForm", siteForm);
        return "site/addSite";
    }

    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("siteForm") SiteForm siteForm, BindingResult result,
                            @ModelAttribute("directoryIds") String directoryIds, Model model,
                            HttpServletRequest request)
            throws EarthException {

        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            List<Directory> directories = directoryService.getAllDirectories();
            String directoryBySites = siteForm.getDirectoryIds();
            List<String> directoryIdList = Arrays.asList(directoryBySites.split("\\s*,\\s*"));
            siteForm.setDirectories(directories);
            if (!EStringUtil.isEmpty(directoryBySites) && directoryIdList.size() > 0) {
                for (Directory directory : directories) {
                    for (String directoryId : directoryIdList) {
                        if (directory.getDataDirectoryId() == Integer.valueOf(directoryId)) {
                            directory.setChecked(true);
                        }
                    }
                }
            }
            model.addAttribute("siteForm", siteForm);
            model.addAttribute(Session.MESSAGES, messages);
            return "site/addSite";
        }
        List<String> directoryIdList = Arrays.asList(directoryIds.split("\\s*,\\s*"));
        siteService.updateSite(siteForm.getSiteId(), directoryIdList, Constant.EARTH_WORKSPACE_ID);
        return redirectToList("site");
    }

}
