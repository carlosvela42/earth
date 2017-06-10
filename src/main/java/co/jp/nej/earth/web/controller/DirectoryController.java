package co.jp.nej.earth.web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EDatadirectoryId;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.service.DirectoryService;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.DirectoryForm;

@Controller
@RequestMapping("/directory")
public class DirectoryController extends BaseController {

  @Autowired
  private DirectoryService directoryService;

  @Autowired
  private ValidatorUtil validatorUtil;

  @Autowired
  private EDatadirectoryId eDatadirectoryId;

  private static final String URL = "directory";
  private static final String ADD_URL = "directory/addDirectory";

  @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
  public String showList(Model model, HttpServletRequest request) throws EarthException {
    model.addAttribute("directorys", directoryService.getAllDirectories());
    return "directory/directoryList";
  }

  @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
  public String deleteList(DeleteProcessForm deleteForm) {
    try {
      List<Integer> intDataDirectoryIds = deleteForm.getProcessIds();
      directoryService.deleteDirectorys(intDataDirectoryIds, Constant.EARTH_WORKSPACE_ID);
      return redirectToList(URL);
    } catch (Exception ex) {
      return redirectToList(URL);
    }
  }

  @RequestMapping(value = "/addNew", method = RequestMethod.GET)
  public String addNew(Model model, HttpServletRequest request) throws EarthException {
    DirectoryForm directoryForm = new DirectoryForm();
    directoryForm.setDataDirectoryId(eDatadirectoryId.getAutoId());
    directoryForm.setDiskVolSize(String.valueOf(Constant.Directory.DEFAULT_VALUE));
    directoryForm.setNewCreateFile(String.valueOf(Constant.Directory.YES));
    model.addAttribute("directoryForm", directoryForm);
    return ADD_URL;
  }

  @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
  public String insertOne(@Valid @ModelAttribute("directoryForm") DirectoryForm directoryForm, BindingResult result,
      HttpServletRequest request, Model model) throws EarthException {
    List<Message> messages = validatorUtil.validate(result);
    directoryForm.setLastUpdateTime(null);
    if (messages.size() > 0) {
      model.addAttribute("directoryForm", directoryForm);
      model.addAttribute(Session.MESSAGES, messages);
      return ADD_URL;
    }
    Directory directory = new Directory();
    directory.setDataDirectoryId(Integer.valueOf(directoryForm.getDataDirectoryId()));
    directory.setNewCreateFile(Integer.valueOf(directoryForm.getNewCreateFile()));
    directory.setDiskVolSize(directoryForm.getDiskVolSize());
    directory.setReservedDiskVolSize(directoryForm.getReservedDiskVolSize());
    directory.setFolderPath(directoryForm.getFolderPath());

    messages = directoryService.validateInsert(directory, Constant.EARTH_WORKSPACE_ID);
    if (messages != null && messages.size() > 0) {
      model.addAttribute(Session.MESSAGES, messages);
      model.addAttribute("directory", directory);
      return ADD_URL;
    } else {
      directoryService.insertOne(directory, Constant.EARTH_WORKSPACE_ID);
      return redirectToList(URL);
    }
  }

  @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
  public String showDetail(@ModelAttribute("dataDirectoryId") String dataDirectoryId, Model model,
      HttpServletRequest request) throws EarthException {
    DirectoryForm directoryForm = new DirectoryForm();
    if (!EStringUtil.isEmpty(dataDirectoryId)) {
      Directory directory = directoryService.getById(dataDirectoryId, Constant.EARTH_WORKSPACE_ID);
      directoryForm.setDataDirectoryId(String.valueOf(directory.getDataDirectoryId()));
      directoryForm.setNewCreateFile(String.valueOf(directory.getNewCreateFile()));
      directoryForm.setReservedDiskVolSize(directory.getReservedDiskVolSize());
      directoryForm.setDiskVolSize(directory.getDiskVolSize());
      directoryForm.setFolderPath(directory.getFolderPath());
      directoryForm.setLastUpdateTime(directory.getLastUpdateTime());
      model.addAttribute("directoryForm", directoryForm);
    }
    return ADD_URL;
  }

  @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
  public String updateOne(@Valid @ModelAttribute("directoryForm") DirectoryForm directoryForm, BindingResult result,
      HttpServletRequest request, Model model) throws EarthException {
    List<Message> messages = validatorUtil.validate(result);
    if (messages.size() > 0) {
      model.addAttribute("directoryForm", directoryForm);
      model.addAttribute(Session.MESSAGES, messages);
      return ADD_URL;
    }
    Directory directory = new Directory();
    directory.setDataDirectoryId(Integer.valueOf(directoryForm.getDataDirectoryId()));
    directory.setNewCreateFile(Integer.valueOf(directoryForm.getNewCreateFile()));
    directory.setDiskVolSize(directoryForm.getDiskVolSize());
    directory.setReservedDiskVolSize(directoryForm.getReservedDiskVolSize());
    directory.setFolderPath(directoryForm.getFolderPath());
    directoryService.updateDirectory(directory, Constant.EARTH_WORKSPACE_ID);
    return redirectToList(URL);
  }

  @RequestMapping(value = "/getSizeFolder", method = RequestMethod.GET)
  public @ResponseBody long getSizeFolder(@ModelAttribute("folderPath") String folderPath, Model model,
      HttpServletRequest request) throws EarthException {
    File file = new File(folderPath);
    long freeSpace = 0L;
    if (file.isDirectory() && file.exists()) {
      freeSpace = file.getFreeSpace(); // free disk space in bytes.
      freeSpace = freeSpace / (Constant.Directory.CONVERT);
    }
    return freeSpace;
  }

  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  public String cancel() {
    return redirectToList(URL);
  }
}
