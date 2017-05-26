package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.form.*;
import co.jp.nej.earth.model.ws.*;
import co.jp.nej.earth.service.*;
import co.jp.nej.earth.util.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SiteService siteService;

    @Autowired
    private ValidatorUtil validatorUtil;

    /**
     * get processes and workspaces from db
     *
     * @param workSpaceId
     * @param model
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public String showList(Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("processes", processService.getAllByWorkspace(workspaceId));
        return "process/processList";
    }

    /**
     * get list site id
     *
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model, HttpServletRequest request) throws EarthException {
        loadInfo(request, model, new ProcessForm());
        return "process/addProcess";
    }

    /**
     * insert process to db
     *
     * @param processForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/insertOne", method = RequestMethod.POST)
    public String insertOne(@Valid @ModelAttribute("processForm") ProcessForm processForm, BindingResult result,
            Model model, HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(processService.validateProcess(processForm));
        if (messages.size() > 0) {
            loadInfo(request, model, processForm);
            model.addAttribute("messages", messages);
            return "process/addProcess";
        }
        processForm.setWorkspaceId(SessionUtil.getSearchConditionWorkspaceId(request.getSession()));
        processService.insertOne(processForm);
        return redirectToList();
    }

    /**
     * show process detail
     *
     * @param processDetailForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/showDetail", method = RequestMethod.GET)
    public String showDetail(String processId, HttpServletRequest request, Model model) throws EarthException {
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        Map<String, Object> result = processService.getDetail(workspaceId, processId);
        ProcessForm processForm = new ProcessForm();
        processForm.setProcess((MgrProcess) result.get("process"));
        processForm.setStrageDb((StrageDb) result.get("strageDb"));
        processForm.setStrageFile((StrageFile) result.get("strageFile"));

        loadInfo(request, model, processForm);

        return "process/addProcess";
    }

    /**
     * update process
     *
     * @param processForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/updateOne", method = RequestMethod.POST)
    public String updateOne(@Valid @ModelAttribute("processForm") ProcessForm processForm, BindingResult result,
            HttpServletRequest request, Model model) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        messages.addAll(processService.validateProcess(processForm));

        if (messages.size() > 0) {
            loadInfo(request, model, processForm);
            model.addAttribute("messages", messages);
            return "process/addProcess";
        }

        processService.updateOne(processForm);
        return redirectToList();
    }

    /**
     * delete list process
     *
     * @param deleteProcessForm
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteList(DeleteProcessForm deleteProcessForm) throws EarthException {
        Map<String, Object> result = new HashMap<>();
        Response respone = processService.validateDeleteAction(deleteProcessForm);
        if (!respone.isResult()) {
            result.put("message", respone.getMessage());
            return result;
        }
        result.put("result", processService.deleteList(deleteProcessForm));
        return result;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList();
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

    private void loadInfo(HttpServletRequest request, Model model, ProcessForm processForm) throws EarthException {
        String workSpaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("siteIds", siteService.getAllSiteIds(workSpaceId));
        model.addAttribute("workspaceId", workSpaceId);

        MgrProcess process = processForm.getProcess();
        if (process == null) {
            process = new MgrProcess();
            process.setDocumentDataSavePath("database");
        }
        model.addAttribute("process", process);

        StrageDb strageDb = processForm.getStrageDb();
        if (strageDb == null) {
            strageDb = new StrageDb();
        }
        model.addAttribute("strageDb", strageDb);

        StrageFile strageFile = processForm.getStrageFile();
        if (strageFile == null) {
            strageFile = new StrageFile();
        }
        model.addAttribute("strageFile", strageFile);
    }
}
