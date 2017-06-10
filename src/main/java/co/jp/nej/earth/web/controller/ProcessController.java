package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.StrageDb;
import co.jp.nej.earth.model.StrageFile;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.service.ProcessService;
import co.jp.nej.earth.service.SiteService;
import co.jp.nej.earth.service.WorkspaceService;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author p-tvo-sonta
 */
@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {

    public static final String URL = "process";
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SiteService siteService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private SmartValidator validator;

    /**
     * get processes and workspaces from db
     *
     * @param model
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showList(Model model, HttpServletRequest request) throws EarthException {
        SessionUtil.loadWorkspaces(workspaceService, model, request);
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        model.addAttribute("processes", processService.getAllByWorkspace(workspaceId));
        model.addAttribute("messages", model.asMap().get("messages"));
        return "process/processList";
    }

    /**
     * get list site id
     *
     * @return
     * @throws EarthException
     */
    @RequestMapping(value = "/addNew", method = RequestMethod.GET)
    public String addNew(Model model, HttpServletRequest request)
            throws EarthException {
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
        validator.validate(processForm.getProcess(), result);
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
    public String updateOne(@Valid @ModelAttribute("processForm") ProcessForm processForm,
                            BindingResult result, HttpServletRequest request, Model model)
            throws EarthException {
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
    public String deleteList(
            DeleteProcessForm deleteProcessForm, final RedirectAttributes redirectAttrs) throws EarthException {
        Map<String, Object> result = new HashMap<>();
        Response respone = processService.validateDeleteAction(deleteProcessForm);
        // todo: delete all process in list
        if (!respone.isResult()) {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("error", respone.getMessage()));
            redirectAttrs.addFlashAttribute("messages", messages);
        }
        return redirectToList();
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        return redirectToList(URL);
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
