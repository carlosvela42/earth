package co.jp.nej.earth.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessDetailForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.service.ProcessService;
import co.jp.nej.earth.service.SiteService;
import co.jp.nej.earth.service.WorkspaceService;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SiteService siteService;

    /**
     * get processes and workspaces from db
     *
     * @param workSpaceId
     * @param model
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/showList", method = RequestMethod.GET)
    public String showList(@RequestParam(value = "workspaceId", required = false) String workspaceId, Model model)
            throws EarthException {
        // String workSpaceId = (String) request.getAttribute("workspaceId");
        // get all work space
        List<MgrWorkspace> workspaces = workspaceService.getAll();
        // if workspace id from parameter is null
        if (workspaceId == null || workspaceId.isEmpty()) {
            workspaceId = workspaces.get(0).getWorkspaceId();
        }
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("processes", processService.getAllByWorkspace(workspaceId));
        return "process/processList";
    }

    /**
     * get list site id
     *
     * @param workSpaceId
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/addNew", method = RequestMethod.POST)
    public @ResponseBody List<Integer> addNew(@RequestAttribute("workSpaceId") String workSpaceId)
            throws EarthException {
        return siteService.getAllSiteIds(workSpaceId);
    }

    /**
     * insert process to db
     *
     * @param processForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertOne(@ModelAttribute("processForm") ProcessForm processForm)
            throws EarthException {
        Map<String, Object> result = new HashMap<>();
        Response respone = processService.validateProcess(processForm);
        if (!respone.isResult()) {
            result.put("message", respone.getMessage());
            return result;
        }
        result.put("success", processService.insertOne(processForm));
        return result;
    }

    /**
     * show process detail
     *
     * @param processDetailForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/showDetail", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> showDetail(
            @ModelAttribute("processDetailForm") ProcessDetailForm processDetailForm) throws EarthException {
        Map<String, Object> result = processService.getDetail(processDetailForm.getWorkspaceId(),
                processDetailForm.getProcessId());
        result.put("siteIds", siteService.getAllSiteIds(processDetailForm.getWorkspaceId()));
        return result;
    }

    /**
     * update process
     *
     * @param processForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> updateOne(@ModelAttribute("processForm") ProcessForm processForm)
            throws EarthException {
        Map<String, Object> result = new HashMap<>();
        Response respone = processService.validateProcess(processForm);
        if (!respone.isResult()) {
            result.put("message", respone.getMessage());
            return result;
        }
        result.put("success", processService.updateOne(processForm));
        return result;
    }

    /**
     * delete list process
     *
     * @param deleteProcessForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteList(
            @RequestBody DeleteProcessForm deleteProcessForm) throws EarthException {
        Map<String, Object> result = new HashMap<>();
        Response respone = processService.validateDeleteAction(deleteProcessForm);
        if (!respone.isResult()) {
            result.put("message", respone.getMessage());
            return result;
        }
        result.put("result", processService.deleteList(deleteProcessForm));
        return result;
    }

    /**
     * download file
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response) throws IOException {
        // TODO
        File file = new File("C:\\temp.txt");
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
