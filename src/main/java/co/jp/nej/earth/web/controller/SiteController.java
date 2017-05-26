package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.ESiteId;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.service.DirectoryService;
import co.jp.nej.earth.service.SiteService;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/site")
public class SiteController extends BaseController {

  @Autowired
  private SiteService siteService;

  @Autowired
  private DirectoryService directoryService;

  @Autowired
  private ESiteId eSiteId;

  @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
  public String showList(Model model, HttpServletRequest request) throws EarthException {
    model.addAttribute("siteIds", siteService.getAllSiteIds(Constant.EARTH_WORKSPACE_ID));
    return "site/siteList";
  }

  @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
  public String deleteList(@ModelAttribute("siteIds") String siteIds, Model model,
      HttpServletRequest request) throws EarthException {
    if (!EStringUtil.isEmpty(siteIds)) {
      List<Integer> siteIdInteger = new ArrayList<>();
      List<String> siteIdList = Arrays.asList(siteIds.split("\\s*,\\s*"));
      for (String siteId : siteIdList) {
        siteIdInteger.add(Integer.valueOf(siteId));
      }
      siteService.deleteSites(siteIdInteger, Constant.EARTH_WORKSPACE_ID);
    }
    return redirectToList();
  }

  @RequestMapping(value = "/addNew", method = RequestMethod.GET)
  public String addNew(Model model, HttpServletRequest request) throws EarthException {
    List<Directory> directorys = directoryService.getAllDirectories();
    Site site = new Site();
    site.setSiteId(eSiteId.getAutoId());
    model.addAttribute("site", site);
    model.addAttribute("directorys", directorys);
    return "site/addSite";
  }

  @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
  public String insertOne(@ModelAttribute("siteId") String siteId,
      @ModelAttribute("directoryIds") String directoryIds, HttpServletRequest request)
      throws EarthException {
    if (!EStringUtil.isEmpty(siteId) && !EStringUtil.isEmpty(directoryIds)) {
      List<String> directoryIdList = Arrays.asList(directoryIds.split("\\s*,\\s*"));
      siteService.insertOne(siteId, directoryIdList, Constant.EARTH_WORKSPACE_ID);
    }
    return redirectToList();
  }

  @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
  public String showDetail(@ModelAttribute("siteId") String siteId, Model model,
      HttpServletRequest request) throws EarthException {
    List<Directory> directorys = directoryService.getAllDirectories();
    List<Directory> directoryBySites = directoryService.getAllDirectoriesBySite(siteId,
        Constant.EARTH_WORKSPACE_ID);
    Site site = new Site();
    site.setSiteId(Integer.parseInt(siteId));
    for (Directory directory : directorys) {
      for (Directory directoryBySite : directoryBySites) {
        if (directory.getDataDirectoryId() == directoryBySite.getDataDirectoryId()) {
          directory.setChecked(true);
        }
      }
    }
    model.addAttribute("site", site);
    model.addAttribute("directorys", directorys);
    return "site/editSite";
  }

  @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
  public String updateOne(@ModelAttribute("siteId") String siteId,
      @ModelAttribute("directoryIds") String directoryIds, HttpServletRequest request)
      throws EarthException {
    if (!EStringUtil.isEmpty(siteId) && !EStringUtil.isEmpty(directoryIds)) {
      List<String> directoryIdList = Arrays.asList(directoryIds.split("\\s*,\\s*"));
      siteService.updateSite(siteId, directoryIdList, Constant.EARTH_WORKSPACE_ID);
    }
    return redirectToList();
  }

}
