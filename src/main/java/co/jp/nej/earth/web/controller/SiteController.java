package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import co.jp.nej.earth.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.model.constant.Constant;
//import co.jp.nej.earth.service.DirectoryService;
import co.jp.nej.earth.service.SiteService;
import co.jp.nej.earth.util.EStringUtil;

@Controller
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private DirectoryService directoryService;

    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(Model model, HttpServletRequest request) throws EarthException {
        String result = "site/siteList";
        List<Site> sites = siteService.getAllSites();
        model.addAttribute("sites", sites);
        return result;
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(@ModelAttribute("siteIds") String siteIds, Model model, HttpServletRequest request)
            throws EarthException {
        List<Integer> siteIdInteger = new ArrayList<>();
        String result = EStringUtil.EMPTY;
        boolean status;
        List<String> siteIdList = Arrays.asList(siteIds.split("\\s*,\\s*"));
        for (String siteId : siteIdList) {
            siteIdInteger.add(Integer.valueOf(siteId));
        }
        status = siteService.deleteSites(siteIdInteger, Constant.EARTH_WORKSPACE_ID);
        if (status) {
            result = "redirect:showList";
        } else {
            result = "error/error";
        }

        return result;
    }

    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model, HttpServletRequest request) throws EarthException {

        List<Site> sites = siteService.getAllSites();
        List<Directory> directorys = directoryService.getAllDirectories();

        Site site = new Site();
        List<Integer> siteIds = new ArrayList<>();

        if (sites.size() > 0) {
            for (Site siteTemp : sites) {
                siteIds.add(siteTemp.getSiteId());
            }
            site.setSiteId(Collections.max(siteIds) + 1);
        } else {
            site.setSiteId(1);
        }
        model.addAttribute("site", site);
        model.addAttribute("directorys", directorys);
        return "site/addSite";
    }

    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@ModelAttribute("siteId") String siteId,
            @ModelAttribute("directoryIds") String directoryIds) throws EarthException {
        String result = EStringUtil.EMPTY;
        List<String> directoryIdList = Arrays.asList(directoryIds.split("\\s*,\\s*"));
        boolean success = siteService.insertOne(siteId, directoryIdList, Constant.EARTH_WORKSPACE_ID);
        if (success) {
            result = "redirect:showList";
        }
        return result;
    }

    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(@ModelAttribute("siteId") String siteId, Model model, HttpServletRequest request)
            throws EarthException {
        Site site = new Site();
        List<Directory> directorys = siteService.getDirectorysBySiteId(siteId, Constant.EARTH_WORKSPACE_ID);
        site.setSiteId(Integer.parseInt(siteId));
        model.addAttribute("site", site);
        model.addAttribute("directorys", directorys);

        return "site/editSite";
    }

}
